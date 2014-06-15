package org.vvgaming.harmegido.vision;

import java.io.IOException;
import java.util.List;

import org.opencv.android.CameraBridgeViewBase.ListItemAccessor;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;

/**
 * Wrapper para captura de frames da camera.
 * 
 * @author Vinicius Nogueira
 */
public class JavaCam implements PreviewCallback {

	private static final int MAGIC_TEXTURE_ID = 10;
	private SurfaceTexture mSurfaceTexture;

	private byte mBuffer[];

	private Camera camera;

	private JavaCameraFrame lastFrame = JavaCameraFrame.empty;
	private int mFrameWidth;
	private int mFrameHeight;

	/**
	 * Conecta à camera
	 * 
	 * @param width
	 *            largura da resolução
	 * @param height
	 *            altura da resolução
	 * 
	 * @return <code>true</code> se houve sucesso na conexão
	 */
	public void connectCamera(int width, int height) {
		synchronized (this) {
			camera = null;

			camera = Camera.open();

			if (camera == null
					&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				boolean connected = false;
				for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
					camera = Camera.open(camIdx);
					connected = true;
					if (connected)
						break;
				}
			}

			if (camera == null)
				throw new IllegalStateException(
						"Não foi possível conectar à câmera");

			Camera.Parameters params = camera.getParameters();

			params.setPreviewFormat(ImageFormat.NV21);
			params.setPreviewSize(width, height);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
				params.setRecordingHint(true);

			List<String> FocusModes = params.getSupportedFocusModes();
			if (FocusModes != null
					&& FocusModes
							.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}

			camera.setParameters(params);
			params = camera.getParameters();

			mFrameWidth = params.getPreviewSize().width;
			mFrameHeight = params.getPreviewSize().height;

			int size = mFrameWidth * mFrameHeight;
			size = size
					* ImageFormat.getBitsPerPixel(params.getPreviewFormat())
					/ 8;
			mBuffer = new byte[size];

			camera.addCallbackBuffer(mBuffer);
			camera.setPreviewCallbackWithBuffer(this);

			try {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mSurfaceTexture = new SurfaceTexture(MAGIC_TEXTURE_ID);
					camera.setPreviewTexture(mSurfaceTexture);
				} else
					camera.setPreviewDisplay(null);

			} catch (IOException ioe) {
				throw new IllegalStateException(
						"Não foi possível conectar à câmera", ioe);
			}
			camera.startPreview();
		}
	}

	/**
	 * Libera a camera conectada
	 */
	public void disconnectCamera() {
		synchronized (this) {
			if (camera != null) {
				camera.stopPreview();
				camera.setPreviewCallback(null);

				camera.release();
			}
			camera = null;
			if (lastFrame != null) {
				lastFrame.release();
			}
		}
	}

	public void onPreviewFrame(byte[] frame, Camera arg1) {
		synchronized (this) {
			// TODO deveria dar um release no lastFrame agora, mas não pode pq
			// ele esta sendo acessado para desenhar na tela
			// como fazer para dar o release de maneira segura?
			Mat mat = new Mat(mFrameHeight + (mFrameHeight / 2), mFrameWidth,
					CvType.CV_8UC1);
			mat.put(0, 0, frame);
			lastFrame = new JavaCameraFrame(mat, mFrameWidth, mFrameHeight);
		}
		if (camera != null)
			camera.addCallbackBuffer(mBuffer);

	}

	public JavaCameraFrame getLastFrame() {
		return lastFrame;
	}

	public int getHeight() {
		// retorna invertido INTENCIONALMENTE, pois a imagem está sendo
		// capturada invertida e após rodar temos a inversão de width e height
		// TODO arrumar isso
		return mFrameWidth;
	};

	public int getWidth() {
		// retorna invertido INTENCIONALMENTE, pois a imagem está sendo
		// capturada invertida e após rodar temos a inversão de width e height
		// TODO arrumar isso
		return mFrameHeight;
	}

	public static class JavaCameraSizeAccessor implements ListItemAccessor {

		public int getWidth(Object obj) {
			Camera.Size size = (Camera.Size) obj;
			return size.width;
		}

		public int getHeight(Object obj) {
			Camera.Size size = (Camera.Size) obj;
			return size.height;
		}
	}

}

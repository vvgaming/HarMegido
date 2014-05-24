package org.vvgaming.harmegido.vision;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

/**
 * Wrapper para captura de frames da camera. Essa classe � capaz de rodar uma
 * Thread e ficar capturando frames e deixando-os dispon�veis quando necess�rios
 * 
 * @author Vinicius Nogueira
 */
public class Cam {

	private boolean stopGrabbing;
	private Thread grabbingThread;
	private int width;
	private int height;
	private VideoCapture mCamera;

	public boolean connectCamera(int width, int height) {

		this.width = width;
		this.height = height;

		if (!initializeCamera())
			return false;

		grabbingThread = new Thread(new CameraWorker());
		grabbingThread.start();

		return true;
	}

	public void disconnectCamera() {
		if (grabbingThread != null) {
			try {
				stopGrabbing = true;
				grabbingThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				grabbingThread = null;
				stopGrabbing = false;
			}
		}
		releaseCamera();
	}

	private boolean initializeCamera() {
		synchronized (this) {

			mCamera = new VideoCapture(Highgui.CV_CAP_ANDROID);

			if (mCamera == null)
				return false;

			if (mCamera.isOpened() == false)
				return false;

			// java.util.List<Size> sizes = mCamera.getSupportedPreviewSizes();
			//
			// Size frameSize = calculateCameraFrameSize(sizes,
			// new OpenCvSizeAccessor(), width, height);
			//
			// mFrameWidth = (int) frameSize.width;
			// mFrameHeight = (int) frameSize.height;
			//
			// if ((getLayoutParams().width == LayoutParams.MATCH_PARENT)
			// && (getLayoutParams().height == LayoutParams.MATCH_PARENT))
			// mScale = Math.min(((float) height) / mFrameHeight,
			// ((float) width) / mFrameWidth);
			// else
			// mScale = 0;
			//
			// mCamera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, frameSize.width);
			// mCamera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, frameSize.height);

			mCamera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, width);
			mCamera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, height);
		}

		return true;
	}

	public void releaseCamera() {
		synchronized (this) {
			if (mCamera != null) {
				mCamera.release();
			}
		}
	}

	private NativeCameraFrame lastFrame = NativeCameraFrame.empty;

	private class CameraWorker implements Runnable {

		public void run() {
			do {
				if (!mCamera.grab()) {
					break;
				}
				lastFrame = (new NativeCameraFrame(mCamera));
				try {
					// TODO esse sleep � s� uma gambiarra para diminuir o
					// consumo, dado que a camera s� pega em no m�ximo 30 fps,
					// n�o adianta ficar indo t�o r�pido. O ideal � ajustar isso
					// aqui depois para ficar mais inteligente
					Thread.sleep(20);
				} catch (InterruptedException ignored) {
				}
			} while (!stopGrabbing);
		}
	}

	public NativeCameraFrame getLastFrame() {
		return lastFrame;
	}

	public static class NativeCameraFrame implements CvCameraViewFrame {

		@Override
		public Mat rgba() {
			mCapture.retrieve(mRgba, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA);
			// n�o sei pq est�o sendo capturadas giradas
			Core.flip(mRgba.t(), mRgba, 1);
			return mRgba;
		}

		@Override
		public Mat gray() {
			mCapture.retrieve(mGray, Highgui.CV_CAP_ANDROID_GREY_FRAME);
			// n�o sei pq est�o sendo capturadas giradas
			Core.flip(mGray.t(), mGray, 1);
			return mGray;
		}

		public NativeCameraFrame(VideoCapture capture) {
			mCapture = capture;
			mGray = new Mat();
			mRgba = new Mat();
		}

		private VideoCapture mCapture;
		private Mat mRgba;
		private Mat mGray;

		public static NativeCameraFrame empty = new NativeCameraFrame(null) {

			private Mat empty;

			public Mat rgba() {
				return getEmpty();
			}

			public Mat gray() {
				return getEmpty();
			}

			private Mat getEmpty() {
				if (empty == null) {
					empty = new Mat(1, 1, CvType.CV_8UC1);
				}
				return empty;
			}
		};

	}

	public int getHeight() {
		// retorna invertido INTENCIONALMENTE, pois a imagem est� sendo
		// capturada invertida e ap�s rodar temos a invers�o de width e height
		// TODO arrumar isso
		return width;
	}

	public int getWidth() {
		// retorna invertido INTENCIONALMENTE, pois a imagem est� sendo
		// capturada invertida e ap�s rodar temos a invers�o de width e height
		// TODO arrumar isso
		return height;
	}

}
//package org.vvgaming.harmegido.vision;
//
//import org.opencv.highgui.Highgui;
//import org.opencv.highgui.VideoCapture;
//
///**
// * Wrapper para captura de frames da camera. Essa classe é capaz de rodar uma
// * Thread e ficar capturando frames e deixando-os disponíveis quando necessários
// * 
// * @author Vinicius Nogueira
// */
//public class OldCamThreaded {
//
//	private VideoCapture mCamera;
//	
//	private NativeCameraFrame lastFrame = NativeCameraFrame.empty;
//
//	private boolean running = false;
//	private boolean stopGrabbing;
//	private Thread grabbingThread;
//	
//	private int width;
//	private int height;
//	
//	private OnFrameListener onFrameListener;
//
//	/**
//	 * Conecta à camera nativa
//	 * 
//	 * @param width
//	 *            largura da resolução
//	 * @param height
//	 *            altura da resolução
//	 * @param onFrameListener
//	 *            listener para ser notificado a cada frame coletado
//	 * 
//	 * @return <code>true</code> se houve sucesso na conexão
//	 */
//	public boolean connectCamera(int width, int height,
//			OnFrameListener onFrameListener) {
//
//		this.onFrameListener = onFrameListener;
//
//		this.width = width;
//		this.height = height;
//
//		if (!initializeCamera())
//			return false;
//
//		grabbingThread = new Thread(new CameraWorker());
//		grabbingThread.start();
//
//		return true;
//	}
//
//	/**
//	 * Overload de {@link OldCamThreaded#connectCamera(int, int, OnFrameListener)} sem
//	 * listener
//	 */
//	public boolean connectCamera(int width, int height) {
//		return connectCamera(width, height, null);
//	}
//
//	/**
//	 * Libera a camera conectada
//	 */
//	public void disconnectCamera() {
//		if (grabbingThread != null) {
//			try {
//				stopGrabbing = true;
//				grabbingThread.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} finally {
//				grabbingThread = null;
//				stopGrabbing = false;
//			}
//		}
//		releaseCamera();
//	}
//
//	private boolean initializeCamera() {
//		synchronized (this) {
//
//			mCamera = new VideoCapture(Highgui.CV_CAP_ANDROID);
//
//			if (mCamera == null)
//				return false;
//
//			if (mCamera.isOpened() == false)
//				return false;
//
//			// java.util.List<Size> sizes = mCamera.getSupportedPreviewSizes();
//			//
//			// Size frameSize = calculateCameraFrameSize(sizes,
//			// new OpenCvSizeAccessor(), width, height);
//			//
//			// mFrameWidth = (int) frameSize.width;
//			// mFrameHeight = (int) frameSize.height;
//			//
//			// if ((getLayoutParams().width == LayoutParams.MATCH_PARENT)
//			// && (getLayoutParams().height == LayoutParams.MATCH_PARENT))
//			// mScale = Math.min(((float) height) / mFrameHeight,
//			// ((float) width) / mFrameWidth);
//			// else
//			// mScale = 0;
//			//
//			// mCamera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, frameSize.width);
//			// mCamera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, frameSize.height);
//
//			mCamera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, width);
//			mCamera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, height);
//		}
//
//		return true;
//	}
//
//	private void releaseCamera() {
//		synchronized (this) {
//			if (mCamera != null) {
//				mCamera.release();
//			}
//		}
//	}
//
//	private class CameraWorker implements Runnable {
//
//		public void run() {
//			running = true;
//			do {
//				if (!mCamera.grab()) {
//					break;
//				}
//				lastFrame = (new NativeCameraFrame(mCamera));
//
//				// após capturado o frame, chama o listener
//				if (onFrameListener != null) {
//					onFrameListener.onFrame(lastFrame);
//				}
//
//				try {
//					// TODO esse sleep é só uma gambiarra para diminuir o
//					// consumo, dado que a camera só pega em no máximo 30 fps,
//					// não adianta ficar indo tão rápido. O ideal é ajustar isso
//					// aqui depois para ficar mais inteligente
//					Thread.sleep(20);
//				} catch (InterruptedException ignored) {
//				}
//			} while (!stopGrabbing);
//			running = false;
//		}
//	}
//
//	public NativeCameraFrame getLastFrame() {
//		return lastFrame;
//	}
//
//	public int getHeight() {
//		// retorna invertido INTENCIONALMENTE, pois a imagem está sendo
//		// capturada invertida e após rodar temos a inversão de width e height
//		// TODO arrumar isso
//		return width;
//	}
//
//	public int getWidth() {
//		// retorna invertido INTENCIONALMENTE, pois a imagem está sendo
//		// capturada invertida e após rodar temos a inversão de width e height
//		// TODO arrumar isso
//		return height;
//	}
//
//	public boolean isRunning() {
//		return running;
//	}
//
//}

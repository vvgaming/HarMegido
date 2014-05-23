package org.vvgaming.harmegido.vision;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class Cam {

	private boolean mStopThread;
	private Thread mThread;

	private VideoCapture mCamera;

	public boolean connectCamera(int width, int height) {

		if (!initializeCamera(width, height))
			return false;

		mThread = new Thread(new CameraWorker());
		mThread.start();

		return true;
	}

	public void disconnectCamera() {
		if (mThread != null) {
			try {
				mStopThread = true;
				mThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				mThread = null;
				mStopThread = false;
			}
		}
		releaseCamera();
	}

	private boolean initializeCamera(int width, int height) {
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
			} while (!mStopThread);
		}
	}

	public NativeCameraFrame getLastFrame() {
		return lastFrame;
	}

	public static class NativeCameraFrame implements CvCameraViewFrame {

		@Override
		public Mat rgba() {
			mCapture.retrieve(mRgba, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA);
			return mRgba;
		}

		@Override
		public Mat gray() {
			mCapture.retrieve(mGray, Highgui.CV_CAP_ANDROID_GREY_FRAME);
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

	};

}

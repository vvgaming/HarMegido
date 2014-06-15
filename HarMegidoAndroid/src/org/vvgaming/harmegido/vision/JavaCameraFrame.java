package org.vvgaming.harmegido.vision;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class JavaCameraFrame implements CvCameraViewFrame {

	private Mat mYuvFrameData;
	private Mat mRgba;
	private int mWidth;
	private int mHeight;

	public JavaCameraFrame(Mat Yuv420sp, int width, int height) {
		super();
		mWidth = width;
		mHeight = height;
		mYuvFrameData = Yuv420sp;
		mRgba = new Mat();
	}

	public Mat gray() {
		final Mat retorno = mYuvFrameData.submat(0, mHeight, 0, mWidth);
		// não sei pq estão sendo capturadas giradas
		Core.flip(retorno.t(), retorno, 1);
		return retorno;
	}

	public Mat rgba() {
		Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2BGR_NV12, 4);
		// não sei pq estão sendo capturadas giradas
		Core.flip(mRgba.t(), mRgba, 1);
		return mRgba;
	}

	public void release() {
		mRgba.release();
	}

	public static JavaCameraFrame empty = new JavaCameraFrame(null, 1, 1) {

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

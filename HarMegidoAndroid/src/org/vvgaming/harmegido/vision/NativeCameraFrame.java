package org.vvgaming.harmegido.vision;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class NativeCameraFrame implements CvCameraViewFrame {

	@Override
	public Mat rgba() {
		mCapture.retrieve(mRgba, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA);
		// não sei pq estão sendo capturadas giradas
		Core.flip(mRgba.t(), mRgba, 1);
		return mRgba;
	}

	@Override
	public Mat gray() {
		mCapture.retrieve(mGray, Highgui.CV_CAP_ANDROID_GREY_FRAME);
		// não sei pq estão sendo capturadas giradas
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
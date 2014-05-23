package org.vvgaming.harmegido.bubbleTest;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.vvgaming.harmegido.gameEngine.GameCanvas;

import android.app.Activity;
import android.os.Bundle;

public class BubbleTestActivity extends Activity {

	private GameCanvas gameCanvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameCanvas = new GameCanvas(this, new BubbleGame());
		gameCanvas.setShowFps(true);
		setContentView(gameCanvas);
	}

	@Override
	protected void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this,
				mLoaderCallback);
	}

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}

	};

}

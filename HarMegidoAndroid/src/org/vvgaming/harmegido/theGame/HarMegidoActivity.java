package org.vvgaming.harmegido.theGame;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.vvgaming.harmegido.gameEngine.GameCanvas;

import android.app.Activity;
import android.os.Bundle;

public class HarMegidoActivity extends Activity {

	private GameCanvas gameCanvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
				gameCanvas = new GameCanvas(HarMegidoActivity.this,
						new HarMegidoGame());
				gameCanvas.setShowFps(true);
				setContentView(gameCanvas);
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

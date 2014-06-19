package org.vvgaming.harmegido.theGame;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.vvgaming.harmegido.gameEngine.GameCanvas;
import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.theGame.scenes.MenuScene;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class HarMegidoActivity extends Activity {

	public static HarMegidoActivity activity;

	private GameCanvas gameCanvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		activity = this;
	}

	@Override
	protected void onResume() {
		super.onResume();

		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this,
				callback);
	}

	private BaseLoaderCallback callback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				RootNode.create(HarMegidoActivity.this, new MenuScene());
				gameCanvas = new GameCanvas(HarMegidoActivity.this,
						RootNode.getInstance());
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

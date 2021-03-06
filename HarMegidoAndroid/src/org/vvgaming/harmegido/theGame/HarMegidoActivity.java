package org.vvgaming.harmegido.theGame;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.unbiquitous.uos.core.ClassLoaderUtils;
import org.vvgaming.harmegido.gameEngine.GameCanvas;
import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.theGame.mainNodes.N1Loading;
import org.vvgaming.harmegido.uos.UOSFacade;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class HarMegidoActivity extends Activity {

	private GameCanvas gameCanvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		ClassLoaderUtils.builder = new ClassLoaderUtils.DefaultClassLoaderBuilder() {
			public ClassLoader getParentClassLoader() {
				return getClassLoader();
			};
		};

	}

	@Override
	protected void onResume() {
		super.onResume();

		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this,
				callback);
	}

	private BaseLoaderCallback callback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				RootNode.create(HarMegidoActivity.this, new N1Loading());

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UOSFacade.stopUos();
	}

}

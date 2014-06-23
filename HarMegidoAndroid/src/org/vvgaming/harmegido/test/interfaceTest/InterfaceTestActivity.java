package org.vvgaming.harmegido.test.interfaceTest;

import org.vvgaming.harmegido.gameEngine.GameCanvas;
import org.vvgaming.harmegido.gameEngine.RootNode;

import android.app.Activity;
import android.os.Bundle;

public class InterfaceTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RootNode.create(InterfaceTestActivity.this, new NInterfaceCharTest());
		GameCanvas gameCanvas = new GameCanvas(InterfaceTestActivity.this,
				RootNode.getInstance());
		gameCanvas.setShowFps(true);
		setContentView(gameCanvas);
	}

}

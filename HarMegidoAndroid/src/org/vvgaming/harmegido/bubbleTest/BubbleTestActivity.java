package org.vvgaming.harmegido.bubbleTest;

import org.vvgaming.harmegido.gameEngine.GameCanvas;

import android.app.Activity;
import android.os.Bundle;

public class BubbleTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameCanvas gameCanvas = new GameCanvas(this, new BubbleGame());
		gameCanvas.setShowFps(true);
		setContentView(gameCanvas);
	}

}

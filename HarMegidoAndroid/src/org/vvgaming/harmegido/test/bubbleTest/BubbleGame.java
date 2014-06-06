package org.vvgaming.harmegido.test.bubbleTest;

import java.util.ArrayList;
import java.util.List;

import org.vvgaming.harmegido.gameEngine.AbstractGame;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.content.res.Resources;
import android.view.MotionEvent;

public class BubbleGame extends AbstractGame {

	public BubbleGame(Resources res) {
		super(res);
	}

	private List<RandomBubble> bubbles = new ArrayList<>();
	// private final SquareButton btn = new SquareButton(0, 100, 200, 120,
	// "Testar ObjDetect");
	private final Text texto = new Text(100, 10, "");
	private final OCVCamObjTest cam = new OCVCamObjTest(new Ponto(0, 0));

	@Override
	public void init() {
		super.init();
		cam.setCenter(new Ponto(getWidth() / 2, getHeight() / 2));
		addObject(texto, 100);
		addObject(cam);
	}

	@Override
	public void update(long delta) {
		texto.text = "bolhas: " + bubbles.size();
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			// if (btn.ret.contem(new Vetor2d(event.getX(), event.getY()))) {
			// Intent i = new Intent(
			// GameTestActivity.this.getApplicationContext(),
			// ObjDetectTestActivity.class);
			// startActivity(i);
			// } else {
			RandomBubble bubble = new RandomBubble(getWidth(), getHeight());
			addObject(bubble);
			bubbles.add(bubble);
			// }

		}
	}

}

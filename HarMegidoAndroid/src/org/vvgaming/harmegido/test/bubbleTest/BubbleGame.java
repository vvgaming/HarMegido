package org.vvgaming.harmegido.test.bubbleTest;

import java.util.ArrayList;
import java.util.List;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.gos.TextGO;

import android.view.MotionEvent;

public class BubbleGame extends GameNode {

	private List<RandomBubble> bubbles = new ArrayList<>();

	private final TextGO texto = new TextGO(100, 10, "");

	@Override
	public void init() {
		super.init();
		addSubNode(texto, 100);
	}

	@Override
	public void update(long delta) {
		texto.text = "bolhas: " + bubbles.size();
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			RandomBubble bubble = new RandomBubble(getGameWidth(),
					getGameHeight());
			addSubNode(bubble);
			bubbles.add(bubble);

		}
	}

}

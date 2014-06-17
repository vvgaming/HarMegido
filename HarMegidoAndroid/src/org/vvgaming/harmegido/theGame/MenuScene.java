package org.vvgaming.harmegido.theGame;

import org.vvgaming.harmegido.gameEngine.AbstractGameScene;

import android.content.res.Resources;
import android.view.MotionEvent;

public class MenuScene extends AbstractGameScene {

	private long acc;

	public MenuScene(Resources res) {
		super(res);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void update(long delta) {
		System.out.println("waitttinngggg");
		acc += delta;
		if (acc > 10000) {
			HarMegidoActivity.activity.trocarCena(new MainScene(getRes()));
		}
	}

	@Override
	public void onTouch(MotionEvent event) {
	}

}

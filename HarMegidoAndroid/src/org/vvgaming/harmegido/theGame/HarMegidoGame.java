package org.vvgaming.harmegido.theGame;

import org.vvgaming.harmegido.gameEngine.AbstractGame;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.theGame.gos.CamGO;
import org.vvgaming.harmegido.theGame.gos.SimpleBoxGO;

import android.view.MotionEvent;

public class HarMegidoGame extends AbstractGame {

	private CamGO cam;
	private SimpleBoxGO bg;
	private SimpleBoxGO redBox;
	private SimpleBoxGO greenBox;

	// private SimpleBoxGO btn1;
	// private SimpleBoxGO btn2;
	// private SimpleBoxGO btn3;

	@Override
	public void init() {
		super.init();
		cam = new CamGO();
		cam.setCenter(new Ponto(getWidth() / 2, getHeight() / 3));

		bg = new SimpleBoxGO(0, 0, getWidth(), getHeight(), 130, 0, 0);
		redBox = new SimpleBoxGO(0, getHeight() - 300, getWidth(), 300, 255, 0, 0, 100);
		redBox.setVisible(false);
		greenBox = new SimpleBoxGO(0, getHeight() - 300, getWidth(), 300, 0, 255, 0, 100);
		greenBox.setVisible(false);

		addObject(bg, 0);
		addObject(cam, 1);
		addObject(redBox, 2);
		addObject(greenBox, 2);

	}

	@Override
	public void update(long delta) {
		if (cam.isResultado()) {
			greenBox.setVisible(true);
			redBox.setVisible(false);
		} else {
			greenBox.setVisible(false);
			redBox.setVisible(true);
		}
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			cam.onClick();
		}
	}

}

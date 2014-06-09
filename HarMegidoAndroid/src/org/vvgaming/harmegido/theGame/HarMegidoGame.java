package org.vvgaming.harmegido.theGame;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.AbstractGame;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.geometry.Retangulo;
import org.vvgaming.harmegido.theGame.gos.CamGO;
import org.vvgaming.harmegido.theGame.gos.ImageGO;
import org.vvgaming.harmegido.theGame.gos.SimpleBoxGO;

import android.content.res.Resources;
import android.view.MotionEvent;

public class HarMegidoGame extends AbstractGame {

	public HarMegidoGame(Resources res) {
		super(res);
	}

	private CamGO cam;
	private SimpleBoxGO bg;
	private SimpleBoxGO resultBox;

	private ImageGO compass;

	@Override
	public void init() {
		super.init();
		cam = new CamGO();
		cam.setCenter(new Ponto(getWidth() / 2, getHeight() / 3));

		bg = new SimpleBoxGO(0, 0, getWidth(), getHeight(), 130, 0, 0);
		resultBox = new SimpleBoxGO(0, getHeight() - 300, getWidth(), 300, 255,
				0, 0, 100);
		resultBox.setVisible(true);

		compass = new ImageGO(new Ponto(getWidth() / 2, getHeight() - 200),
				getRes(), R.drawable.compass);
		compass.setScale(0.5f);

		addObject(bg, 0);
		addObject(cam, 1);
		addObject(resultBox, 2);
		addObject(compass, 3);
	}

	@Override
	public void update(long delta) {
		if (cam.isOkResultado()) {
			resultBox.setColor(0, 255, 0);
		} else {
			resultBox.setColor(255, 0, 0);
		}

		float comparacao = (float) cam.getComparacao();
		if (comparacao > 250) {
			comparacao = 180;
		} else {
			comparacao = (180.0f * comparacao) / 250.0f;
		}
		compass.setRotation(comparacao+180);
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			cam.onClick();
		}
	}

}

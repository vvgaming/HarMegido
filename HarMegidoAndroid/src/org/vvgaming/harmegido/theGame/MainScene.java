package org.vvgaming.harmegido.theGame;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.AbstractGameScene;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.theGame.gos.ImageGO;
import org.vvgaming.harmegido.theGame.gos.SimilarityCamGO;
import org.vvgaming.harmegido.theGame.gos.SimpleBoxGO;

import android.content.res.Resources;
import android.view.MotionEvent;

public class MainScene extends AbstractGameScene {

	public MainScene(Resources res) {
		super(res);
	}

	private SimilarityCamGO cam;
	private SimpleBoxGO bg;
	private SimpleBoxGO resultBox;

	private ImageGO compass;

	private ImageGO bgImg;

	@Override
	public void init() {
		super.init();
		cam = new SimilarityCamGO(new Ponto(getWidth() / 2, getHeight() / 3),
				getWidth() * .75f, getHeight() * .7f);

		bg = new SimpleBoxGO(0, 0, getWidth(), getHeight(), 0, 0, 0);
		resultBox = new SimpleBoxGO(0, 0, getWidth(), getHeight(), 100, 0, 0,
				100);
		resultBox.setVisible(true);

		compass = new ImageGO(new Ponto(getWidth() / 2, getHeight() - 200),
				getRes(), R.drawable.compass);
		compass.setScale(0.3f);

		bgImg = new ImageGO(new Ponto(getWidth() / 2, getHeight() / 2),
				getRes(), R.drawable.bg);

		addObject(bg, 0);
		addObject(cam, 1);
		addObject(resultBox, 2);
		addObject(bgImg, 3);
		addObject(compass, 4);
	}

	@Override
	public void update(long delta) {
		if (cam.isOkResultado()) {
			resultBox.setColor(30, 0, 255, 0);
		} else {
			resultBox.setColor(30, 255, 0, 0);
		}

		float comparacao = (float) cam.getComparacao();
		if (comparacao > 250) {
			comparacao = 180;
		} else {
			comparacao = (180.0f * comparacao) / 250.0f;
		}
		compass.setRotation(comparacao + 180);
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			cam.onClick();
		}
	}

}

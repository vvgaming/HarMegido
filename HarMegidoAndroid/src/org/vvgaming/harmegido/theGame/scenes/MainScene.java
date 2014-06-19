package org.vvgaming.harmegido.theGame.scenes;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.AbstractGameScene;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.gos.ImageGO;
import org.vvgaming.harmegido.gameEngine.gos.TextGO;
import org.vvgaming.harmegido.theGame.gos.SimilarityCamGO;
import org.vvgaming.harmegido.theGame.gos.SimpleBoxGO;

import android.app.Activity;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;

public class MainScene extends AbstractGameScene {

	public MainScene(Activity act) {
		super(act);
	}

	private SimilarityCamGO cam;
	private SimpleBoxGO bg;
	private SimpleBoxGO resultBox;

	private ImageGO bgImg;

	@Override
	public void init() {
		super.init();
		cam = new SimilarityCamGO(new Ponto(getWidth() / 2,
				(getHeight() * 2) / 5), getWidth() * .75f);

		bg = new SimpleBoxGO(0, 0, getWidth(), getHeight(), 0, 0, 0);
		resultBox = new SimpleBoxGO(0, 0, getWidth(), getHeight(), 100, 0, 0,
				100);
		resultBox.setVisible(true);

		bgImg = new ImageGO(new Ponto(getWidth() / 2, getHeight() / 2),
				getAssetManager().getBitmap(R.drawable.bg));
		bgImg.setHeightKeepingRatio(getHeight());

		TextGO text = new TextGO(getWidth() / 2, getHeight() / 10, "Har Megido");
		text.face = Typeface.createFromAsset(getAssetManager()
				.getAndroidAssets(), "fonts/dc_o.ttf");
		text.paint.setTextAlign(Align.CENTER);
		text.size = 140;

		addObject(bg, 0);
		addObject(cam, 1);
		addObject(resultBox, 2);
		addObject(bgImg, 3);
		addObject(text, 4);

	}

	@Override
	public void update(long delta) {
		if (cam.isOkResultado()) {
			resultBox.setColor(100, 0, 255, 0);
		} else {
			resultBox.setColor(100, 255, 0, 0);
		}
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			cam.onClick();
		}
	}

}

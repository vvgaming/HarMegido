package org.vvgaming.harmegido.theGame.scenes;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.gos.ImageGO;
import org.vvgaming.harmegido.gameEngine.gos.TextGO;
import org.vvgaming.harmegido.theGame.gos.SimilarityCamGO;
import org.vvgaming.harmegido.theGame.gos.SimpleBoxGO;

import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;

public class MainScene extends GameNode {

	private SimilarityCamGO cam;
	private SimpleBoxGO bg;
	private SimpleBoxGO resultBox;

	private ImageGO bgImg;

	@Override
	public void init() {
		super.init();
		cam = new SimilarityCamGO(new Ponto(getGameWidth() / 2,
				(getGameHeight() * 2) / 5), getGameWidth() * .75f);

		bg = new SimpleBoxGO(0, 0, getGameWidth(), getGameHeight(), 0, 0, 0);
		resultBox = new SimpleBoxGO(0, 0, getGameWidth(), getGameHeight(), 100,
				0, 0, 100);

		bgImg = new ImageGO(new Ponto(getGameWidth() / 2, getGameHeight() / 2),
				getGameAssetManager().getBitmap(R.drawable.bg));
		bgImg.setHeightKeepingRatio(getGameHeight());

		TextGO text = new TextGO(getGameWidth() / 2, getGameHeight() / 10,
				"Har Megido");
		text.face = Typeface.createFromAsset(getGameAssetManager()
				.getAndroidAssets(), "fonts/dc_o.ttf");
		text.paint.setTextAlign(Align.CENTER);
		text.size = 140;

		addSubNode(bg, 0);
		addSubNode(cam, 1);
		addSubNode(resultBox, 2);
		addSubNode(bgImg, 3);
		addSubNode(text, 4);

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

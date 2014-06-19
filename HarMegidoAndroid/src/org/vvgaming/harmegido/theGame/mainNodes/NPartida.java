package org.vvgaming.harmegido.theGame.mainNodes;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.theGame.objNodes.NSimilarityCam;
import org.vvgaming.harmegido.theGame.objNodes.NSimpleBox;

import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;

public class NPartida extends GameNode {

	private NSimilarityCam cam;
	private NSimpleBox bg;
	private NSimpleBox resultBox;

	private NImage bgImg;

	@Override
	public void init() {
		super.init();
		cam = new NSimilarityCam(new Ponto(getGameWidth() / 2,
				(getGameHeight() * 2) / 5), getGameWidth() * .75f);

		bg = new NSimpleBox(0, 0, getGameWidth(), getGameHeight(), 0, 0, 0);
		resultBox = new NSimpleBox(0, 0, getGameWidth(), getGameHeight(), 100,
				0, 0, 100);

		bgImg = new NImage(new Ponto(getGameWidth() / 2, getGameHeight() / 2),
				getGameAssetManager().getBitmap(R.drawable.bg));
		bgImg.setHeightKeepingRatio(getGameHeight());

		NText text = new NText(getGameWidth() / 2, getGameHeight() / 10,
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

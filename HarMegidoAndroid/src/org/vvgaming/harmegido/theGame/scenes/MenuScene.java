package org.vvgaming.harmegido.theGame.scenes;

import org.vvgaming.harmegido.gameEngine.AbstractGameScene;
import org.vvgaming.harmegido.gameEngine.gos.Text;
import org.vvgaming.harmegido.theGame.HarMegidoActivity;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.view.MotionEvent;

public class MenuScene extends AbstractGameScene {

	private long timerPisca;
	private Text text;

	public MenuScene(Activity act) {
		super(act);
	}

	@Override
	public void init() {
		super.init();

		text = new Text(getWidth() / 2, getHeight() / 2,
				"Toque na tela para iniciar");
		text.face = Typeface.createFromAsset(getAssetManager()
				.getAndroidAssets(), "fonts/Radio Trust.ttf");
		text.paint.setTextAlign(Align.CENTER);
		text.size = 70;

		addObject(text);

	}

	@Override
	public void update(long delta) {
		timerPisca += delta;
		if (timerPisca > 500) {
			text.setVisible(!text.isVisible());
			timerPisca = 0;
		}
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			HarMegidoActivity.activity.trocarCena(new MainScene(
					getAssetManager().getActivity()));
		}
	}

}

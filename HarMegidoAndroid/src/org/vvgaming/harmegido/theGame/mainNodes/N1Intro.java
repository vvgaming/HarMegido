package org.vvgaming.harmegido.theGame.mainNodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.NText.VerticalAlign;
import org.vvgaming.harmegido.gameEngine.nodes.NTextBlinking;

import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;

public class N1Intro extends GameNode {

	@Override
	public void init() {
		super.init();

		final String texto = "\"Eles então se congregaram num lugar\n"
				+ "que em hebreu chama-se Har Megido.\"\nApocalise 16:13-16\n\n\n"
				+ "A batalha entre o bem \n" + "e o mal está se espalhando \n"
				+ "pelo planeta, e é hora de\n "
				+ "você escolher por qual lado lutar";
		NText orientacao = new NText((int) getGameWidth(.5f),
				(int) getGameHeight(.5f), texto);
		final Typeface font = Typeface.createFromAsset(getGameAssetManager()
				.getAndroidAssets(), "fonts/Radio Trust.ttf");
		orientacao.face = font;
		orientacao.paint.setTextAlign(Align.CENTER);
		orientacao.vAlign = NText.VerticalAlign.MIDDLE;
		orientacao.size = 50;

		String toqParaComecarStr = "Toque na tela para começar...";
		NTextBlinking toqueParaComeçar = new NTextBlinking(
				(int) getGameWidth(.5f), (int) getGameHeight(.9f),
				toqParaComecarStr);
		toqueParaComeçar.face = font;
		toqueParaComeçar.paint.setTextAlign(Align.CENTER);
		toqueParaComeçar.vAlign = NText.VerticalAlign.MIDDLE;
		toqueParaComeçar.size = 60;

		NText title = new NText((int) getGameWidth(.5f),
				(int) getGameHeight(.1f), "Har Megido");
		title.face = Typeface.createFromAsset(getGameAssetManager()
				.getAndroidAssets(), "fonts/dc_o.ttf");
		title.paint.setTextAlign(Align.CENTER);
		title.size = 140;
		title.vAlign = VerticalAlign.TOP;

		addSubNode(orientacao);
		addSubNode(toqueParaComeçar);
		addSubNode(title);

	}

	@Override
	public void update(long delta) {
	}

	@Override
	public boolean onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			RootNode.getInstance().changeMainNode(new N2Menu());
			return true;
		}
		return false;
	}

}

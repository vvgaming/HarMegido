package org.vvgaming.harmegido.theGame.mainNodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.nodes.NText;

import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;

public class N1Intro extends GameNode {

	private NText orientacao;

	@Override
	public void init() {
		super.init();

		final String texto = "Vamos preparar o ambiente para você.\n"
				+ "Enquanto isso, escolha suas preferências\n\n\n"
				+ "Toque na tela para continuar...";
		orientacao = new NText(getGameWidth() / 2, getGameHeight() / 2, texto);
		orientacao.face = Typeface.createFromAsset(getGameAssetManager()
				.getAndroidAssets(), "fonts/Radio Trust.ttf");
		orientacao.paint.setTextAlign(Align.CENTER);
		orientacao.vAlign = NText.VerticalAlign.MIDDLE;
		orientacao.size = 50;

		addSubNode(orientacao);

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

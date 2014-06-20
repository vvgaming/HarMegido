package org.vvgaming.harmegido.theGame.mainNodes;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.NText.VerticalAlign;
import org.vvgaming.harmegido.gameEngine.nodes.NTextBlinking;

import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;

/**
 * Tela simples de introdução com uma pequena mensagem para o usuário clicar e seguir em frente
 * 
 * @author Vinicius Nogueira
 */
public class N2Intro extends GameNode
{

	@Override
	public void init()
	{
		super.init();

		final String texto = "\"Eles então se congregaram num lugar\n" + "que em hebreu chama-se Har Megido.\"\nApocalise 16,16\n\n\n"
				+ "A batalha entre o bem \n" + "e o mal está se espalhando \n" + "pelo planeta, e é hora de\n "
				+ "você escolher por qual lado lutar";
		NText orientacao = new NText((int) getGameWidth(.5f), (int) getGameHeight(.5f), texto);
		final Typeface font = Typeface.createFromAsset(getGameAssetManager().getAndroidAssets(), "fonts/Radio Trust.ttf");
		orientacao.face = font;
		orientacao.paint.setTextAlign(Align.CENTER);
		orientacao.vAlign = NText.VerticalAlign.MIDDLE;
		orientacao.size = 50;

		String toqParaComecarStr = "Toque na tela para começar...";
		NTextBlinking toqueParaComeçar = new NTextBlinking((int) getGameWidth(.5f), (int) getGameHeight(.9f), toqParaComecarStr);
		toqueParaComeçar.face = font;
		toqueParaComeçar.paint.setTextAlign(Align.CENTER);
		toqueParaComeçar.vAlign = NText.VerticalAlign.MIDDLE;
		toqueParaComeçar.size = 60;

		NText title = new NText((int) getGameWidth(.5f), (int) getGameHeight(.1f), "Har Megido");
		title.face = Typeface.createFromAsset(getGameAssetManager().getAndroidAssets(), "fonts/dc_o.ttf");
		title.paint.setTextAlign(Align.CENTER);
		title.size = 140;
		title.vAlign = VerticalAlign.TOP;

		NImage bgImg = new NImage(new Ponto(getGameWidth(.5f), getGameHeight(.5f)), getGameAssetManager().getBitmap(R.drawable.bg_intro));
		bgImg.setHeightKeepingRatio(getGameHeight());

		addSubNode(bgImg, 0);
		addSubNode(orientacao, 1);
		addSubNode(toqueParaComeçar, 1);
		addSubNode(title, 1);

	}

	@Override
	public void update(long delta)
	{
	}

	@Override
	public boolean onTouch(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			RootNode.getInstance().changeMainNode(new N3Menu());
			return true;
		}
		return false;
	}

}

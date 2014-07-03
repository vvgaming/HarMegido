package org.vvgaming.harmegido.theGame.mainNodes;

import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.NText.VerticalAlign;
import org.vvgaming.harmegido.gameEngine.nodes.fx.NBlinker;
import org.vvgaming.harmegido.theGame.objNodes.NHMBackground;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;

import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;

/**
 * Tela simples de introdução com uma pequena mensagem para o usuário clicar e seguir em frente
 * 
 * @author Vinicius Nogueira
 */
public class N2Intro extends NHMMainNode
{

	@Override
	public void init()
	{
		super.init();

		final String texto = "\"Eles então se congregaram num lugar\n" + "que em hebreu chama-se Har Megido.\"\nApocalise 16,16\n\n\n"
				+ "A batalha entre o bem \n" + "e o mal está se espalhando \n" + "pelo planeta, e é hora de\n "
				+ "você escolher por qual lado lutar";
		NText orientacao = new NText((int) getGameWidth(.5f), (int) getGameHeight(.5f), texto);
		orientacao.face = getDefaultFace();
		orientacao.paint.setTextAlign(Align.CENTER);
		orientacao.vAlign = NText.VerticalAlign.MIDDLE;

		String toqParaComecarStr = "Toque na tela para começar...";
		NText toqueParaComecarText = new NText((int) getGameWidth(.5f), (int) getGameHeight(.9f), toqParaComecarStr);
		toqueParaComecarText.face = getDefaultFace();
		toqueParaComecarText.paint.setTextAlign(Align.CENTER);
		toqueParaComecarText.vAlign = NText.VerticalAlign.MIDDLE;
		NBlinker blinkingFxToqueParaComecar = new NBlinker(toqueParaComecarText);

		NText title = new NText((int) getGameWidth(.5f), (int) getGameHeight(.1f), "Har Megido");
		title.face = Typeface.createFromAsset(getGameAssetManager().getAndroidAssets(), "fonts/dc_o.ttf");
		title.paint.setTextAlign(Align.CENTER);
		title.setSize(getBigFontSize());
		title.vAlign = VerticalAlign.BOTTOM;

		addSubNode(blinkingFxToqueParaComecar);
		
		addSubNode(new NHMBackground(), 0);
		addSubNode(orientacao, 1);
		addSubNode(toqueParaComecarText, 1);
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
			RootNode.getInstance().changeMainNode(new N3SelecaoPartida());
			return true;
		}
		return false;
	}

}

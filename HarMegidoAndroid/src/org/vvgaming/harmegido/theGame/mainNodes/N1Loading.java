package org.vvgaming.harmegido.theGame.mainNodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.uos.UOSFacade;

import android.graphics.Paint.Align;
import android.graphics.Typeface;

public class N1Loading extends GameNode
{

	private NText texto;
	private boolean uosOk = false;
	private boolean facadeOk = false;

	@Override
	public void init()
	{
		super.init();

		final String textoStr = getLoadingText();

		texto = new NText((int) getGameWidth(.5f), (int) getGameHeight(.5f), textoStr);
		final Typeface font = Typeface.createFromAsset(getGameAssetManager().getAndroidAssets(), "fonts/Radio Trust.ttf");
		texto.face = font;
		texto.paint.setTextAlign(Align.CENTER);
		texto.vAlign = NText.VerticalAlign.MIDDLE;
		texto.size = 50;

		addSubNode(texto);

		UOSFacade.startUos();

	}

	@Override
	public void update(long delta)
	{

		if (UOSFacade.isUosStarted())
		{
			uosOk = true;
			UOSFacade.createFacadeAsync();
		}

		if (UOSFacade.isDriverFacadeCreated())
		{
			facadeOk = true;
		}

		if (facadeOk && uosOk)
		{
			RootNode.getInstance().changeMainNode(new N2Intro());
		}

		texto.text = getLoadingText();
	}

	private String getLoadingText()
	{
		final String texto = (uosOk ? "[uOS preparado]" : "...Preparando uOS...") + "\n\n"
				+ (facadeOk ? "[Servidor localizado]" : "...Procurando o Servidor...");
		return texto;
	}
}

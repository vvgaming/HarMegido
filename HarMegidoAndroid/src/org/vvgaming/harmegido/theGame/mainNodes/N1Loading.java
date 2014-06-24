package org.vvgaming.harmegido.theGame.mainNodes;

import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.util.NTimer;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;
import org.vvgaming.harmegido.uos.UOSFacade;

import android.graphics.Paint.Align;

import com.github.detentor.codex.function.Function0;

/**
 * Tela de carregamento no início do jogo, para garantir que não entra no jogo antes de carregar o uOS
 * 
 * @author Vinicius Nogueira
 */
public class N1Loading extends NHMMainNode
{

	private NText texto;
	private boolean uosOk = false;
	private boolean facadeOk = false;
	private boolean ended = false;

	@Override
	public void init()
	{
		super.init();

		final String textoStr = getLoadingText();

		texto = new NText((int) getGameWidth(.5f), (int) getGameHeight(.5f), textoStr);
		texto.face = getDefaultFace();
		texto.paint.setTextAlign(Align.CENTER);
		texto.vAlign = NText.VerticalAlign.MIDDLE;

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

		if (facadeOk && uosOk && !ended)
		{
			ended = true;
			addSubNode(new NTimer(1000, new Function0<Void>()
			{

				@Override
				public Void apply()
				{
					RootNode.getInstance().changeMainNode(new N2Intro());
					return null;
				}
			}, true));

		}

		texto.text = getLoadingText();
	}

	private String getLoadingText()
	{
		final String texto = (uosOk ? "[Encantamentos preparados]" : "...Preparando encantamentos...") + "\n\n"
				+ (facadeOk ? "[Har Megido localizado]" : "...Procurando o campo de batalha...");
		return texto;
	}
}

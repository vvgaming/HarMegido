package org.vvgaming.harmegido.theGame.objNodes;

import org.vvgaming.harmegido.gameEngine.GameNode;

import android.graphics.Typeface;

/**
 * Implementação de um GameNode padrão para o Harmegido para conferir funcionalidades específicas do Harmegido, como por exemplo as
 * mensagens de console do tipo {@link NHMConsoleText}
 * 
 * @author Vinicius Nogueira
 */
public class NHMMainNode extends GameNode
{

	private static final long DEFAULT_CONSOLE_TIMER = 2000;
	private Typeface defaultFace;

	private NHMConsoleText lastText;

	@Override
	protected void update(long delta)
	{

	}

	protected void sendConsoleMsg(final String text, final long timer)
	{
		if (lastText != null)
		{
			lastText.kill();
		}
		// guarda sempre a referencia a antiga para matá-la antes de uma nova
		lastText = new NHMConsoleText(text, timer);
		addSubNode(lastText);
	}

	protected void sendConsoleMsg(final String text)
	{
		sendConsoleMsg(text, DEFAULT_CONSOLE_TIMER);
	}

	protected Typeface getDefaultFace()
	{
		if (defaultFace == null)
		{
			defaultFace = Typeface.createFromAsset(getGameAssetManager().getAndroidAssets(), "fonts/Radio Trust.ttf");
		}
		return defaultFace;
	}

}

package org.vvgaming.harmegido.theGame.objNodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.geometry.Retangulo;

import android.graphics.Canvas;
import android.graphics.Paint;

public class NProgressBar extends GameNode
{

	private final Paint color = new Paint();
	private final Retangulo ret;
	private Retangulo retToDraw;

	private boolean larguraProgress = true;
	private boolean alturaProgress = false;

	private float progress = 0f;

	public NProgressBar(final float x, final float y, final float w, final float h)
	{
		ret = new Retangulo(new Ponto(x, y), w, h);
	}

	@Override
	public void update(final long delta)
	{
		retToDraw = new Retangulo(ret.origem, larguraProgress ? ret.width * progress : ret.width, alturaProgress ? ret.height * progress
				: ret.height);
	}

	@Override
	public void render(final Canvas canvas)
	{
		canvas.drawRect(retToDraw.toAndroidCanvasRect(), color);
	}

	public void setProgress(final float percentProgress)
	{
		progress = percentProgress;
		if (progress > 1)
		{
			progress = 1;
		}
		else if (progress < 0)
		{
			progress = 0;
		}
	}

	public float getProgress()
	{
		return progress;
	}

	public void reset()
	{
		setProgress(0);
	}

	public boolean isFull()
	{
		return progress >= 1;
	}

	public boolean isLarguraProgress()
	{
		return larguraProgress;
	}

	/**
	 * indicador se o a barra de progresso cresce em largura (por padrão sim!)
	 * 
	 * @param larguraProgress
	 */
	public void setLarguraProgress(final boolean larguraProgress)
	{
		this.larguraProgress = larguraProgress;
	}

	public boolean isAlturaProgress()
	{
		return alturaProgress;
	}

	/**
	 * indicador se o a barra de progresso cresce em altura
	 * 
	 * @param alturaProgress
	 */
	public void setAlturaProgress(final boolean alturaProgress)
	{
		this.alturaProgress = alturaProgress;
	}

	public void setARGB(final int a, final int r, final int g, final int b)
	{
		color.setARGB(a, r, g, b);
	}

}

package org.vvgaming.harmegido.theGame.objNodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.geometry.Retangulo;

import android.graphics.Canvas;
import android.graphics.Paint;

public class NCircleProgressBar extends GameNode
{

	private final Paint color = new Paint();
	private final Retangulo ret;

	private float progress = 0f;

	public NCircleProgressBar(final float x, final float y, final float w, final float h)
	{
		ret = new Retangulo(new Ponto(x, y), w, h);
	}

	@Override
	public void update(final long delta)
	{
	}

	@Override
	public void render(final Canvas canvas)
	{
		canvas.drawArc(ret.toAndroidCanvasRectF(), -90, (progress * 360), true, color);
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

	public void setARGB(final int a, final int r, final int g, final int b)
	{
		color.setARGB(a, r, g, b);
	}

}

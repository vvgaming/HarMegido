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

	private float progress = 0f;

	public NProgressBar(float x, float y, float w, float h)
	{
		ret = new Retangulo(new Ponto(x, y), w, h);
	}

	@Override
	public void update(long delta)
	{
		retToDraw = new Retangulo(ret.origem, ret.width * progress, ret.height);
	}

	@Override
	public void render(Canvas canvas)
	{
		canvas.drawRect(retToDraw.toAndroidCanvasRect(), color);
	}

	public void setProgress(float percentProgress)
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

}

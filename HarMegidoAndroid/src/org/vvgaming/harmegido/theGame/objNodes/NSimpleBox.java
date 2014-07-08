package org.vvgaming.harmegido.theGame.objNodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.geometry.Retangulo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.detentor.codex.product.Tuple3;

public class NSimpleBox extends GameNode
{

	private final Paint color = new Paint();

	private final Retangulo ret;

	private boolean visible = true;

	public NSimpleBox(final int x, final int y, final int w, final int h, final int colorR, final int colorG, final int colorB,
			final int colorA)
	{
		color.setARGB(colorA, colorR, colorG, colorB);
		ret = new Retangulo(new Ponto(x, y), w, h);

	}

	public NSimpleBox(final int x, final int y, final int w, final int h, final int colorR, final int colorG, final int colorB)
	{
		this(x, y, w, h, colorR, colorG, colorB, 255);
	}

	@Override
	public void update(final long delta)
	{

	}

	@Override
	public void render(final Canvas canvas)
	{
		canvas.drawRect(ret.toAndroidCanvasRect(), color);
	}

	public void setColor(final int r, final int g, final int b)
	{
		color.setARGB(255, r, g, b);
	}

	public void setColor(final int a, final int r, final int g, final int b)
	{
		color.setARGB(a, r, g, b);
	}

	public Tuple3<Integer, Integer, Integer> getColor()
	{
		final int colorNumber = color.getColor();
		return Tuple3.from(Color.red(colorNumber), Color.green(colorNumber), Color.blue(colorNumber));
	}

	@Override
	public boolean isVisible()
	{
		return visible;
	}

	@Override
	public void setVisible(final boolean visible)
	{
		this.visible = visible;
	}

}

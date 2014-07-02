package org.vvgaming.harmegido.gameEngine.nodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Uma implementação de {@link GameNode} que dispõe e manipula texto
 * 
 * @author Vinicius Nogueira
 */
public class NText extends GameNode
{

	public enum VerticalAlign
	{
		TOP, BOTTOM, MIDDLE;
		
		public float getY(NText fromText)
		{
			final int nLines = fromText.text.split("\n").length;
			
			switch (this)
			{
				case TOP:
					return fromText.pos.y + (nLines * fromText.size);
				case BOTTOM:
					return fromText.pos.y;
				case MIDDLE:
					return fromText.pos.y - ((nLines * fromText.size) / 2);
				default:
					throw new IllegalArgumentException("Alinhamento vertical desconhecido: " + this);
			}
		}
		
	}

	public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	public Typeface face;
	public String text;
	public Ponto pos;
	public VerticalAlign vAlign = VerticalAlign.MIDDLE;
	public Integer size;

	private boolean visible = true;

	public NText()
	{
		this(0, 0, "");
	}
	
	public NText(Ponto ponto, String text)
	{
		paint.setARGB(255, 255, 255, 255);
		pos = ponto;
		paint.setTypeface(face);
		this.text = text;
	}

	public NText(float x, float y, String text)
	{
		this(new Ponto(x, y), text);
	}

	@Override
	public void init()
	{
		if(size == null)
		size = getSmallFontSize();
	}

	@Override
	public void update(long delta)
	{
		paint.setTextSize(size);
		paint.setTypeface(face);
	}

	@Override
	public void render(Canvas canvas)
	{
		final String[] splitByLine = text.split("\n");
		float yStart = vAlign.getY(this);

		for (int i = 0; i < splitByLine.length; i++)
		{
			canvas.drawText(splitByLine[i], pos.x, yStart + (i * size), paint);
		}
	}

	@Override
	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public void setARGB(final int a, final int r, final int g, final int b)
	{
		paint.setARGB(a, r, g, b);
	}

	public NText clone()
	{
		NText retorno = new NText();
		retorno.face = face;
		retorno.size = size;
		retorno.visible = visible;
		retorno.paint = new Paint(paint);
		retorno.face = face;
		retorno.pos = pos;
		retorno.text = text;
		return retorno;
	}

}

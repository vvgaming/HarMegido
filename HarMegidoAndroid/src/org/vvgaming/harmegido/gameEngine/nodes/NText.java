package org.vvgaming.harmegido.gameEngine.nodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Uma implementação de {@link GameNode} que dispõe e manipula texto
 * 
 * @author Vinicius Nogueira
 */
public class NText extends GameNode
{
	public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	public Typeface face;
	public String text;
	public Ponto pos;
	public VerticalAlign vAlign = VerticalAlign.MIDDLE;
	private float size;

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
		if (size == 0.0f)
		{
			size = getSmallFontSize();
		}
	}
	
	public void setSize(final float newSize)
	{
		this.size = newSize;
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
	
	/**
	 * Redimensiona o tamanho da fonte do texto de forma a caber
	 * dentro da largura passada como parâmetro
	 * @param desiredWidth o tamanho desejado
	 */
	public void setWidth(float desiredWidth) 
	{
		if (text.isEmpty())
		{
			return;
		}
		
		final String[] linhas = text.split("\n");
		
		int maxIndex = 0;
		int prevSize = -1;
		
		//Encontra qual a linha com o maior texto
		for (int i = 0; i < linhas.length; i++)
		{
			int curSize = getTextBounds(linhas[i]).width();
			if (curSize > prevSize)
			{
				prevSize = curSize;
				maxIndex = i;
			}
		}
		
		//Redimensiona a caixa de acordo com a linha de maior texto
	    final float testTextSize = 24f;
	    paint.setTextSize(testTextSize);
	    Rect bounds = new Rect();
	    paint.getTextBounds(linhas[maxIndex], 0, linhas[maxIndex].length(), bounds);
	    size = testTextSize * desiredWidth / bounds.width();
	    paint.setTextSize(size);
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
		retorno.visible = visible;
		retorno.paint = new Paint(paint);
		retorno.face = face;
		retorno.pos = pos;
		retorno.text = text;
		return retorno;
	}
	

	private Rect getTextBounds(final String theText)
	{
		final Rect toReturn = new Rect();
		
		if (theText == "")
		{
			return toReturn;
		}
		
		final String[] linhas = theText.split("\n");
		
		int maxWidth = 0;
		int totalHeight = 0;
		
		for (int i = 0; i < linhas.length; i++)
		{
			paint.getTextBounds(linhas[i], 0, linhas[i].length(), toReturn);
			
			if (toReturn.width() > maxWidth)
			{
				maxWidth = toReturn.width();
			}
			
			totalHeight += toReturn.height();
		}
		
		toReturn.bottom = toReturn.top + totalHeight;
		toReturn.right = toReturn.left + maxWidth;
		
		return toReturn;
	}
	
	/**
	 * Retorna o menor retângulo que cabe o texto deste componente
	 */
	public Rect getTextBounds()
	{
		return getTextBounds(text);
	}
	
	public enum VerticalAlign
	{
		TOP, BOTTOM, MIDDLE;
		
		public float getY(NText fromText)
		{
			final int nLines = fromText.text.split("\n").length;
			final float size = fromText.size;
			
			switch (this)
			{
				case TOP:
					return fromText.pos.y + (nLines * size);
				case BOTTOM:
					return fromText.pos.y;
				case MIDDLE:
					return fromText.pos.y - ((nLines * size) / 2);
				default:
					throw new IllegalArgumentException("Alinhamento vertical desconhecido: " + this);
			}
		}
	}

}

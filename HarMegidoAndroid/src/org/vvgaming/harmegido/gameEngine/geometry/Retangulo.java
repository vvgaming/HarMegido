package org.vvgaming.harmegido.gameEngine.geometry;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Classe geométrica que representa o retângulo e suas operações
 * 
 * @author Vinicius Nogueira
 */
public class Retangulo
{

	public final Ponto origem;
	public final float width, height;

	public Retangulo(final Ponto origem, final float width, final float height)
	{
		super();
		this.origem = origem;
		this.width = width;
		this.height = height;
	}

	public Rect toAndroidCanvasRect()
	{
		return new Rect((int) origem.x, (int) origem.y, (int) (origem.x + width), (int) (origem.y + height));
	}

	public RectF toAndroidCanvasRectF()
	{
		return new RectF(origem.x, origem.y, origem.x + width, origem.y + height);
	}

	public boolean contains(final Ponto ponto)
	{
		return ponto.x >= origem.x && ponto.x <= origem.x + width && ponto.y >= origem.y && ponto.y <= origem.y + height;
	}

	public Ponto getCenter()
	{
		return new Ponto(origem.x + width / 2, origem.y + height / 2);
	}

	public static Retangulo fromCenter(final Ponto centro, final float w, final float h)
	{
		return new Retangulo(new Ponto(centro.x - w / 2, centro.y - h / 2), w, h);
	}

}

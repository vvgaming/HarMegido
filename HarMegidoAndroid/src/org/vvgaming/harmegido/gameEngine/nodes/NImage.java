package org.vvgaming.harmegido.gameEngine.nodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.MatrizTransfAndroid;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Mostra e manipula imagens
 * 
 * @author Vinicius Nogueira
 */
public class NImage extends GameNode
{

	private final Paint paint;
	private Bitmap bmp;
	private boolean visible = true;

	// matriz de transformações
	private MatrizTransfAndroid matriz;

	public NImage(final int x, final int y, final Bitmap bmp)
	{
		this(new Ponto(x, y), bmp);
	}

	public NImage(Ponto center, final Bitmap bmp)
	{
		this.bmp = bmp;
		matriz = new MatrizTransfAndroid(bmp.getWidth(), bmp.getHeight());
		matriz.setCenter(center);
		paint = new Paint();

		// TODO os parametros abaixo melhorando bastante o visual das imagens,
		// mas implicam bastante na performance, diminuindo significativamente o fps
		// Pensar em como melhorar isso, ou expor setter para usar, só em alguns casos
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
	}

	@Override
	public void update(long delta)
	{

	}

	/**
	 * Retorna a posição X e Y da imagem
	 */
	public Ponto getPos()
	{
		final Ponto center = matriz.getCenter();
		return new Ponto(center.x - (matriz.getDstWidth() / 2), center.y - (matriz.getDstHeight() / 2));
	}

	@Override
	public void render(Canvas canvas)
	{
		canvas.drawBitmap(bmp, matriz.getMatrix(), paint);
	}

	@Override
	public boolean isDead()
	{
		return false;
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

	public void setDimensions(final float width, final float height)
	{
		matriz.setDimensions(width, height);
	}

	public float getHeight()
	{
		return matriz.getDstHeight();
	}

	public float getWidth()
	{
		return matriz.getDstWidth();
	}

	public void setWidth(float width)
	{
		matriz.setWidth(width);
	}

	/**
	 * Define a largura da imagem, mantendo a proporção, se selecionado.
	 * 
	 * @param width A nova largura da imagem
	 * @param keepAspectRatio Se marcado, a altura da imagem também será alterada, de forma a manter a proporção
	 */
	public void setWidth(float width, boolean keepAspectRatio)
	{
		matriz.setWidth(width, keepAspectRatio);
	}

	public void setHeight(float height)
	{
		matriz.setHeight(height);
	}

	/**
	 * Define a altura da imagem, mantendo a proporção, se selecionado.
	 * 
	 * @param height A nova altura da imagem
	 * @param keepAspectRatio Se marcado, a largura da imagem também será alterada, de forma a manter a proporção
	 */
	public void setHeight(float height, boolean keepAspectRatio)
	{
		matriz.setHeight(height, keepAspectRatio);
	}

	public void setRotation(final float rotation)
	{
		matriz.setRotation(rotation);
	}

	public void setCenter(final Ponto center)
	{
		matriz.setCenter(center);
	}

	public void setScale(final float scale)
	{
		matriz.setScale(scale);
	}

	public void setBmp(Bitmap bmp)
	{
		this.bmp = bmp;
	}

	public Bitmap getBmp()
	{
		return bmp;
	}

	public void sethFlip(boolean hFlip)
	{
		matriz.sethFlip(hFlip);
	}

	public void setvFlip(boolean vFlip)
	{
		matriz.setvFlip(vFlip);
	}

	public RectF getBoundingRect()
	{
		return matriz.getBoundingRect();
	}

	public Ponto getCenter()
	{
		return matriz.getCenter();
	}

	public Paint getPaint()
	{
		return paint;
	}

}

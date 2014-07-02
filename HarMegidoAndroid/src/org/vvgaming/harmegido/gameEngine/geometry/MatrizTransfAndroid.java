package org.vvgaming.harmegido.gameEngine.geometry;

import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * Wrapper da {@link Matrix} do Android, provendo novas funcionalidades
 * 
 * @author Vinicius Nogueira
 */
public class MatrizTransfAndroid {

	private final Matrix matrix;

	private final float srcWidth;
	private final float srcHeight;

	private float rotation = 0;

	private float dstWidth;
	private float dstHeight;

	private Ponto center = new Ponto(0, 0);

	private boolean hFlip = false;
	private boolean vFlip = false;

	public MatrizTransfAndroid(float srcWidth, float srcHeight) {
		this(0, srcWidth, srcHeight, srcWidth, srcHeight, new Ponto(0, 0));
	}

	public MatrizTransfAndroid(float rotation, float srcWidth, float srcHeight,
			float dstWidth, float dstHeight, Ponto center) {
		super();

		matrix = new Matrix();
		this.rotation = rotation;
		this.srcWidth = srcWidth;
		this.srcHeight = srcHeight;
		this.dstWidth = dstWidth;
		this.dstHeight = dstHeight;
		this.center = center;
		refreshMatrix();
	}

	private void refreshMatrix() {
		matrix.reset();
		
		if (hFlip) {
			matrix.postScale(-1, 1);
			matrix.postTranslate(dstWidth, 0);
		}
		if (vFlip) {
			matrix.postScale(1, -1);
			matrix.postTranslate(0, dstHeight);
		}
		
		matrix.postScale(dstWidth / srcWidth, dstHeight / srcHeight);
		matrix.postTranslate(center.x - dstWidth / 2, center.y - dstHeight / 2);
		matrix.postRotate(rotation, center.x, center.y);
	}

	public void setDimensions(final float width, final float height) {
		this.dstWidth = width;
		this.dstHeight = height;
		refreshMatrix();
	}
	
	public void setWidth(final float width)
	{
		setWidth(width, false);
	}
	
	/**
	 * Define a largura da imagem, mantendo a proporção, se selecionado.
	 * @param width A nova largura da imagem
	 * @param keepAspectRatio Se marcado, a altura da imagem também será alterada,
	 * de forma a manter a proporção
	 */
	public void setWidth(final float width, final boolean keepAspectRatio)
	{
		setDimensions(width, keepAspectRatio ? (srcHeight / srcWidth) * width : dstHeight);
	}
	
	public void setHeight(final float height)
	{
		setWidth(height, false);
	}
	
	/**
	 * Define a altura da imagem, mantendo a proporção, se selecionado.
	 * @param height A nova altura da imagem
	 * @param keepAspectRatio Se marcado, a largura da imagem também será alterada,
	 * de forma a manter a proporção
	 */
	public void setHeight(final float height, final boolean keepAspectRatio)
	{
		setDimensions(keepAspectRatio ? (srcWidth / srcHeight) * height : dstWidth, height);
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
		refreshMatrix();
	}

	public void setCenter(Ponto center) {
		this.center = center;
		refreshMatrix();
	}

	public void setScale(final float scaleX, final float scaleY) {
		dstWidth = srcWidth * scaleX;
		dstHeight = srcHeight * scaleY;
		refreshMatrix();
	}

	public void setScale(final float scale) {
		setScale(scale, scale);
	}

	public void sethFlip(boolean hFlip) {
		this.hFlip = hFlip;
		refreshMatrix();
	}

	public void setvFlip(boolean vFlip) {
		this.vFlip = vFlip;
		refreshMatrix();
	}

	public boolean ishFlip() {
		return hFlip;
	}

	public boolean isvFlip() {
		return vFlip;
	}

	public float getRotation() {
		return rotation;
	}

	public float getSrcWidth() {
		return srcWidth;
	}

	public float getSrcHeight() {
		return srcHeight;
	}

	public float getDstWidth() {
		return dstWidth;
	}

	public float getDstHeight() {
		return dstHeight;
	}

	public Ponto getCenter() {
		return center;
	}

	public Matrix getMatrix() {
		return new Matrix(matrix);
	}

	public RectF getBoundingRect() {

		float x0 = 0;
		float y0 = 0;

		float[] pts = new float[] { x0, y0, x0 + getSrcWidth(), y0, x0,
				y0 + getSrcHeight(), x0 + getSrcWidth(), y0 + getSrcHeight() };
		matrix.mapPoints(pts);

		// procurar o maior x
		float maiorX = Float.MIN_VALUE;
		float menorX = Float.MAX_VALUE;
		for (int i = 0; i < pts.length; i += 2) {
			if (pts[i] > maiorX) {
				maiorX = pts[i];
			}
			if (pts[i] < menorX) {
				menorX = pts[i];
			}
		}

		// procurar o maior y
		float maiorY = Float.MIN_VALUE;
		float menorY = Float.MAX_VALUE;
		for (int i = 1; i < pts.length; i += 2) {
			if (pts[i] > maiorY) {
				maiorY = pts[i];
			}
			if (pts[i] < menorY) {
				menorY = pts[i];
			}
		}
		return new RectF(menorX, menorY, maiorX, maiorY);
	}
}

package org.vvgaming.harmegido.gameEngine.geometry;

import android.graphics.Matrix;

/**
 * 
 */
public class MatrizTransfAndroid {

	private final Matrix matrix;

	private float rotation = 0;

	private final float srcWidth;
	private final float srcHeight;

	private float dstWidth;
	private float dstHeight;

	private Ponto center = new Ponto(0, 0);

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
		matrix.postScale(dstWidth / srcWidth, dstHeight / srcHeight);
		matrix.postTranslate(center.x - dstWidth / 2, center.y - dstHeight / 2);
		matrix.postRotate(rotation, center.x, center.y);
	}

	public void setDimensions(final float width, final float height) {
		this.dstWidth = width;
		this.dstHeight = height;
		refreshMatrix();
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

}

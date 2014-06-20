package org.vvgaming.harmegido.gameEngine.nodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.MatrizTransfAndroid;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Mostra e manipula imagens
 * 
 * @author Vinicius Nogueira
 */
public class NImage extends GameNode {

	private boolean visible = true;

	private Bitmap bmp;

	// matriz de transformações
	private MatrizTransfAndroid matriz;

	public NImage(final int x, final int y, final Bitmap bmp) {
		this(new Ponto(x, y), bmp);
	}

	public NImage(Ponto center, final Bitmap bmp) {
		this.bmp = bmp;
		matriz = new MatrizTransfAndroid(bmp.getWidth(), bmp.getHeight());
		matriz.setCenter(center);
	}

	@Override
	public void update(long delta) {

	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawBitmap(bmp, matriz.getMatrix(), null);
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public void end() {
		bmp.recycle();
		bmp = null;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setDimensions(final float width, final float height) {
		matriz.setDimensions(width, height);
	}

	public void setWidthKeepingRatio(final float width) {
		matriz.setWidthKeepingRatio(width);
	}

	public void setHeightKeepingRatio(final float height) {
		matriz.setHeightKeepingRatio(height);
	}

	public void setRotation(final float rotation) {
		matriz.setRotation(rotation);
	}

	public void setCenter(final Ponto center) {
		matriz.setCenter(center);
	}

	public void setScale(final float scale) {
		matriz.setScale(scale);
	}

	public RectF getBoundingRect() {
		return matriz.getBoundingRect();
	}
	
	

}

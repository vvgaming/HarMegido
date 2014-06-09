package org.vvgaming.harmegido.theGame.gos;

import org.vvgaming.harmegido.gameEngine.GameObject;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class ImageGO implements GameObject {

	private boolean visible = true;

	private Bitmap bmp;
	private Ponto center;
	// matriz de transformações
	private Matrix matrix;

	private float rotation = 0;
	private float scaleX = 1;
	private float scaleY = 1;

	private final Resources res;
	private final int resourceId;

	public ImageGO(int x, int y, Resources res, int resourceId) {
		this(new Ponto(x, y), res, resourceId);
	}

	public ImageGO(Ponto center, Resources res, int resourceId) {
		this.center = center;
		this.res = res;
		this.resourceId = resourceId;
	}

	@Override
	public void init() {
		bmp = BitmapFactory.decodeResource(res, resourceId);
		matrix = new Matrix();
	}

	@Override
	public void update(long delta) {
		matrix.reset();
		matrix.postScale(scaleX, scaleY);
		matrix.postTranslate(center.x - (bmp.getWidth() * scaleX)/2,
				center.y - (bmp.getHeight() * scaleY)/2);
		matrix.postRotate(rotation, center.x, center.y);
	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawBitmap(bmp, matrix, null);
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

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
	public void setScale(float scale) {
		setScaleX(scale);
		setScaleY(scale);
	}

}

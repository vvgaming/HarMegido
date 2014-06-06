package org.vvgaming.harmegido.theGame.gos;

import org.vvgaming.harmegido.gameEngine.GameObject;
import org.vvgaming.harmegido.gameEngine.geometry.Retangulo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class ImageGO implements GameObject {

	private boolean visible = true;

	private Bitmap bmp;
	private Rect dstRect;
	private Rect srcRect;
	// matriz de transformações
	private Matrix matrix;

	private float rotation = 0;

	private final Resources res;
	private final int resourceId;

	public ImageGO(int x, int y, int w, int h, Resources res, int resourceId) {
		dstRect = new Rect(x, y, x + w, y + h);
		this.res = res;
		this.resourceId = resourceId;

	}

	public ImageGO(Retangulo ret, Resources res, int resourceId) {
		this((int) ret.origem.x, (int) ret.origem.y, (int) ret.w, (int) ret.h,
				res, resourceId);
	}

	@Override
	public void init() {
		bmp = BitmapFactory.decodeResource(res, resourceId);
		srcRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
		matrix = new Matrix();
		matrix.postScale((float)dstRect.width() / (float)srcRect.width(), (float)dstRect.height()
				/ (float)srcRect.height());
		matrix.postTranslate(dstRect.left, dstRect.top);
	}

	@Override
	public void update(long delta) {
		matrix.postRotate(delta, dstRect.centerX(), dstRect.centerY());
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

}

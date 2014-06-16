package org.vvgaming.harmegido.theGame.gos;

import org.vvgaming.harmegido.gameEngine.LazyInitGameObject;
import org.vvgaming.harmegido.gameEngine.geometry.MatrizTransfAndroid;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.github.detentor.codex.function.Function0;

public class ImageGO extends LazyInitGameObject {

	private boolean visible = true;

	private Bitmap bmp;
	private Ponto center;

	// matriz de transformações
	MatrizTransfAndroid matriz;

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
	public void preInit() {
		bmp = BitmapFactory.decodeResource(res, resourceId);
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
		if (matriz == null) {
			onInit(new Function0<Void>() {
				@Override
				public Void apply() {
					ImageGO.this.matriz.setDimensions(width, height);
					return null;
				}
			});
			return;
		}
		matriz.setDimensions(width, height);
	}

	public void setRotation(final float rotation) {
		if (matriz == null) {
			onInit(new Function0<Void>() {

				@Override
				public Void apply() {
					ImageGO.this.matriz.setRotation(rotation);
					return null;
				}
			});
			return;
		}
		matriz.setRotation(rotation);
	}

	public void setCenter(final Ponto center) {
		if (matriz == null) {
			onInit(new Function0<Void>() {

				@Override
				public Void apply() {
					ImageGO.this.matriz.setCenter(center);
					return null;
				}
			});
			return;
		}
		matriz.setCenter(center);
	}

	public void setScale(final float scale) {
		if (matriz == null) {
			onInit(new Function0<Void>() {

				@Override
				public Void apply() {
					ImageGO.this.matriz.setScale(scale);
					return null;
				}
			});
			return;
		}
		matriz.setScale(scale);
	}

}

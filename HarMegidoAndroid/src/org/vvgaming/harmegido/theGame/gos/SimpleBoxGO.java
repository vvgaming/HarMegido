package org.vvgaming.harmegido.theGame.gos;

import org.vvgaming.harmegido.gameEngine.GameObject;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.geometry.Retangulo;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SimpleBoxGO implements GameObject {

	private final Paint color = new Paint();

	private boolean visible = true;

	private final Retangulo ret;

	public SimpleBoxGO(int x, int y, int w, int h, int colorR, int colorG,
			int colorB, int colorA) {
		color.setARGB(colorA, colorR, colorG, colorB);
		ret = new Retangulo(new Ponto(x, y), w, h);

	}

	public SimpleBoxGO(int x, int y, int w, int h, int colorR, int colorG,
			int colorB) {
		this(x, y, w, h, colorR, colorG, colorB, 255);
	}

	@Override
	public void update(long delta) {

	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawRect(ret.toAndroidCanvasRect(), color);
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setColor(int r, int g, int b){
		color.setARGB(255, r, g, b);
	}
	
}

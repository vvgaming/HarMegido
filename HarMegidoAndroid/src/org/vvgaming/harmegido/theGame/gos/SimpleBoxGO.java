package org.vvgaming.harmegido.theGame.gos;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.geometry.Retangulo;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SimpleBoxGO extends GameNode {

	private final Paint color = new Paint();

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

	public void setColor(int r, int g, int b) {
		color.setARGB(255, r, g, b);
	}

	public void setColor(int a, int r, int g, int b) {
		color.setARGB(a, r, g, b);
	}

}

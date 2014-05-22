package org.vvgaming.harmegido.bubbleTest;

import org.vvgaming.harmegido.gameEngine.GameObject;
import org.vvgaming.harmegido.gameEngine.geometry.Retangulo;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SquareButton implements GameObject {

	private final Paint white = new Paint();
	private final Paint black = new Paint();

	public final Retangulo ret;
	public final String text;

	public SquareButton(int x, int y, int w, int h, String text) {
		white.setARGB(255, 255, 255, 255);
		black.setARGB(255, 0, 0, 0);
		black.setTextSize(20);

		ret = new Retangulo(new Ponto(x, y), w, h);
		this.text = text;

	}

	@Override
	public void update(long delta) {

	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawRect(ret.toAndCanvasRect(), white);
		canvas.drawText(text, ret.origem.x, ret.origem.y + ret.h / 2, black);
	}

	@Override
	public boolean isDead() {
		return false;
	}
}

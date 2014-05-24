package org.vvgaming.harmegido.bubbleTest;

import org.vvgaming.harmegido.gameEngine.GameObject;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Text implements GameObject {

	private Paint cinza = new Paint();
	public String text;
	public Ponto pos;

	public Text(int x, int y, String text) {
		cinza.setARGB(255, 200, 200, 200);
		pos = new Ponto(x, y);
		this.text = text;
	}

	@Override
	public void update(long delta) {

	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawText(text, pos.x, pos.y, cinza);
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
}

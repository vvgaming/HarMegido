package org.vvgaming.harmegido.gameEngine;

import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

public class Text implements GameObject {

	public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	public Typeface face;
	public String text;
	public Ponto pos;
	public int size = 12;

	private boolean visible = true;

	public Text(int x, int y, String text) {
		paint.setARGB(255, 255, 255, 255);
		pos = new Ponto(x, y);
		paint.setTextSize(size);
		
		paint.setTypeface(face);
		this.text = text;
	}

	@Override
	public void update(long delta) {
		paint.setTextSize(size);
		paint.setTypeface(face);
	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawText(text, pos.x, pos.y, paint);
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public void init() {
	}

	@Override
	public void end() {
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setARGB(final int a, final int r, final int g, final int b) {
		paint.setARGB(a, r, g, b);
	}
	
	

}

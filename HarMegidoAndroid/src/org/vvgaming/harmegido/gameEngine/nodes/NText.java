package org.vvgaming.harmegido.gameEngine.nodes;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Uma implementação de {@link GameObject} que dispõe e manipula texto
 * 
 * @author Vinicius Nogueira
 */
public class NText extends GameNode {

	public enum VerticalAlign {
		TOP, MIDDLE;
	}

	public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	public Typeface face;
	public String text;
	public Ponto pos;
	public VerticalAlign vAlign = VerticalAlign.MIDDLE;
	public int size = 12;

	private boolean visible = true;

	public NText() {
		this(0, 0, "");
	}

	public NText(int x, int y, String text) {
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
		final String[] splitByLine = text.split("\n");
		float yStart = pos.y;
		switch (vAlign) {
		case TOP:
			yStart = pos.y;
			break;
		case MIDDLE:
			yStart = pos.y - ((splitByLine.length * size) / 2);
			break;
		default:
			throw new IllegalArgumentException(
					"Alinhamento vertical desconhecido: " + vAlign);
		}
		for (int i = 0; i < splitByLine.length; i++) {
			canvas.drawText(splitByLine[i], pos.x, yStart + (i * size), paint);
		}
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

	public NText clone() {
		NText retorno = new NText();
		retorno.face = face;
		retorno.size = size;
		retorno.visible = visible;
		retorno.paint = new Paint(paint);
		retorno.face = face;
		retorno.pos = pos;
		retorno.text = text;
		return retorno;
	}

}

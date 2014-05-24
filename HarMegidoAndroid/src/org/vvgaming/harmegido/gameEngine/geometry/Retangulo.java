package org.vvgaming.harmegido.gameEngine.geometry;

import android.graphics.Rect;

public class Retangulo {

	public final Ponto origem;
	public final float w, h;

	public Retangulo(Ponto origem, float w, float h) {
		super();
		this.origem = origem;
		this.w = w;
		this.h = h;
	}

	public Rect toAndroidCanvasRect() {
		return new Rect((int) origem.x, (int) origem.y, (int) (origem.x + w),
				(int) (origem.y + h));
	}

	public boolean contem(Ponto ponto) {
		return (ponto.x >= origem.x && ponto.x <= (origem.x + w)
				&& ponto.y >= origem.y && ponto.y <= (origem.y + h));
	}

	public Ponto getCenter() {
		return new Ponto(origem.x + w / 2, origem.y + h / 2);
	}

}

package org.vvgaming.harmegido.gameEngine.geometry;

import android.graphics.Rect;

public class Retangulo {

	public final Vetor2d origem;
	public final float w, h;

	public Retangulo(Vetor2d origem, float w, float h) {
		super();
		this.origem = origem;
		this.w = w;
		this.h = h;
	}

	public Rect toAndCanvasRect() {
		return new Rect((int) origem.x, (int) origem.y, (int) (origem.x + w),
				(int) (origem.y + h));
	}

	public boolean contem(Vetor2d ponto){
		return (ponto.x >= origem.x && 
				ponto.x <= (origem.x + w) &&	
				ponto.y >= origem.y && 
				ponto.y <= (origem.y + h));
		
	}

}

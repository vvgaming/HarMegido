package org.vvgaming.harmegido.gameEngine.geometry;

/**
 * Classe geométrica que representa o ponto e suas operações
 * 
 * @author Vinicius Nogueira
 */
public class Ponto {

	public final float x, y;

	public Ponto(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Ponto translate(float x, float y) {
		return new Ponto(this.x + x, this.y + y);
	}

}

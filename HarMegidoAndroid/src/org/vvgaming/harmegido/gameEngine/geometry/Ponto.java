package org.vvgaming.harmegido.gameEngine.geometry;

/**
 * Classe geométrica que representa o ponto e suas operações
 * 
 * @author Vinicius Nogueira
 */
public class Ponto
{

	public final float x, y;

	public Ponto(final float x, final float y)
	{
		super();
		this.x = x;
		this.y = y;
	}

	public Ponto translate(final float x, final float y)
	{
		return new Ponto(this.x + x, this.y + y);
	}

}

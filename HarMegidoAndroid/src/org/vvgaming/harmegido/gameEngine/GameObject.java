package org.vvgaming.harmegido.gameEngine;

import android.graphics.Canvas;

/**
 * Interface que define comportamento padrão para objetos em jogos do tipo
 * {@link AbstractGame}
 * 
 * @author Vinicius Nogueira
 */
public interface GameObject {

	/**
	 * Método invocado a cada frame para que o objeto faça suas atualizações
	 * necessárias
	 * 
	 * @param delta
	 *            é o tempo decorrido desde no último frame, para os casos em
	 *            que seja necessário fazer atualizações baseadas em variações
	 *            de tempo
	 */
	void update(final long delta);
	
	/**
	 * Método invocado a cada frame para desenhar (ou renderizar) o objeto na
	 * tela. Deve se valer do canvas do parâmetro, para realizar o desenho
	 * 
	 * @param canvas
	 */
	void render(final Canvas canvas);

	boolean isDead();

}

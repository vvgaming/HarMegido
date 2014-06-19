package org.vvgaming.harmegido.gameEngine;

import android.graphics.Canvas;

/**
 * Interface que define comportamento padr�o para objetos em jogos do tipo
 * {@link AbstractGameScene}
 * 
 * @author Vinicius Nogueira
 */
public interface GameObject {

	/**
	 * Método invocado a cada frame para que o objeto fa�a suas atualiza��es
	 * necess�rias
	 * 
	 * @param delta
	 *            � o tempo decorrido desde no �ltimo frame, para os casos em
	 *            que seja necess�rio fazer atualiza��es baseadas em varia��es
	 *            de tempo
	 */
	void update(final long delta);

	/**
	 * Método invocado a cada frame para desenhar (ou renderizar) o objeto na
	 * tela. Deve se valer do canvas do par�metro, para realizar o desenho
	 * 
	 * @param canvas
	 */
	void render(final Canvas canvas);

	boolean isDead();

	boolean isVisible();

	void init();

	void end();

}

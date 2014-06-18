package org.vvgaming.harmegido.theGame;

import org.opencv.core.Mat;
import org.vvgaming.harmegido.vision.JavaCameraFrame;

import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * É uma especificação de câmera que tem a capacidade de capturar determinados
 * frames e compará-los ao atuais que fica comparando os frames com frames
 * anteriores guardados, verificando sua similaridade. <br/>
 * 
 * @author Vinicius Nogueira
 */
public interface SimilarityCam<T> {

	/**
	 * Captura um snapshot retornando a imagem capturada e os dados necessários
	 * à comparação deste frame no futuro
	 * 
	 * @return
	 */
	public abstract T snapshot();

	/**
	 * Inicia observação dos dados informados por parametro, isto é, fica
	 * comparando o frame atual com este informado
	 * 
	 * @param obs
	 */
	public abstract void observar(T obs);

	/**
	 * Para a observação iniciada por
	 * {@link FeaturesSimilarityCam#observar(Tuple2)}
	 */
	public abstract void stopObservar();

	/**
	 * Compara o frame atual com o frame capturado anteriormente
	 * 
	 * @return retorna o valor da comparação de acordo com a métrica definida ou
	 *         option vazia, caso não tenha sido um frame capturado
	 *         anteriormente. Capture com
	 *         {@link FeaturesSimilarityCam#snapshot()} e inicie observação com
	 *         {@link FeaturesSimilarityCam#observar(Mat)}
	 */
	public abstract Option<Float> compara();

	/**
	 * Conecta à câmera com a resolução desejada
	 * 
	 * @param width
	 * @param height
	 */
	public abstract void connectCamera(int width, int height);

	/**
	 * Libera a câmera
	 */
	public abstract void disconnectCamera();

	/**
	 * Recupera o último frame
	 * 
	 * @return
	 */
	public abstract JavaCameraFrame getLastFrame();

	/**
	 * Implementa a regra para ver se o resultado da comparação é suficiente
	 * para considerar a imagem "bastante" similar
	 * 
	 * @param comparacao
	 * @return
	 */
	public abstract boolean isSimilarEnough(float comparacao);

	public abstract int getHeight();

	public abstract int getWidth();

}
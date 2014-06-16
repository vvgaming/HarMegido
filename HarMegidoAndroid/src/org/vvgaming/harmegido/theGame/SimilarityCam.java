package org.vvgaming.harmegido.theGame;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.vvgaming.harmegido.vision.JavaCam;
import org.vvgaming.harmegido.vision.JavaCameraFrame;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Bitmap;

import com.github.detentor.codex.product.Tuple2;

/**
 * Wrapper de {@link JavaCam} para conferir novas funcionalidades. É uma câmera
 * que fica comparando os frames com frames anteriores guardados, verificando
 * sua similaridade. <br/>
 * Essencialmente é uma especialização de {@link JavaCam}, mas implementado como
 * agregação ao em vez de herança
 * 
 * @author Vinicius Nogueira
 */
public class SimilarityCam {

	public static final int EQUALITY_THRESHOLD = 20;

	private JavaCam realCam = new JavaCam();

	private Mat histogramaEmObservacao;

	public Tuple2<Mat, Bitmap> register() {

		// sincronizando a classe para garantir o acesso apenas de uma Thread
		// o código nativo apresenta uns erros estranhos ao ser acessado por
		// mais de uma Thread
		// TODO verificar se essa é a melhor forma mesmo, pois parece perigoso
		// ficar trancando o acesso
		synchronized (this) {
			final OCVUtil ocvUtil = OCVUtil.getInstance();

			final Mat rgba = getLastFrame().rgba().clone();

			Bitmap bmp = ocvUtil.toBmp(rgba);
			final Mat hist = ocvUtil.calcHistHS(rgba);

			ocvUtil.releaseMat(rgba);
			return Tuple2.from(hist, bmp);
		}
	}

	public void initObservar(Mat histograma) {
		synchronized (this) {
			stopObservar();
			histogramaEmObservacao = histograma;
		}
	}

	public void stopObservar() {
		// sincronizando a classe para garantir o acesso apenas de uma Thread
		// o código nativo apresenta uns erros estranhos ao ser acessado por
		// mais de uma Thread
		// TODO verificar se essa é a melhor forma mesmo, pois parece perigoso
		// ficar trancando o acesso
		synchronized (this) {
			OCVUtil.getInstance().releaseMat(histogramaEmObservacao);
			histogramaEmObservacao = null;
		}
	}

	public boolean emObservacao() {
		return histogramaEmObservacao != null;
	}

	public boolean isObservacaoOk() {

		if (histogramaEmObservacao != null) {
			final OCVUtil ocvUtil = OCVUtil.getInstance();

			final Mat atual = getLastFrame().rgba().clone();
			final Mat histAtual = ocvUtil.calcHistHS(atual);

			// compara o histograma
			final int METODO_COMPARACAO = 1;
			boolean result = Imgproc.compareHist(histAtual,
					histogramaEmObservacao, METODO_COMPARACAO) < EQUALITY_THRESHOLD;

			ocvUtil.releaseMat(atual, histAtual);
			return result;
		}
		return false;
	}

	public double compara() {
		if (histogramaEmObservacao != null) {
			final OCVUtil ocvUtil = OCVUtil.getInstance();

			final Mat atual = getLastFrame().rgba().clone();
			final Mat histAtual = ocvUtil.calcHistHS(atual);

			// compara o histograma
			final int METODO_COMPARACAO = 1;
			double result = Imgproc.compareHist(histAtual,
					histogramaEmObservacao, METODO_COMPARACAO);

			ocvUtil.releaseMat(atual, histAtual);
			return result;
		}
		return Double.MAX_VALUE;
	}

	public void connectCamera(int width, int height) {
		realCam.connectCamera(width, height);
	}

	public void disconnectCamera() {
		realCam.disconnectCamera();
	}

	public JavaCameraFrame getLastFrame() {

		// sincronizando a classe para garantir o acesso apenas de uma Thread
		// o código nativo apresenta uns erros estranhos ao ser acessado por
		// mais de uma Thread
		// TODO verificar se essa é a melhor forma mesmo, pois parece perigoso
		// ficar trancando o acesso
		synchronized (this) {
			return realCam.getLastFrame();
		}
	}

	public int getHeight() {
		return realCam.getHeight();
	}

	public int getWidth() {
		return realCam.getWidth();
	}

}

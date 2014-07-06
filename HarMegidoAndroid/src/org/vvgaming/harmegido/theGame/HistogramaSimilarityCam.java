package org.vvgaming.harmegido.theGame;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.vvgaming.harmegido.vision.JavaCam;
import org.vvgaming.harmegido.vision.JavaCameraFrame;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Bitmap;

import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Wrapper de {@link JavaCam} para conferir novas funcionalidades. � uma c�mera
 * que fica comparando os frames com frames anteriores guardados, verificando
 * sua similaridade. <br/>
 * Essencialmente � uma especializa��o de {@link JavaCam}, mas implementado como
 * agrega��o ao em vez de heran�a
 * 
 * @author Vinicius Nogueira
 */
public class HistogramaSimilarityCam implements
		SimilarityCam<Tuple2<Bitmap, Mat>> {

	private static final int EQUALITY_THRESHOLD = 20;

	private JavaCam realCam = new JavaCam();

	private Mat histogramaEmObservacao;

	@Override
	public Option<Tuple2<Bitmap, Mat>> snapshot() {
		// sincronizando a classe para garantir o acesso apenas de uma Thread
		// o c�digo nativo apresenta uns erros estranhos ao ser acessado por
		// mais de uma Thread
		// TODO verificar se essa � a melhor forma mesmo, pois parece perigoso
		// ficar trancando o acesso
		synchronized (this) {
			final OCVUtil ocvUtil = OCVUtil.getInstance();

			final Mat rgba = getLastFrame().rgba().clone();

			Bitmap bmp = ocvUtil.toBmp(rgba);
			final Mat hist = ocvUtil.calcHistHS(rgba);

			ocvUtil.releaseMat(rgba);
			return Option.from(Tuple2.from(bmp, hist));
		}
	}

	@Override
	public void observar(Tuple2<Bitmap, Mat> obs) {
		synchronized (this) {
			stopObservar();
			histogramaEmObservacao = obs.getVal2();
		}
	}

	@Override
	public void stopObservar() {
		// sincronizando a classe para garantir o acesso apenas de uma Thread
		// o c�digo nativo apresenta uns erros estranhos ao ser acessado por
		// mais de uma Thread
		// TODO verificar se essa � a melhor forma mesmo, pois parece perigoso
		// ficar trancando o acesso
		synchronized (this) {
			OCVUtil.getInstance().releaseMat(histogramaEmObservacao);
			histogramaEmObservacao = null;
		}
	}

	public Option<Float> compara() {
		if (histogramaEmObservacao != null) {
			final OCVUtil ocvUtil = OCVUtil.getInstance();

			final Mat atual = getLastFrame().rgba().clone();
			final Mat histAtual = ocvUtil.calcHistHS(atual);

			// compara o histograma
			final int METODO_COMPARACAO = 1;
			float result = (float) Imgproc.compareHist(histAtual,
					histogramaEmObservacao, METODO_COMPARACAO);

			ocvUtil.releaseMat(atual, histAtual);
			return Option.from(result);
		}
		return Option.empty();
	}

	public void connectCamera(int width, int height) {
		realCam.connectCamera(width, height);
	}

	public void disconnectCamera() {
		realCam.disconnectCamera();
	}

	public JavaCameraFrame getLastFrame() {

		// sincronizando a classe para garantir o acesso apenas de uma Thread
		// o c�digo nativo apresenta uns erros estranhos ao ser acessado por
		// mais de uma Thread
		// TODO verificar se essa � a melhor forma mesmo, pois parece perigoso
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

	@Override
	public boolean isSimilarEnough(float comparacao) {
		return comparacao < EQUALITY_THRESHOLD;
	}

}

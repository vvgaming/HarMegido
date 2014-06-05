package org.vvgaming.harmegido.theGame;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.vvgaming.harmegido.vision.Cam;
import org.vvgaming.harmegido.vision.NativeCameraFrame;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Bitmap;

import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Wrapper de {@link Cam} para conferir novas funcionalidades. É uma câmera que
 * fica comparando os frames com frames anteriores guardados. <br/>
 * Essencialmente é uma especialização de {@link Cam}, mas implementado como
 * agregação ao em vez de herança
 * 
 * @author Vinicius Nogueira
 */
public class SimilarityCam {

	private Cam realCam = new Cam();

	private Mat histogramaEmObservacao;

	public Option<Tuple2<Mat, Bitmap>> register() {

		final OCVUtil ocvUtil = OCVUtil.getInstance();

		final Mat rgba = getLastFrame().rgba().clone();

		Bitmap bmp = ocvUtil.toBmp(rgba);
		final Mat hist = ocvUtil.calcHistHS(rgba);

		ocvUtil.releaseMat(rgba);
		return Option.from(Tuple2.from(hist, bmp));

	}

	public void initObservar(Mat histograma) {
		stopObservar();
		histogramaEmObservacao = histograma;
	}

	public void stopObservar() {
		if (histogramaEmObservacao != null) {
			synchronized (histogramaEmObservacao) {
				OCVUtil.getInstance().releaseMat(histogramaEmObservacao);
				histogramaEmObservacao = null;
			}
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
					histogramaEmObservacao, METODO_COMPARACAO) < 20;

			ocvUtil.releaseMat(atual, histAtual);
			return result;
		}
		return false;
	}

	public boolean connectCamera(int width, int height) {
		return realCam.connectCamera(width, height);
	}

	public void disconnectCamera() {
		realCam.disconnectCamera();
	}

	public NativeCameraFrame getLastFrame() {
		return realCam.getFrame();
	}

	public int getHeight() {
		return realCam.getHeight();
	}

	public int getWidth() {
		return realCam.getWidth();
	}

}

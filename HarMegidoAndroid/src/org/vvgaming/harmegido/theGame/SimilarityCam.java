package org.vvgaming.harmegido.theGame;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.vvgaming.harmegido.vision.Cam;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Bitmap;

import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

public class SimilarityCam {

	private Cam realCam = new Cam();

	private Mat histogramaEmObservacao;

	public Option<Tuple2<Mat, Bitmap>> register() {

		if (realCam.isRunning()) {

			final OCVUtil ocvUtil = OCVUtil.getInstance();
			final Mat rgba = realCam.getLastFrame().rgba();

			Bitmap bmp = ocvUtil.toBmp(rgba);
			final Mat hist = ocvUtil.calcHistHS(rgba);

			ocvUtil.releaseMat(rgba);
			return Option.from(Tuple2.from(hist, bmp));

		}
		return Option.empty();
	}

	public void initObservar(Mat histograma) {
		stopObservar();
		histogramaEmObservacao = histograma;
	}

	public void stopObservar() {
		OCVUtil.getInstance().releaseMat(histogramaEmObservacao);
		histogramaEmObservacao = null;
	}

	public boolean emObservacao() {
		return histogramaEmObservacao != null;
	}

	public boolean isObservacaoOk() {
		if (histogramaEmObservacao != null) {
			final OCVUtil ocvUtil = OCVUtil.getInstance();

			final Mat atual = realCam.getLastFrame().rgba();
			final Mat histAtual = ocvUtil.calcHistHS(atual);

			// compara o histograma
			final int METODO_COMPARACAO = 1;
			final boolean resultado = Imgproc.compareHist(histAtual,
					histogramaEmObservacao, METODO_COMPARACAO) < 20;

			ocvUtil.releaseMat(atual, histAtual);
			return resultado;
		}
		return false;
	}

	public Cam getRealCam() {
		return realCam;
	}

}

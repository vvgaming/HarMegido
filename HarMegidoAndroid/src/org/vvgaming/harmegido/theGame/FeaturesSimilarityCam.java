package org.vvgaming.harmegido.theGame;

import org.opencv.core.Mat;
import org.vvgaming.harmegido.vision.JavaCam;
import org.vvgaming.harmegido.vision.JavaCameraFrame;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Bitmap;

import com.github.detentor.codex.monads.Option;
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
public class FeaturesSimilarityCam implements
		SimilarityCam<Tuple2<Bitmap, Mat>> {

	private static final float MAGIC_PERCENTAGE = 0.4f;

	private JavaCam realCam = new JavaCam();

	private Mat descsEmObservacao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#snapshot()
	 */
	@Override
	public Tuple2<Bitmap, Mat> snapshot() {

		// sincronizando a classe para garantir o acesso apenas de uma Thread
		// o código nativo apresenta uns erros estranhos ao ser acessado por
		// mais de uma Thread
		// TODO verificar se essa é a melhor forma mesmo, pois parece perigoso
		// ficar trancando o acesso
		synchronized (this) {
			final OCVUtil ocvUtil = OCVUtil.getInstance();

			final Mat rgba = getLastFrame().rgba().clone();
			final Mat gray = getLastFrame().gray().clone();

			Bitmap bmp = ocvUtil.toBmp(rgba);
			final Mat descs = ocvUtil.extractFeatureDescriptors(gray);

			ocvUtil.releaseMat(rgba, gray);
			return Tuple2.from(bmp, descs);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.vvgaming.harmegido.theGame.SimilarityCam#observar(com.github.detentor
	 * .codex.product.Tuple2)
	 */
	@Override
	public void observar(Tuple2<Bitmap, Mat> obs) {
		synchronized (this) {
			stopObservar();
			descsEmObservacao = obs.getVal2();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#stopObservar()
	 */
	@Override
	public void stopObservar() {
		// sincronizando a classe para garantir o acesso apenas de uma Thread
		// o código nativo apresenta uns erros estranhos ao ser acessado por
		// mais de uma Thread
		// TODO verificar se essa é a melhor forma mesmo, pois parece perigoso
		// ficar trancando o acesso
		synchronized (this) {
			OCVUtil.getInstance().releaseMat(descsEmObservacao);
			descsEmObservacao = null;
		}
	}

	//
	// public boolean emObservacao() {
	// return descsEmObservacao != null;
	// }

	// public boolean isObservacaoOk() {
	//
	// if (descsEmObservacao != null) {
	// final OCVUtil ocvUtil = OCVUtil.getInstance();
	//
	// final Mat atualGray = getLastFrame().gray().clone();
	// final Mat descsAtual = ocvUtil.extractFeatureDescriptors(atualGray);
	//
	// // compara os descritores
	// boolean result = ocvUtil.compareDescriptors(descsEmObservacao,
	// descsAtual) > MAGIC_PERCENTAGE;
	//
	// ocvUtil.releaseMat(atualGray, descsAtual);
	// return result;
	// }
	// return false;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#compara()
	 */
	@Override
	public Option<Float> compara() {

		if (descsEmObservacao != null) {
			final OCVUtil ocvUtil = OCVUtil.getInstance();

			final Mat atualGray = getLastFrame().gray().clone();
			final Mat descsAtual = ocvUtil.extractFeatureDescriptors(atualGray);

			// compara os descritores
			float result = ocvUtil.compareDescriptors(descsEmObservacao,
					descsAtual);

			ocvUtil.releaseMat(atualGray, descsAtual);
			return Option.from(result);
		}

		return Option.empty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#connectCamera(int, int)
	 */
	@Override
	public void connectCamera(int width, int height) {
		realCam.connectCamera(width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#disconnectCamera()
	 */
	@Override
	public void disconnectCamera() {
		realCam.disconnectCamera();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#getLastFrame()
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#getHeight()
	 */
	@Override
	public int getHeight() {
		return realCam.getHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#getWidth()
	 */
	@Override
	public int getWidth() {
		return realCam.getWidth();
	}

	@Override
	public boolean isSimilarEnough(float comparacao) {
		return comparacao > MAGIC_PERCENTAGE;
	}

}

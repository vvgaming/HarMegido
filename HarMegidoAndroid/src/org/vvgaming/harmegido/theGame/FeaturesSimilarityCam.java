package org.vvgaming.harmegido.theGame;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.vvgaming.harmegido.util.Constantes;
import org.vvgaming.harmegido.vision.JavaCam;
import org.vvgaming.harmegido.vision.JavaCameraFrame;
import org.vvgaming.harmegido.vision.OCVUtil;

import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Uma implementação de {@link SimilarityCam} que usa uma abordagem simplificada da técnica de extração e comparação de Features
 * 
 * @author Vinicius Nogueira
 */
public class FeaturesSimilarityCam implements SimilarityCam<Tuple2<Mat, Mat>>
{

	private static final float MAGIC_PERCENTAGE = 0.25f;
	private static final int LIMITE_DESCS = 100;

	private final JavaCam realCam = new JavaCam();

	private Mat descsEmObservacao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#snapshot()
	 */
	@Override
	public Option<Tuple2<Mat, Mat>> snapshot()
	{
		synchronized (this)
		{
			final Mat gray = getLastFrame().gray().clone();
			final Tuple2<Mat, Mat> retorno = extrairFeatures(gray);
			if (retorno.getVal2().rows() < LIMITE_DESCS)
			{
				return Option.empty();
			}

			return Option.from(retorno);
		}
	}

	/**
	 * Extrai da imagem como parametro os descritores das features<br/>
	 * 
	 * @param imgGray EM ESCALA DE CINZA
	 * @return uma tupla com a própria imagem do parâmetro e os descritores
	 */
	public Tuple2<Mat, Mat> extrairFeatures(final Mat imgGray)
	{
		final Mat descs = OCVUtil.getInstance().extractFeatureDescriptors(imgGray);
		// reduz a imagem de preview para economizar espaço em memória e banda de transferência
		Imgproc.resize(imgGray, imgGray, new Size(Constantes.camPreviewSize.height, Constantes.camPreviewSize.width));
		Imgproc.blur(imgGray, imgGray, new Size(3, 3));
		return Tuple2.from(imgGray, descs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#observar(com.github.detentor .codex.product.Tuple2)
	 */
	@Override
	public void observar(final Tuple2<Mat, Mat> obs)
	{
		synchronized (this)
		{
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
	public void stopObservar()
	{
		synchronized (this)
		{
			OCVUtil.getInstance().releaseMat(descsEmObservacao);
			descsEmObservacao = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#compara()
	 */
	@Override
	public Option<Float> compara()
	{

		synchronized (this)
		{
			if (descsEmObservacao != null)
			{
				final OCVUtil ocvUtil = OCVUtil.getInstance();

				final Mat atualGray = getLastFrame().gray().clone();
				final Mat descsAtual = ocvUtil.extractFeatureDescriptors(atualGray);

				// compara os descritores
				final float result = ocvUtil.compareDescriptors(descsEmObservacao, descsAtual);

				ocvUtil.releaseMat(atualGray, descsAtual);
				return Option.from(result);
			}
		}

		return Option.empty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#connectCamera(int, int)
	 */
	@Override
	public void connectCamera(final int width, final int height)
	{
		realCam.connectCamera(width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#disconnectCamera()
	 */
	@Override
	public void disconnectCamera()
	{
		realCam.disconnectCamera();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#getLastFrame()
	 */
	@Override
	public JavaCameraFrame getLastFrame()
	{
		synchronized (this)
		{
			return realCam.getLastFrame();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#getHeight()
	 */
	@Override
	public int getHeight()
	{
		return realCam.getHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vvgaming.harmegido.theGame.SimilarityCam#getWidth()
	 */
	@Override
	public int getWidth()
	{
		return realCam.getWidth();
	}

	@Override
	public boolean isSimilarEnough(final float comparacao)
	{
		return comparacao > MAGIC_PERCENTAGE;
	}

}

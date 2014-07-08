package org.vvgaming.harmegido.theGame.objNodes;

import org.opencv.core.Mat;
import org.vvgaming.harmegido.gameEngine.LazyInitGameNode;
import org.vvgaming.harmegido.gameEngine.geometry.MatrizTransfAndroid;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.util.NTimer;
import org.vvgaming.harmegido.theGame.FeaturesSimilarityCam;
import org.vvgaming.harmegido.util.DefaultCamSize;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Camera para encantar e desencantar coisas ;)
 * 
 * @author Vinicius Nogueira
 */
public class NHMEnchantingCam extends LazyInitGameNode
{

	public static final long ENCANTAMENTO_CASTING_TIME = 5000;
	public static final long DESENCANTAMENTO_CASTING_TIME = 5000;
	private FeaturesSimilarityCam cam;
	private Bitmap lastFrame;

	private MatrizTransfAndroid matrizPrincipal;
	private MatrizTransfAndroid matrizPreview;

	private Status status = Status.OCIOSO;

	// variáveis de controle para o desencantamento
	private Option<Function0<Void>> callbackFound = Option.empty();
	private Option<Function1<Boolean, Void>> callbackFimDesencantamento = Option.empty();
	private Option<Tuple2<Mat, Mat>> desencantando = Option.empty();
	boolean desencantandoFound = false;

	// variáveis de controle para o encantamento
	private Option<Function1<Option<Tuple2<Mat, Mat>>, Void>> callbackFimEncantamento = Option.empty();
	//
	private NTimer timer;

	public NHMEnchantingCam(final Ponto center, final float height)
	{
		addToInit(new Function0<Void>()
		{
			@Override
			public Void apply()
			{
				NHMEnchantingCam.this.matrizPrincipal.setCenter(center);
				NHMEnchantingCam.this.matrizPrincipal.setHeight(height, true);
				NHMEnchantingCam.this.matrizPreview.setCenter(center);
				NHMEnchantingCam.this.matrizPreview.setHeight(height, true);
				return null;
			}
		});
	}

	@Override
	public void preInit()
	{
		if (cam == null)
		{
			cam = new FeaturesSimilarityCam();
			// cam = new HistogramaSimilarityCam();
		}

		cam.connectCamera((int) DefaultCamSize.normalSize.width, (int) DefaultCamSize.normalSize.height);
		matrizPrincipal = new MatrizTransfAndroid((float) DefaultCamSize.normalSize.height, (float) DefaultCamSize.normalSize.width);
		matrizPreview = new MatrizTransfAndroid((float) DefaultCamSize.previewSize.height, (float) DefaultCamSize.previewSize.width);
	}

	@Override
	public void update(final long delta)
	{
		// captura e guarda o frame
		final Mat lastFrameMat = cam.getLastFrame().rgba().clone();
		lastFrame = OCVUtil.getInstance().toBmp(lastFrameMat);
		OCVUtil.getInstance().releaseMat(lastFrameMat);

		if (status.equals(Status.ENCANTANDO))
		{
			final Option<Float> comparacao = cam.compara();
			if (comparacao.notEmpty() && !cam.isSimilarEnough(comparacao.get()))
			{
				// se sair do foco, para de encatar
				callbackFimEncantamento.get().apply(Option.<Tuple2<Mat, Mat>> empty());
				parar();
			}
		}

		if (status.equals(Status.DESENCANTANDO))
		{
			final Option<Float> comparacao = cam.compara();
			if (comparacao.notEmpty())
			{
				if (desencantandoFound)
				{
					// já estamos desencantando, vamos ver se não saiu da mira,
					// se sair cancela
					if (!cam.isSimilarEnough(comparacao.get()))
					{
						NHMEnchantingCam.this.callbackFimDesencantamento.get().apply(false);
						desencantandoFound = false;
						timer.kill();
					}
				}
				else
				{
					// não tinha encontrado, mas acabamos de achar um igual
					if (cam.isSimilarEnough(comparacao.get()))
					{
						// avisa que achou
						callbackFound.get().apply();
						desencantandoFound = true;
						// lança o timer para que ele mantenha o dispositivo
						// quieto enquanto desencanta
						timer = new NTimer(DESENCANTAMENTO_CASTING_TIME, new Function0<Void>()
						{
							@Override
							public Void apply()
							{
								// ao terminar, veja se ainda estamos
								// desencantando e ainda o objeto ainda
								// está no foco
								if (status.equals(Status.DESENCANTANDO) && desencantandoFound)
								{
									NHMEnchantingCam.this.callbackFimDesencantamento.get().apply(true);
									desencantandoFound = false;
								}
								return null;
							}
						}, true);
						addSubNode(timer);
					}
				}
			}
		}
	}

	@Override
	public void render(final Canvas canvas)
	{
		final Paint alphed = new Paint();
		alphed.setAlpha(100);

		final Paint natural = new Paint();
		canvas.drawBitmap(lastFrame, matrizPrincipal.getMatrix(), natural);

		if (desencantando.notEmpty())
		{
			final Bitmap desFrame = OCVUtil.getInstance().toBmp(desencantando.get().getVal1());
			canvas.drawBitmap(desFrame, matrizPreview.getMatrix(), alphed);
		}
	}

	@Override
	public boolean isDead()
	{
		return false;
	}

	@Override
	public void end()
	{
		cam.disconnectCamera();
	}

	private enum Status
	{
		OCIOSO, ENCANTANDO, DESENCANTANDO;
	}

	public void iniciaEncantamento(final Function1<Option<Tuple2<Mat, Mat>>, Void> callbackFimEncantamento)
	{
		if (callbackFimEncantamento == null)
		{
			throw new IllegalArgumentException("callback não pode ser nulo");
		}

		parar();

		this.callbackFimEncantamento = Option.from(callbackFimEncantamento);
		final Option<Tuple2<Mat, Mat>> registrado = cam.snapshot();
		if (registrado.notEmpty())
		{
			status = Status.ENCANTANDO;
			cam.observar(registrado.get());
			timer = new NTimer(ENCANTAMENTO_CASTING_TIME, new Function0<Void>()
			{
				@Override
				public Void apply()
				{
					if (status.equals(Status.ENCANTANDO))
					{
						// se ainda está encantando após o delay é pq
						// não perdeu o alvo no meio do caminho
						// então vamos retornar OK, pq encantou tudo bem
						NHMEnchantingCam.this.callbackFimEncantamento.get().apply(registrado);
						parar();
					}
					return null;
				}
			}, true);
			addSubNode(timer);
		}
		else
		{
			if (this.callbackFimEncantamento.notEmpty())
			{
				this.callbackFimEncantamento.get().apply(Option.<Tuple2<Mat, Mat>> empty());
			}
		}
	}

	public void iniciaDesencantamento(final Tuple2<Mat, Mat> toDesencantar, final Function0<Void> callbackFound,
			final Function1<Boolean, Void> callbackFimDesencantamento)
	{

		if (callbackFound == null || callbackFimDesencantamento == null)
		{
			throw new IllegalArgumentException("callbacks não podem ser nulo");
		}

		parar();

		this.callbackFound = Option.from(callbackFound);
		this.callbackFimDesencantamento = Option.from(callbackFimDesencantamento);

		status = Status.DESENCANTANDO;
		cam.observar(toDesencantar);

		desencantando = Option.from(toDesencantar);

	}

	public boolean parar()
	{
		if (!status.equals(Status.OCIOSO))
		{
			cam.stopObservar();
			status = Status.OCIOSO;
			this.callbackFimEncantamento = Option.empty();
			this.callbackFimDesencantamento = Option.empty();
			this.callbackFound = Option.empty();
			this.desencantando = Option.empty();
			this.desencantandoFound = false;
			if (timer != null)
			{
				timer.kill();
			}
			return true;
		}
		return false;
	}

}

package org.vvgaming.harmegido.theGame.objNodes;

import org.opencv.core.Mat;
import org.vvgaming.harmegido.gameEngine.LazyInitGameNode;
import org.vvgaming.harmegido.gameEngine.geometry.MatrizTransfAndroid;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.util.NTimer;
import org.vvgaming.harmegido.theGame.FeaturesSimilarityCam;
import org.vvgaming.harmegido.theGame.SimilarityCam;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
	private SimilarityCam<Tuple2<Bitmap, Mat>> cam;
	private Bitmap lastFrame;

	MatrizTransfAndroid matriz;

	// private Option<Tuple2<Bitmap, Mat>> registrado = Option.empty();
	// private Option<Float> comparacao = Option.empty();

	private Status status = Status.OCIOSO;

	private Option<Function1<Boolean, Void>> callbackFimEncantamento = Option.empty();
	private NTimer encantamentoTimer;

	public NHMEnchantingCam(final Ponto center, final float height)
	{
		addToInit(new Function0<Void>()
		{
			@Override
			public Void apply()
			{
				NHMEnchantingCam.this.matriz.setCenter(center);
				NHMEnchantingCam.this.matriz.setHeight(height, true);
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
		cam.connectCamera(640, 480);
		matriz = new MatrizTransfAndroid(480, 640);
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
			if (!cam.isSimilarEnough(cam.compara().get()))
			{
				// se sair do foco, para de encatar
				callbackFimEncantamento.get().apply(false);
				pararEncantamento();
			}
		}

	}

	@Override
	public void render(final Canvas canvas)
	{
		// final Paint alphed = new Paint();
		// alphed.setAlpha(100);

		final Paint natural = new Paint();
		final Matrix matrix = matriz.getMatrix();
		canvas.drawBitmap(lastFrame, matrix, natural);

		// if (registrado.notEmpty())
		// {
		// final Bitmap regFrame = registrado.get().getVal1();
		// canvas.drawBitmap(regFrame, matrix, alphed);
		// }
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

	public void iniciaEncantamento(final Function1<Boolean, Void> callbackFimEncantamento)
	{
		if (callbackFimEncantamento == null)
		{
			throw new IllegalArgumentException("callback não pode ser nulo");
		}

		pararEncantamento();

		this.callbackFimEncantamento = Option.from(callbackFimEncantamento);
		final Option<Tuple2<Bitmap, Mat>> registrado = cam.snapshot();
		if (registrado.notEmpty())
		{
			status = Status.ENCANTANDO;
			cam.observar(registrado.get());
			encantamentoTimer = new NTimer(ENCANTAMENTO_CASTING_TIME, new Function0<Void>()
			{
				@Override
				public Void apply()
				{
					if (status.equals(Status.ENCANTANDO))
					{
						// se ainda está encantando após o delay é pq não perdeu o alvo no meio do caminho
						// então vamos retornar OK, pq encantou tudo bem
						NHMEnchantingCam.this.callbackFimEncantamento.get().apply(true);
						pararEncantamento();
					}
					return null;
				}
			}, true);
			addSubNode(encantamentoTimer);
		}
		else
		{
			if (this.callbackFimEncantamento.notEmpty())
			{
				this.callbackFimEncantamento.get().apply(false);
			}
		}
	}

	public void pararEncantamento()
	{
		cam.stopObservar();
		status = Status.OCIOSO;
		this.callbackFimEncantamento = Option.empty();
		if (encantamentoTimer != null)
			encantamentoTimer.kill();
	}

}

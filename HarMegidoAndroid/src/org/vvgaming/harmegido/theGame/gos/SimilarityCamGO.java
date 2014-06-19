package org.vvgaming.harmegido.theGame.gos;

import org.opencv.core.Mat;
import org.vvgaming.harmegido.gameEngine.LazyInitGameNode;
import org.vvgaming.harmegido.gameEngine.geometry.MatrizTransfAndroid;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.theGame.FeaturesSimilarityCam;
import org.vvgaming.harmegido.theGame.HistogramaSimilarityCam;
import org.vvgaming.harmegido.theGame.SimilarityCam;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * GameObject para plotar imagens da {@link HistogramaSimilarityCam} em um jogo
 * 
 * @author Vinicius Nogueira
 */
public class SimilarityCamGO extends LazyInitGameNode {

	private SimilarityCam<Tuple2<Bitmap, Mat>> cam;
	private Bitmap lastFrame;

	// contagem de frames para pular na compara��o (para ganhar performance)
	private int skipCount = 0;
	private final int SKIP_LIMIT = 3;

	MatrizTransfAndroid matriz;

	private Option<Tuple2<Bitmap, Mat>> registrado = Option.empty();
	private Option<Float> comparacao = Option.empty();

	public SimilarityCamGO(final Ponto center, final float width) {
		addToInit(new Function0<Void>() {
			@Override
			public Void apply() {
				SimilarityCamGO.this.matriz.setCenter(center);
				SimilarityCamGO.this.matriz.setWidthKeepingRatio(width);
				return null;
			}
		});
	}

	@Override
	public void preInit() {
		if (cam == null) {
			cam = new FeaturesSimilarityCam();
//			cam = new HistogramaSimilarityCam();
		}
		cam.connectCamera(640, 480);
		matriz = new MatrizTransfAndroid(480, 640);
	}

	@Override
	public void update(long delta) {
		final Mat lastFrameMat = cam.getLastFrame().rgba().clone();
		lastFrame = OCVUtil.getInstance().toBmp(lastFrameMat);
		OCVUtil.getInstance().releaseMat(lastFrameMat);

		skipCount++;

		if (skipCount > SKIP_LIMIT) {
			comparacao = cam.compara();
			skipCount = 0;
		}

	}

	@Override
	public void render(Canvas canvas) {
		Paint alphed = new Paint();
		alphed.setAlpha(100);
		Paint natural = new Paint();
		final Matrix matrix = matriz.getMatrix();
		canvas.drawBitmap(lastFrame, matrix, natural);
		if (registrado.notEmpty()) {
			final Bitmap regFrame = registrado.get().getVal1();
			canvas.drawBitmap(regFrame, matrix, alphed);
		}
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public void end() {
		cam.disconnectCamera();
	}

	public void onClick() {

		registrado = Option.from(cam.snapshot());
		if (!registrado.isEmpty()) {
			cam.observar(registrado.get());
		}

	}

	public boolean isOkResultado() {
		if (comparacao.notEmpty()) {
			return cam.isSimilarEnough(comparacao.get());
		} else {
			return false;
		}
	}

	@Override
	public boolean isVisible() {
		return true;
	}

}

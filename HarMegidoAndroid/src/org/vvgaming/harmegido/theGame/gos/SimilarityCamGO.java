package org.vvgaming.harmegido.theGame.gos;

import org.opencv.core.Mat;
import org.vvgaming.harmegido.gameEngine.LazyInitGameObject;
import org.vvgaming.harmegido.gameEngine.geometry.MatrizTransfAndroid;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
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
 * GameObject para plotar imagens da {@link SimilarityCam} em um jogo
 * 
 * @author Vinicius Nogueira
 */
public class SimilarityCamGO extends LazyInitGameObject {

	private SimilarityCam cam;
	private Bitmap lastFrame;
//	private Ponto center;

	MatrizTransfAndroid matriz;

	private Option<Tuple2<Mat, Bitmap>> registrado = Option.empty();
	private double comparacao = Double.MAX_VALUE;

	public SimilarityCamGO(final Ponto center, final float width,
			final float height) {
		addToInit(new Function0<Void>() {
			@Override
			public Void apply() {
				SimilarityCamGO.this.matriz.setCenter(center);
				SimilarityCamGO.this.matriz.setDimensions(width, height);
				return null;
			}
		});
	}

	@Override
	public void render(Canvas canvas) {
		Paint alphed = new Paint();
		alphed.setAlpha(100);
		Paint natural = new Paint();
		final Matrix matrix = matriz.getMatrix();
		canvas.drawBitmap(lastFrame, matrix, natural);
		if (!registrado.isEmpty()) {
			final Bitmap regFrame = registrado.get().getVal2();
			canvas.drawBitmap(regFrame, matrix, alphed);
		}
	}

	@Override
	public void update(long delta) {
		final Mat lastFrameMat = cam.getLastFrame().rgba().clone();
		lastFrame = OCVUtil.getInstance().toBmp(lastFrameMat);
		OCVUtil.getInstance().releaseMat(lastFrameMat);

		comparacao = cam.compara();
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public void preInit() {
		if (cam == null) {
			cam = new SimilarityCam();
		}
		cam.connectCamera(640, 480);
		matriz = new MatrizTransfAndroid(480, 640);
	}

	@Override
	public void end() {
		cam.disconnectCamera();
	}

	public void onClick() {
		registrado = Option.from(cam.register());
		if (!registrado.isEmpty()) {
			cam.initObservar(registrado.get().getVal1());
		}

	}

	public boolean isOkResultado() {
		return comparacao < SimilarityCam.EQUALITY_THRESHOLD;
	}

	public double getComparacao() {
		return comparacao;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

}

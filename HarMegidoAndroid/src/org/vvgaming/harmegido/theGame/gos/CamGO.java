package org.vvgaming.harmegido.theGame.gos;

import org.opencv.core.Mat;
import org.vvgaming.harmegido.gameEngine.GameObject;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.theGame.SimilarityCam;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Teste de GameObject que recupera imagens da camera OpenCV e desenha no Canvas
 * 
 * @author Vinicius Nogueira
 */
public class CamGO implements GameObject {

	private Paint verde = new Paint();
	private Paint vermelho = new Paint();

	private SimilarityCam cam;
	private Bitmap lastFrame;
	private Ponto pos;
	private Ponto center;

	private Option<Tuple2<Mat, Bitmap>> registrado = Option.empty();
	private double comparacao = Double.MAX_VALUE;

	public CamGO() {
		this.center = new Ponto(0, 0);
	}

	public CamGO(Ponto center) {
		this.center = center;
		verde.setARGB(100, 255, 0, 0);
		vermelho.setARGB(100, 0, 255, 0);
	}

	@Override
	public void render(Canvas canvas) {
		Paint alphed = new Paint();
		alphed.setAlpha(100);
		Paint natural = new Paint();
		canvas.drawBitmap(lastFrame, pos.x, pos.y, natural);
		if (!registrado.isEmpty()) {
			final Bitmap regFrame = registrado.get().getVal2();
			canvas.drawBitmap(regFrame, pos.x, pos.y, alphed);
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
	public void init() {
		if (cam == null) {
			cam = new SimilarityCam();
		}
		cam.connectCamera(640, 480);
		pos = new Ponto(center.x - cam.getWidth() / 2, center.y
				- cam.getHeight() / 2);

	}

	@Override
	public void end() {
		cam.disconnectCamera();
	}

	public void setCenter(Ponto center) {
		this.center = center;
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

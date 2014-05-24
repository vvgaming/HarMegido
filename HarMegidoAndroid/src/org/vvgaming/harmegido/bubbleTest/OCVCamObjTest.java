package org.vvgaming.harmegido.bubbleTest;

import org.vvgaming.harmegido.gameEngine.GameObject;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.vision.Cam;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Teste de GameObject que recupera imagens da camera OpenCV e desenha no Canvas
 * 
 * @author Vinicius Nogueira
 */
public class OCVCamObjTest implements GameObject {

	private Cam cam;
	private Bitmap lastFrame;
	private Ponto pos;
	private Ponto center;

	public OCVCamObjTest(Ponto center) {
		this.center = center;
	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawBitmap(lastFrame, pos.x, pos.y, null);
	}

	@Override
	public void update(long delta) {
		lastFrame = OCVUtil.toBmp(cam.getLastFrame().rgba());
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public void init() {
		if (cam == null) {
			cam = new Cam();
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

}

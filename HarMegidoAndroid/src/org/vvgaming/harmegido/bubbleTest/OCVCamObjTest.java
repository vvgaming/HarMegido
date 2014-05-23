package org.vvgaming.harmegido.bubbleTest;

import org.vvgaming.harmegido.gameEngine.GameObject;
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

	public OCVCamObjTest() {

	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawBitmap(lastFrame, 0, 0, null);
	}

	@Override
	public void update(long delta) {
		if (cam == null) {
			cam = new Cam();
			cam.connectCamera(640, 480);
		}
		lastFrame = OCVUtil.toBmp(cam.getLastFrame().rgba());
	}

	@Override
	public boolean isDead() {
		return false;
	}

}

package org.vvgaming.harmegido.test.bubbleTest;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.util.Constantes;
import org.vvgaming.harmegido.vision.OCVUtil;
import org.vvgaming.harmegido.vision.old.OldNativeCam;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Teste de GameObject que recupera imagens da camera OpenCV e desenha no Canvas
 * 
 * @author Vinicius Nogueira
 */
public class OCVCamObjTest extends GameNode
{

	private OldNativeCam cam;
	private Bitmap lastFrame;
	private Ponto pos;
	private Ponto center;

	public OCVCamObjTest(Ponto center)
	{
		this.center = center;
	}

	@Override
	public void render(Canvas canvas)
	{
		canvas.drawBitmap(lastFrame, pos.x, pos.y, null);
	}

	@Override
	public void update(long delta)
	{
		final Mat lastFrameMatClone = cam.getFrame().rgba().clone();
		lastFrame = OCVUtil.getInstance().toBmp(lastFrameMatClone);
		OCVUtil.getInstance().releaseMat(lastFrameMatClone);
	}

	@Override
	public void init()
	{
		if (cam == null)
		{
			cam = new OldNativeCam();
		}
		Size size = Constantes.camNormalSize;
		cam.connectCamera((int) size.width, (int) size.height);
		pos = new Ponto(center.x - cam.getWidth() / 2, center.y - cam.getHeight() / 2);

	}

	@Override
	public void end()
	{
		cam.disconnectCamera();
	}

	public void setCenter(Ponto center)
	{
		this.center = center;
	}

}

package org.vvgaming.harmegido.gameEngine.nodes;

import org.vvgaming.harmegido.gameEngine.GameNode;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Botão com imagem
 * 
 * @author Vinicius Nogueira
 */
public class NButtonImage extends GameNode {

	private final NImage image;

	public NButtonImage(NImage image) {
		super();
		this.image = image;
	}

	@Override
	public void init() {
		super.init();
		addSubNode(image);
	}

	@Override
	protected void update(long delta) {
	}

	@Override
	protected void render(Canvas canvas) {
		super.render(canvas);
		final Paint green = new Paint();
		green.setARGB(255, 0, 200, 0);
		canvas.drawRect(image.getBoundingRect(), green);
	}

	@Override
	public boolean onTouch(MotionEvent event) {
		if (image.getBoundingRect().contains(event.getX(), event.getY())) {
			System.out.println("opaa");
			return true;
		}
		return false;
	}

	public NImage getImage() {
		return image;
	}

}

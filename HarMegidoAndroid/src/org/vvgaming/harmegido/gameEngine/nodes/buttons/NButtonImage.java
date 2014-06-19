package org.vvgaming.harmegido.gameEngine.nodes.buttons;

import org.vvgaming.harmegido.gameEngine.nodes.NImage;

import android.graphics.RectF;

/**
 * Botão com imagem
 * 
 * @author Vinicius Nogueira
 */
public class NButtonImage extends NButton {

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

	public NImage getImage() {
		return image;
	}

	@Override
	public RectF getBoundingRect() {
		return image.getBoundingRect();
	}

}

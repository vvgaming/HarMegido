package org.vvgaming.harmegido.gameEngine.nodes.fx;

import org.vvgaming.harmegido.gameEngine.GameNode;

import android.graphics.Paint;

/**
 * {@link GameObject} que aplica um efeito de fade in/out a uma {@link Paint}; <br/>
 * Essa implementação diminui ou aumenta o ALPHA ao longo do tempo. <br/>
 * Útil para fazer o efeito de fade em imagens ou textos e etc...
 * 
 * @author Vinicius Nogueira
 */
public class NPaintFade extends GameNode {

	private final Paint paint;
	private final boolean in;
	private final long duration;
	private final float speed;

	private long elapsed = 0;

	public NPaintFade(Paint paint, boolean in, long duration) {
		super();
		this.paint = paint;
		this.in = in;
		this.duration = duration;
		this.speed = 255.0f / duration;
	}

	@Override
	protected void init() {
		super.init();
		paint.setAlpha(in ? 0 : 255);
	}

	@Override
	protected void update(long delta) {
		elapsed += delta;
		int factor = in ? 1 : -1;
		paint.setAlpha(paint.getAlpha() + ((int) (factor * (speed * delta))));
	}

	@Override
	protected void end() {
		paint.setAlpha(in ? 255 : 0);
	}

	@Override
	public boolean isDead() {
		return elapsed >= duration;
	}

}

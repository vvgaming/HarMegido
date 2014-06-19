package org.vvgaming.harmegido.gameEngine.nodes;

import com.github.detentor.codex.function.Function0;

public class NTextBlinking extends NText {

	private final static long DEFAULT_TIMER_LIMIT = 500;
	private NTimer timer;

	public NTextBlinking() {
		super();
	}

	public NTextBlinking(int x, int y, String texto) {
		super(x, y, texto);
	}

	@Override
	public void init() {
		super.init();
		timer = new NTimer(DEFAULT_TIMER_LIMIT, new Function0<Void>() {
			@Override
			public Void apply() {
				setVisible(!isVisible());
				return null;
			}
		});
		addSubNode(timer);
	}

	public void setTimeLimit(long timeLimit) {
		timer.setTimeLimit(timeLimit);
	}

}

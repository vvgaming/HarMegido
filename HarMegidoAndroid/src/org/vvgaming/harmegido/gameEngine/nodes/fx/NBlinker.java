package org.vvgaming.harmegido.gameEngine.nodes.fx;

import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.util.NTimer;

import com.github.detentor.codex.function.Function0;

/**
 * Uma implentaçãao de {@link NText} que pisca
 * 
 * @author Vinicius Nogueira
 */
public class NBlinker extends GameNode {

	private final static long DEFAULT_TIMER_LIMIT = 500;
	private final GameNode node;

	private NTimer timer;

	public NBlinker(final GameNode node) {
		super();
		this.node = node;
	}

	@Override
	public void init() {
		super.init();
		timer = new NTimer(DEFAULT_TIMER_LIMIT, new Function0<Void>() {
			@Override
			public Void apply() {
				node.setVisible(!node.isVisible());
				return null;
			}
		});
		addSubNode(timer);
	}

	public void setTimeLimit(long timeLimit) {
		timer.setTimeLimit(timeLimit);
	}

	@Override
	protected void update(long delta) {

	}

}

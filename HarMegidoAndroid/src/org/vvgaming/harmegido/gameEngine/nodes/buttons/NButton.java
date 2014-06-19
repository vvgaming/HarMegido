package org.vvgaming.harmegido.gameEngine.nodes.buttons;

import org.vvgaming.harmegido.gameEngine.GameNode;

import android.graphics.RectF;
import android.view.MotionEvent;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.monads.Option;

/**
 * Botão
 * 
 * @author Vinicius Nogueira
 */
public abstract class NButton extends GameNode {

	private Option<Function0<Void>> onClickFunction = Option.empty();

	public NButton() {
		super();
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected void update(long delta) {
	}

	@Override
	public boolean onTouch(MotionEvent event) {
		if (onClickFunction.notEmpty()
				&& event.getAction() == MotionEvent.ACTION_DOWN
				&& getBoundingRect().contains(event.getX(), event.getY())) {
			onClickFunction.get().apply();
			return true;
		}
		return false;
	}

	public abstract RectF getBoundingRect();

	public void setOnClickFunction(Function0<Void> onClickFunction) {
		this.onClickFunction = Option.from(onClickFunction);
	}

}

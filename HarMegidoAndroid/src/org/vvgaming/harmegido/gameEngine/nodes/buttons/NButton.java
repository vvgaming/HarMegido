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
public abstract class NButton extends GameNode
{

	private Option<Function0<Void>> onClickFunction = Option.empty();
	private boolean visible = true;

	public NButton()
	{
		super();
	}

	@Override
	protected void update(final long delta)
	{
	}

	@Override
	public boolean onTouch(final MotionEvent event)
	{
		if (onClickFunction.notEmpty() && event.getAction() == MotionEvent.ACTION_DOWN
				&& getBoundingRect().contains(event.getX(), event.getY()))
		{
			onClickFunction.get().apply();
			return true;
		}
		return false;
	}

	@Override
	public boolean isVisible()
	{
		return visible;
	}

	@Override
	public void setVisible(final boolean visible)
	{
		this.visible = visible;
	}

	public abstract RectF getBoundingRect();

	public void setOnClickFunction(final Function0<Void> onClickFunction)
	{
		this.onClickFunction = Option.from(onClickFunction);
	}

}

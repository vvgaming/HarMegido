package org.vvgaming.harmegido.gameEngine.nodes.buttons;

import org.vvgaming.harmegido.gameEngine.GameNode;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.github.detentor.codex.function.Function0;

/**
 * Botão de seleção
 * 
 * @author Vinicius Nogueira
 */
public class NToggleButton extends GameNode
{

	private final NButton button;

	private RectF boundingBox;
	private float padding = 8.0f;
	private boolean toggled = false;
	private final Paint paint;

	public NToggleButton(NButton button)
	{
		super();
		this.button = button;
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setARGB(255, 255, 255, 255);
		paint.setStrokeWidth(6);
	}

	@Override
	public void init()
	{
		super.init();

		// TODO nem lembro pq coloquei isso aqui, mas estava dando pau e comentei...
		// quando lembrar o pq, tenho que mover para outro lugar pois esse trecho está sobrescrevendo as funções adicionadas antes do init e
		// isso não é bom
		// setOnClickFunction(new Function0<Void>() {
		// @Override
		// public Void apply() {
		// return null;
		// }
		// });
		addSubNode(button);
	}

	@Override
	protected void update(long delta)
	{
		boundingBox = getBoundingRect();
		boundingBox.left -= padding;
		boundingBox.top -= padding;
		boundingBox.bottom += padding;
		boundingBox.right += padding;
	}

	@Override
	protected void render(Canvas canvas)
	{
		super.render(canvas);
		if (toggled)
		{
			canvas.drawRect(boundingBox, paint);
		}
	}

	public void setOnClickFunction(final Function0<Void> onClickFunction)
	{
		button.setOnClickFunction(new Function0<Void>()
		{

			@Override
			public Void apply()
			{
				toggle();
				onClickFunction.apply();
				return null;
			}
		});
	}

	public void toggle()
	{
		toggled = !toggled;
	}

	public void toggle(boolean toggled)
	{
		this.toggled = toggled;
	}

	public RectF getBoundingRect()
	{
		return button.getBoundingRect();
	}

	public NButton getButton()
	{
		return button;
	}

	public Paint getBoundingRectPaint()
	{
		return paint;
	}

}

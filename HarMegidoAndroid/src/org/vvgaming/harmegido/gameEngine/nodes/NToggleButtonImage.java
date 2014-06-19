package org.vvgaming.harmegido.gameEngine.nodes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.github.detentor.codex.function.Function0;

/**
 * Botão de seleção com imagem
 * 
 * @author Vinicius Nogueira
 */
public class NToggleButtonImage extends NButtonImage {

	private RectF boundingBox;
	private float padding = 8.0f;
	private boolean toggled = false;
	private Paint paint;

	public NToggleButtonImage(final NImage image) {
		super(image);
	}

	@Override
	public void init() {
		super.init();
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setARGB(255, 255, 255, 255);
		paint.setStrokeWidth(6);
		setOnClickFunction(new Function0<Void>() {
			@Override
			public Void apply() {
				return null;
			}
		});
	}

	@Override
	protected void update(long delta) {
		super.update(delta);
		boundingBox = getImage().getBoundingRect();
		boundingBox.left -= padding;
		boundingBox.top -= padding;
		boundingBox.bottom += padding;
		boundingBox.right += padding;
	}

	@Override
	protected void render(Canvas canvas) {
		super.render(canvas);
		if (toggled) {
			canvas.drawRect(boundingBox, paint);
		}
	}

	@Override
	public void setOnClickFunction(final Function0<Void> onClickFunction) {
		super.setOnClickFunction(new Function0<Void>() {

			@Override
			public Void apply() {
				toggle();
				onClickFunction.apply();
				return null;
			}
		});
	}

	public void toggle() {
		toggled = !toggled;
	}

	public void toggle(boolean toggled) {
		this.toggled = toggled;
	}

}

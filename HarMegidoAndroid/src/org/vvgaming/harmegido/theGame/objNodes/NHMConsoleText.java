package org.vvgaming.harmegido.theGame.objNodes;

import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.NTimer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.github.detentor.codex.function.Function0;

public class NHMConsoleText extends NText
{
	private final long timer;

	private Paint rectPaint;
	private Rect bgRect;

	public NHMConsoleText(final String text, final long timer)
	{
		super(0, 0, text);
		this.timer = timer;
	}

	@Override
	public void init()
	{
		super.init();

		rectPaint = new Paint();
		rectPaint.setARGB(140, 100, 100, 100);
		bgRect = new Rect(0, getGameHeight() - (int) (getSmallFontSize() * 1.3f), getGameWidth(), getGameHeight());

		pos = new Ponto(getGameWidth(.5f), getGameHeight());
		face = Typeface.createFromAsset(getGameAssetManager().getAndroidAssets(), "fonts/Radio Trust.ttf");
		paint.setTextAlign(Align.CENTER);
		vAlign = VerticalAlign.MIDDLE;

		addSubNode(new NTimer(timer, new Function0<Void>()
		{
			@Override
			public Void apply()
			{
				NHMConsoleText.this.kill();
				return null;
			}
		}, true));
	}

	@Override
	public void update(long delta)
	{
		super.update(delta);
	}

	@Override
	public void render(Canvas canvas)
	{
		canvas.drawRect(bgRect, rectPaint);
		super.render(canvas);
	}

}

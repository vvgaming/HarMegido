package org.vvgaming.harmegido.gameEngine.nodes.buttons;

import org.vvgaming.harmegido.gameEngine.nodes.NText;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Botão com texto
 * 
 * @author Vinicius Nogueira
 */
public class NButtonText extends NButton
{

	private final NText texto;

	public NButtonText(final NText texto)
	{
		super();
		this.texto = texto;
	}

	@Override
	public void init()
	{
		super.init();
		addSubNode(texto);
	}

	@Override
	protected void update(final long delta)
	{
	}

	@Override
	protected void render(final Canvas canvas)
	{
		super.render(canvas);

		// TODO RETIRAR ESSE TESTE ABAIXO
		final Paint paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		canvas.drawRect(getBoundingRect(), paint);
	}

	public NText getTexto()
	{

		return texto;
	}

	@Override
	public RectF getBoundingRect()
	{
		final Rect bounds = new Rect();
		texto.paint.getTextBounds(texto.text, 0, texto.text.length(), bounds);

		// TODO Corrigir esse calculo...
		bounds.set((int) texto.pos.x, (int) texto.pos.y, (int) (texto.pos.x + bounds.width()), (int) (texto.pos.y + bounds.height()));
		return new RectF(bounds);

	}

}

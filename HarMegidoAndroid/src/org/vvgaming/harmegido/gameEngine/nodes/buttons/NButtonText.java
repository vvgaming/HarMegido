package org.vvgaming.harmegido.gameEngine.nodes.buttons;

import org.vvgaming.harmegido.gameEngine.nodes.NText;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Botão com texto
 * 
 * @author Vinicius Nogueira
 */
public class NButtonText extends NButton
{

	private final float percentBoundingBoxPadding = .07f;
	private final NText texto;

	public NButtonText(final NText texto)
	{
		super();
		if (texto.text.contains("\n"))
		{
			throw new IllegalArgumentException("o NButtonText não suporta textos com quebra de linha: " + texto.text);
		}
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

		float boundsX0;
		switch (texto.paint.getTextAlign())
		{
		case LEFT:
			boundsX0 = texto.pos.x;
			break;
		case CENTER:
			boundsX0 = texto.pos.x - bounds.width() / 2;
			break;
		case RIGHT:
			boundsX0 = texto.pos.x - bounds.width();
			break;
		default:
			throw new IllegalArgumentException("Alinhamento de texto desconhecido: " + texto.paint.getTextAlign());
		}
		float boundsY0;
		switch (texto.vAlign)
		{
		case BOTTOM:
			boundsY0 = texto.pos.y - bounds.height();
			break;
		case MIDDLE:
			boundsY0 = texto.pos.y - (3.0f * bounds.height()) / 2.0f;
			break;
		default:
			throw new IllegalArgumentException("Alinhamento vertical de texto desconhecido: " + texto.vAlign);
		}

		float boundsX1 = boundsX0 + bounds.width();
		float boundsY1 = boundsY0 + bounds.height();

		boundsX0 -= bounds.width() * percentBoundingBoxPadding;
		boundsY0 -= bounds.height() * percentBoundingBoxPadding;
		boundsX1 += bounds.width() * percentBoundingBoxPadding;
		boundsY1 += bounds.height() * percentBoundingBoxPadding;

		return new RectF(boundsX0, boundsY0, boundsX1, boundsY1);

	}
}

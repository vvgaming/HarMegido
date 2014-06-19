package org.vvgaming.harmegido.gameEngine.nodes;

import org.vvgaming.harmegido.gameEngine.GameNode;

import android.view.MotionEvent;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.monads.Option;

/**
 * Botão com imagem
 * 
 * @author Vinicius Nogueira
 */
public class NButtonImage extends GameNode {

	private final NImage image;
	private Option<Function0<Void>> onClickFunction = Option.empty();

	public NButtonImage(NImage image) {
		super();
		this.image = image;
	}

	@Override
	public void init() {
		super.init();
		addSubNode(image);
	}

	@Override
	protected void update(long delta) {
	}

	@Override
	public boolean onTouch(MotionEvent event) {
		if (onClickFunction.notEmpty()
				&& event.getAction() == MotionEvent.ACTION_DOWN
				&& image.getBoundingRect().contains(event.getX(), event.getY())) {
			onClickFunction.get().apply();
			return true;
		}
		return false;
	}

	public NImage getImage() {
		return image;
	}

	public void setOnClickFunction(Function0<Void> onClickFunction) {
		this.onClickFunction = Option.from(onClickFunction);
	}

}

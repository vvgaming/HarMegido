package org.vvgaming.harmegido.gameEngine.gos;

import org.vvgaming.harmegido.gameEngine.GameObject;

import android.graphics.Canvas;

import com.github.detentor.codex.function.Function0;

/**
 * {@link GameObject} bem simples que serve para contar tempo para realizar
 * determinadas ações
 * 
 * @author Vinicius Nogueira
 */
public class TimerGO implements GameObject {

	private long counter = 0;
	private long timeLimit = 0;
	private long cycles = 0;
	private final boolean onTimeOnly;
	private final Function0<Void> callback;

	public TimerGO(final long timeLimit, Function0<Void> callback,
			final boolean onTimeOnly) {
		super();
		this.onTimeOnly = onTimeOnly;
		this.timeLimit = timeLimit;
		this.callback = callback;
	}

	public TimerGO(final long timeLimit, Function0<Void> callback) {
		this(timeLimit, callback, false);
	}

	@Override
	public void init() {

	}

	@Override
	public void update(long delta) {
		counter += delta;
		if (counter >= timeLimit) {
			callback.apply();
			counter = (counter % timeLimit);
			cycles++;
		}
	}

	@Override
	public void render(Canvas canvas) {
		// sempre invisível, não pinta nada na tela
	}

	@Override
	public boolean isDead() {
		return onTimeOnly && cycles > 0;
	}

	@Override
	public void end() {
	}

	@Override
	public boolean isVisible() {
		// sempre invisível, não pinta nada na tela
		return false;
	}

	public long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public TimerGO setCounting(long counter) {
		this.counter = counter;
		return this;
	}

}

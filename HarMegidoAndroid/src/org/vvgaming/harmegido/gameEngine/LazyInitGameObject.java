package org.vvgaming.harmegido.gameEngine;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

import com.github.detentor.codex.function.Function0;

/**
 * Uma extensão de {@link GameObject} que suporta inicialição lazy. Devo
 * verificar a real necessidade mesmo dessa implementação depois
 * 
 * @author Vinicius Nogueira
 */
public abstract class LazyInitGameObject implements GameObject {

	private List<Function0<Void>> functions = new ArrayList<>();

	public abstract void update(long delta);

	public abstract void render(Canvas canvas);

	public abstract boolean isDead();

	public abstract boolean isVisible();

	public abstract void end();

	public final void init() {
		preInit();
		for (Function0<Void> f : functions) {
			f.apply();
		}
		functions.clear();
	}

	public abstract void preInit();

	public final void addToInit(Function0<Void> function) {
		functions.add(function);
	}

}

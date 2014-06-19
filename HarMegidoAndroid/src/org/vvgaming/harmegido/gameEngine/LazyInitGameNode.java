package org.vvgaming.harmegido.gameEngine;

import java.util.ArrayList;
import java.util.List;

import com.github.detentor.codex.function.Function0;

/**
 * Uma extensão de {@link GameNode} que suporta inicialização lazy. Devo
 * verificar a real necessidade mesmo dessa implementação depois
 * 
 * @author Vinicius Nogueira
 */
public abstract class LazyInitGameNode extends GameNode {

	private List<Function0<Void>> functions = new ArrayList<>();

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

package org.vvgaming.harmegido.gameEngine.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vvgaming.harmegido.gameEngine.GameNode;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Option;

/**
 * Grupo de botões de seleção {@link NToggleButtonImage} com imagens
 * 
 * @author Vinicius Nogueira
 */
public class NGroupToggleButtonImage extends GameNode {

	private List<NToggleButtonImage> btns = new ArrayList<>();
	private Option<Integer> toggledIndex = Option.empty();

	private Option<Function1<Option<Integer>, Void>> onToggleChange = Option
			.empty();

	public NGroupToggleButtonImage(final NToggleButtonImage... btns) {

		this.btns.addAll(Arrays.asList(btns));
	}

	@Override
	public void init() {
		super.init();
		for (final NToggleButtonImage btn : btns) {
			addSubNode(btn);
			btn.setOnClickFunction(new Function0<Void>() {
				@Override
				public Void apply() {
					toggle(btns.indexOf(btn));
					return null;
				}

			});
		}
	}

	private void toggle(final int index) {
		int oldIndex = toggledIndex.getOrElse(-1);
		for (NToggleButtonImage tglingBtn : btns) {
			tglingBtn.toggle(false);
		}
		btns.get(index).toggle(true);
		toggledIndex = Option.from(index);
		if (onToggleChange.notEmpty() && index != oldIndex) {
			onToggleChange.get().apply(toggledIndex);
		}
	}

	@Override
	protected void update(long delta) {
	}

	public Option<Integer> getToggledIndex() {
		return toggledIndex;
	}

	public Option<Function1<Option<Integer>, Void>> getOnToggleChange() {
		return onToggleChange;
	}

	public void setOnToggleChange(
			Function1<Option<Integer>, Void> onToggleChange) {
		this.onToggleChange = Option.from(onToggleChange);
	}

}

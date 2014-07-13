package org.vvgaming.harmegido.gameEngine.nodes.buttons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vvgaming.harmegido.gameEngine.GameNode;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Option;

/**
 * Grupo de botões de seleção {@link NToggleButton}
 * 
 * @author Vinicius Nogueira
 */
public class NGroupToggleButton extends GameNode
{

	private final List<NToggleButton> btns = new ArrayList<>();
	private Option<Integer> toggledIndex = Option.empty();

	private Option<Function1<Option<Integer>, Void>> onToggleChange = Option.empty();

	public NGroupToggleButton(final NToggleButton... btns)
	{

		this.btns.addAll(Arrays.asList(btns));
	}

	@Override
	public void init()
	{
		super.init();
		for (final NToggleButton btn : btns)
		{
			addSubNode(btn);
			btn.setOnClickFunction(new Function0<Void>()
			{
				@Override
				public Void apply()
				{
					toggle(btns.indexOf(btn));
					return null;
				}

			});
		}
	}

	/**
	 * Força a seleção de um item do grupo
	 * 
	 * @param index indice do item
	 */
	public void toggle(final int index)
	{
		final int oldIndex = toggledIndex.getOrElse(-1);
		for (final NToggleButton tglingBtn : btns)
		{
			tglingBtn.toggle(false);
		}
		btns.get(index).toggle(true);
		toggledIndex = Option.from(index);
		if (onToggleChange.notEmpty() && index != oldIndex)
		{
			onToggleChange.get().apply(toggledIndex);
		}
	}

	@Override
	protected void update(final long delta)
	{
	}

	public Option<Integer> getToggledIndex()
	{
		return toggledIndex;
	}

	/**
	 * Pega a função atribuida em {@link NGroupToggleButton#setOnToggleChange(Function1)}
	 * 
	 * @return
	 */
	public Option<Function1<Option<Integer>, Void>> getOnToggleChange()
	{
		return onToggleChange;
	}

	/**
	 * Atribui uma função listener para quando trocar o item selecionado do grupo
	 * 
	 * @param onToggleChange a função a ser executada que recebe como parametro uma option com o indice do botão selecionado
	 */
	public void setOnToggleChange(final Function1<Option<Integer>, Void> onToggleChange)
	{
		this.onToggleChange = Option.from(onToggleChange);
	}

}

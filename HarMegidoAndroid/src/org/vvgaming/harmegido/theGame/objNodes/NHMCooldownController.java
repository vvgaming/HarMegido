package org.vvgaming.harmegido.theGame.objNodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.vvgaming.harmegido.gameEngine.GameNode;

import com.github.detentor.codex.product.Tuple2;

/**
 * {@link GameNode} para controlar cooldowns de magias
 * 
 * @author Vinicius Nogueira
 */
public class NHMCooldownController extends GameNode
{

	// índice do cooldown, tempo restante, e tempo máximo
	private final Map<Integer, Tuple2<Long, Long>> cooldowns = new HashMap<>();

	@Override
	protected void update(final long delta)
	{
		for (final Entry<Integer, Tuple2<Long, Long>> cd : cooldowns.entrySet())
		{
			if (cd.getValue().getVal1() > 0)
			{
				cd.getValue().setVal1(cd.getValue().getVal1() - delta);
				if (cd.getValue().getVal1() < 0)
				{
					cd.getValue().setVal1(0L);
				}
			}
		}
	}

	/**
	 * Adiciona um novo cooldown a ser controlado
	 * 
	 * @param index
	 * @param maxTime tempo deste cooldown
	 */
	public void addCooldown(final int index, final long maxTime)
	{
		cooldowns.put(index, Tuple2.from(0L, maxTime));
	}

	/**
	 * "toca" numa magia para começar contar o cooldown
	 * 
	 * @param index
	 */
	public void touch(final int index)
	{
		final Tuple2<Long, Long> cd = cooldowns.get(index);
		cd.setVal1(cd.getVal2());
	}

	/**
	 * recupera o percentual (de 1 a 0) regressivo para acabar o cooldown
	 * 
	 * @param index
	 * @return de 1 a 0, onde 0 acabou
	 */
	public float getPercentage(final int index)
	{
		final Tuple2<Long, Long> cd = cooldowns.get(index);
		return cd.getVal1() / (float) cd.getVal2();
	}

}

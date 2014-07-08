package org.vvgaming.harmegido.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.util.AssetManager;

public class RandomNames
{

	private final List<String> angels;
	private final List<String> demons;
	private final Random rnd;

	public RandomNames(final AssetManager assetManager)
	{
		super();
		rnd = new Random();
		angels = Arrays.asList(assetManager.getRawTextFile(R.raw.anjos).split("\n"));
		demons = Arrays.asList(assetManager.getRawTextFile(R.raw.demonios).split("\n"));
		if (angels.isEmpty() || demons.isEmpty())
		{
			throw new IllegalArgumentException("arquivo dos nomes de anjos e/ou demonios está vazio");
		}
	}

	public String getRandomAngelName()
	{
		return angels.get(rnd.nextInt(angels.size()));
	}

	public String getRandomDemonName()
	{
		return demons.get(rnd.nextInt(demons.size()));
	}

	public String getRandomMatchName()
	{
		return getRandomDemonName() + " vs " + getRandomAngelName();
	}

}

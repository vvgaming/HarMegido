package org.vvgaming.harmegido.lib.model.match;

import org.vvgaming.harmegido.lib.model.Enchantment;
import org.vvgaming.harmegido.lib.model.Player;

/**
 * Representa uma mudança para um jogador fazer um desencantamento. Imutável.
 */
public class PlayerChangeDisenchant extends PlayerChange
{
	private final Enchantment enchantment;
	
	protected PlayerChangeDisenchant(Player jogador, final Enchantment enchantment)
	{
		super(jogador);
		this.enchantment = enchantment;
	}

	public Enchantment getEncantamento()
	{
		return enchantment;
	}

}

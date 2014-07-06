package org.vvgaming.harmegido.lib.model.match;

import org.vvgaming.harmegido.lib.model.Player;

/**
 * Representa uma mudança que cria um encantamento para um jogador. Imutável.
 */
public class PlayerChangeEnchant extends PlayerChange
{
	private final  byte[] enchantImage; 

	protected PlayerChangeEnchant(final Player jogador, final  byte[] enchantImage)
	{
		super(jogador);
		this.enchantImage = enchantImage;
	}

	public  byte[] getEnchantmentImage()
	{
		return enchantImage;
	}
}

package org.vvgaming.harmegido.lib.model.match;

import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.lib.util.EnchantmentImage;

/**
 * Representa uma mudança que cria um encantamento para um jogador. Imutável.
 */
public class PlayerChangeEnchant extends PlayerChange
{
	private final EnchantmentImage enchantImage; 

	protected PlayerChangeEnchant(final Player jogador, final EnchantmentImage enchantImage)
	{
		super(jogador);
		this.enchantImage = enchantImage;
	}

	public EnchantmentImage getEnchantmentImage()
	{
		return enchantImage;
	}
}

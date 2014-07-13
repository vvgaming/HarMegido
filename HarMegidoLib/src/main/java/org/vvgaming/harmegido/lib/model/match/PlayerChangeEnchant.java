package org.vvgaming.harmegido.lib.model.match;

import org.vvgaming.harmegido.lib.model.EnchantmentImage;
import org.vvgaming.harmegido.lib.model.Player;

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
	
	/**
	 * Cria uma cópia desta classe modificando a imagem do encantamento para a imagem
	 * passada como parâmetro. 
	 * @param novaImagem A nova imagem para o encantamento
	 * @return Um cópia desta classe, com o valor do encantamento substituído pelo encantamento
	 * passado como parâmetro
	 */
	public PlayerChangeEnchant createCopy(final EnchantmentImage novaImagem)
	{
		return new PlayerChangeEnchant(this.getJogador(), novaImagem);
	}
}

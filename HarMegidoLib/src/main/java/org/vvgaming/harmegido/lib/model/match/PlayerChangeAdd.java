package org.vvgaming.harmegido.lib.model.match;

import org.vvgaming.harmegido.lib.model.Player;

/**
 * Representa uma mudança para adicionar um jogador. Imutável.
 */
public class PlayerChangeAdd extends PlayerChange
{
	protected PlayerChangeAdd(final Player jogador)
	{
		super(jogador);
	}
}

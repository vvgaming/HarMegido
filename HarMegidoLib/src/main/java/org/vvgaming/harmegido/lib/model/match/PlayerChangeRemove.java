package org.vvgaming.harmegido.lib.model.match;

import org.vvgaming.harmegido.lib.model.Player;

/**
 * Representa uma mudança para remover um jogador. Imutável.
 */
public class PlayerChangeRemove extends PlayerChange
{
	protected PlayerChangeRemove(final Player jogador)
	{
		super(jogador);
	}
}

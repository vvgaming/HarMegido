package org.vvgaming.harmegido.lib.model.match;

import org.vvgaming.harmegido.lib.model.Player;

/**
 * Representa uma mudança de partida que tem a ver com um jogador 
 */
public abstract class PlayerChange extends MatchState
{
	private final Player jogador;

	protected PlayerChange(final Player jogador)
	{
		super();
		this.jogador = jogador;
	}

	public Player getJogador()
	{
		return jogador;
	}
}

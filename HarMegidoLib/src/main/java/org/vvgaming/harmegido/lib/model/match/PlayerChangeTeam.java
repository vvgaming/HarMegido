package org.vvgaming.harmegido.lib.model.match;

import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.lib.model.TeamType;

/**
 * Representa uma mudança para trocar o time de um jogador. Imutável.
 */
public class PlayerChangeTeam extends PlayerChange
{
	private final TeamType novoTime;

	protected PlayerChangeTeam(final Player jogador, final TeamType novoTime)
	{
		super(jogador);
		this.novoTime = novoTime;
	}

	public TeamType getNovoTime()
	{
		return novoTime;
	}

}

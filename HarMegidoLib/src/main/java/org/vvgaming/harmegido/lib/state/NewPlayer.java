package org.vvgaming.harmegido.lib.state;

import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.Player;

public class NewPlayer implements StateChange {

	private final Player player;

	public NewPlayer(Player player) {
		super();
		this.player = player;
	}

	public void execute(Match state) {
		state.adicionarJogador(player);
	}

}

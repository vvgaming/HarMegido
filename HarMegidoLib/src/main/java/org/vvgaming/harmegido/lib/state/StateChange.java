package org.vvgaming.harmegido.lib.state;

import org.vvgaming.harmegido.lib.model.Match;

public interface StateChange {
	void execute(Match state);
}

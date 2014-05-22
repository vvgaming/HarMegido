package org.vvgaming.harmegido.gameEngine;

import android.graphics.Canvas;

public interface GameObject {

	void render(final Canvas canvas);

	void update(long delta);

	boolean isDead();
	
}

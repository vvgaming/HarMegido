package org.vvgaming.harmegido.gameEngine;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Game {

	void update(long delta, Canvas canvas);

	void onTouch(MotionEvent event);

	void setWidth(int width);

	int getWidth();

	void setHeight(int height);

	int getHeight();

}

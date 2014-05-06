package org.vvgaming.harmegido.gameEngine;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RandomBubble implements GameObject {

	private static final long ANIMATION_DURATION = 5000;

	private final Paint randomColor = new Paint();
	private final int px, py;
	private final int maxRaio;

	private long accu = ANIMATION_DURATION / 2;
	private boolean faseAbre = true;

	public RandomBubble(int screenWidth, int screenHeight) {
		final Random r = new Random();
		randomColor.setARGB(r.nextInt(256), r.nextInt(256), r.nextInt(256),
				r.nextInt(256));
		px = r.nextInt(screenWidth);
		py = r.nextInt(screenHeight);
		maxRaio = r.nextInt(screenWidth);

	}

	@Override
	public void update(long delta, Canvas canvas) {

		accu += delta;
		if (accu > ANIMATION_DURATION) {
			accu -= ANIMATION_DURATION;
			faseAbre = !faseAbre;
		}

		long raio = (long) (((double) maxRaio / (double) ANIMATION_DURATION) * accu);
		if (!faseAbre) {
			raio = maxRaio - raio;
		}

		canvas.drawCircle(px, py, (int) raio, randomColor);
	}

}

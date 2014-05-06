package org.vvgaming.harmegido.gameEngine;

import java.util.ArrayList;
import java.util.List;

import org.vvgaming.harmegido.gameEngine.geometry.Vetor2d;
import org.vvgaming.harmegido.vision.ObjDetectTestActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;

public class GameTestActivity extends Activity {
	private GameCanvas gameCanvas;
	private List<RandomBubble> bubbles = new ArrayList<>();
	private final SquareButton btn = new SquareButton(0, 100, 200, 120,
			"Testar ObjDetect");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameCanvas = new GameCanvas(this, new Game() {

			private Paint cinza = new Paint();
			private long acc = 0;
			private int width = 0;
			private int height = 0;

			@Override
			public void update(long delta, Canvas canvas) {
				cinza.setARGB(255, 200, 200, 200);
				acc += delta;

				if (acc > 2000) {
					acc -= 2000;
				}

				for (RandomBubble b : new ArrayList<>(bubbles)) {
					b.update(delta, canvas);
				}

				canvas.drawText("bubbles: " + bubbles.size(), 100, 10, cinza);
				btn.update(delta, canvas);
			}

			@Override
			public void onTouch(MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					if (btn.ret.contem(new Vetor2d(event.getX(), event.getY()))) {
						Intent i = new Intent(GameTestActivity.this
								.getApplicationContext(),
								ObjDetectTestActivity.class);
						startActivity(i);
					} else {
						bubbles.add(new RandomBubble(getWidth(), getHeight()));
					}

				}
			}

			public int getWidth() {
				return width;
			}

			@Override
			public void setWidth(int width) {
				this.width = width;

			}

			@Override
			public int getHeight() {
				return height;
			}

			@Override
			public void setHeight(int height) {
				this.height = height;

			}

		});
		gameCanvas.setShowFps(true);
		setContentView(gameCanvas);
	}

}

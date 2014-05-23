package org.vvgaming.harmegido.gameEngine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * Implementação de um canvas para jogos. Este canvas implementa uma Thread e
 * recebe um {@link AbstractGame} para renderizar seus objetos controlando o
 * loop.
 * 
 * @author Vinicius Nogueira
 */
public class GameCanvas extends SurfaceView implements Callback {

	private MyThread myThread;
	private int fpsLock = 30;
	private boolean showFps = false;
	private final AbstractGame theGame;

	public GameCanvas(Context context, AbstractGame theGame) {
		super(context);
		getHolder().addCallback(this);
		this.theGame = theGame;

		setKeepScreenOn(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		theGame.onTouch(event);
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		myThread = new MyThread(getHolder());
		theGame.setWidth(getWidth());
		theGame.setHeight(getHeight());
		myThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		myThread.setTheHolder(holder);
		theGame.setWidth(getWidth());
		theGame.setHeight(getHeight());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		myThread.setShouldQuit(true);
		myThread = null;
	}

	private class MyThread extends Thread {

		private SurfaceHolder theHolder;
		private boolean shouldQuit = false;

		public MyThread(SurfaceHolder theHolder) {
			super();
			this.theHolder = theHolder;
		}

		public void setTheHolder(SurfaceHolder theHolder) {
			this.theHolder = theHolder;
		}

		@Override
		public void run() {

			final int MIN_DELAY = 1000 / fpsLock;

			Paint cinza = new Paint();
			cinza.setARGB(255, 255, 255, 255);

			Paint preto = new Paint();
			preto.setARGB(255, 0, 0, 0);

			// criando um retangulo do tamanho máximo para limpar a tela
			Canvas canvas = theHolder.lockCanvas();
			Rect retanguloMaximo = new Rect(0, 0, canvas.getWidth(),
					canvas.getHeight());
			theHolder.unlockCanvasAndPost(canvas);

			long delta = 0;

			while (!shouldQuit) {

				long initFrame = System.currentTimeMillis();

				canvas = theHolder.lockCanvas();
				// pinta tudo de preto
				canvas.drawRect(retanguloMaximo, preto);

				theGame.realUpdate(delta);
				theGame.realRender(canvas);

				if (showFps) {
					canvas.drawText("fps: "
							+ (delta != 0 ? 1000 / delta : "-----"), 10, 10,
							cinza);
				}

				theHolder.unlockCanvasAndPost(canvas);

				// cálculo parcial do delta
				delta = System.currentTimeMillis() - initFrame;
				// espera um pouco por causa do fpslock
				delay(MIN_DELAY - delta);
				// cálculo definitivo do tempo gasto neste frame
				delta = System.currentTimeMillis() - initFrame;

			}

		}

		public void setShouldQuit(boolean shouldQuit) {
			this.shouldQuit = shouldQuit;
		}

	}

	private void delay(long milis) {
		if (milis < 0)
			return;
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void setFpsLock(int fpsLock) {
		this.fpsLock = fpsLock;
	}

	public boolean isShowFps() {
		return showFps;
	}

	public void setShowFps(boolean showFps) {
		this.showFps = showFps;
	}

}

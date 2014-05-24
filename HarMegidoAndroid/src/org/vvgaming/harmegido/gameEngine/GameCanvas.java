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
 * Implementa��o de um canvas para jogos. Este canvas implementa uma Thread e
 * recebe um {@link AbstractGame} para renderizar seus objetos controlando o
 * loop.
 * 
 * @author Vinicius Nogueira
 */
public class GameCanvas extends SurfaceView implements Callback {

	private LoopThread loopThread;
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
		theGame.setWidth(getWidth());
		theGame.setHeight(getHeight());

		loopThread = new LoopThread(getHolder());
		loopThread.start();
		theGame.realInit();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		loopThread.setTheHolder(holder);
		theGame.setWidth(width);
		theGame.setHeight(height);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		loopThread.setShouldQuit(true);
		loopThread = null;
		theGame.realEnd();
	}

	private class LoopThread extends Thread {

		private SurfaceHolder theHolder;
		private boolean shouldQuit = false;

		public LoopThread(SurfaceHolder theHolder) {
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

			// criando um retangulo do tamanho m�ximo para limpar a tela
			Canvas canvas = theHolder.lockCanvas();
			Rect retanguloMaximo = new Rect(0, 0, canvas.getWidth(),
					canvas.getHeight());
			theHolder.unlockCanvasAndPost(canvas);

			long delta = 0;

			while (!shouldQuit) {

				long initFrame = System.currentTimeMillis();

				canvas = theHolder.lockCanvas();
				if (canvas == null) {
					// se n�o tiver canvas, pula pra pr�xima passada
					// (provavelmente vai abortar)
					// isso � para resolver uma NPE quando surfaceDestroyed �
					// chamado e vai provavelmente abortar na pr�xima passada
					// TODO verificar se essa solu��o � boa mesmo, ou se n�o,
					// bolar uma melhor
					continue;
				}
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

				// c�lculo parcial do delta
				delta = System.currentTimeMillis() - initFrame;
				// espera um pouco por causa do fpslock
				delay(MIN_DELAY - delta);
				// c�lculo definitivo do tempo gasto neste frame
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
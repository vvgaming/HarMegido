package org.vvgaming.harmegido.gameEngine;

import android.annotation.SuppressLint;
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
 * recebe um {@link AbstractGameScene} para renderizar seus objetos controlando o
 * loop.
 * 
 * @author Vinicius Nogueira
 */
@SuppressLint("ViewConstructor")
public class GameCanvas extends SurfaceView implements Callback {

	private LoopThread loopThread;
	private int fpsLock = 30;
	private boolean showFps = false;
	private AbstractGameScene cena;

	private AbstractGameScene proximaCena = null;

	public GameCanvas(final Context context, final AbstractGameScene aCena) {
		super(context);
		getHolder().addCallback(this);
		this.cena = aCena;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		cena.onTouch(event);
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		inicializaCena(cena);
	}

	private void reInicializaCena(final AbstractGameScene aCena) {
		aCena.realEnd();
		// novo jogo
		this.cena = aCena;
		inicializaCena(aCena);
	}

	private void inicializaCena(final AbstractGameScene aCena) {
		aCena.setWidth(getWidth());
		aCena.setHeight(getHeight());

		aCena.realInit();

		loopThread = new LoopThread(getHolder());
		loopThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		loopThread.setTheHolder(holder);
		cena.setWidth(width);
		cena.setHeight(height);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		loopThread.setShouldQuit(true);
		loopThread = null;
		encerraCena();
	}

	private void encerraCena() {
		cena.realEnd();
		cena = null;
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

			// criando um retangulo do tamanho máximo para limpar a tela
			Canvas canvas = theHolder.lockCanvas();
			Rect retanguloMaximo = new Rect(0, 0, canvas.getWidth(),
					canvas.getHeight());
			theHolder.unlockCanvasAndPost(canvas);

			long delta = 0;

			while (!shouldQuit) {

				long initFrame = System.currentTimeMillis();

				canvas = theHolder.lockCanvas();
				if (canvas == null) {
					// se não tiver canvas, pula pra próxima passada
					// (provavelmente vai abortar)
					// isso é para resolver uma NPE quando surfaceDestroyed é
					// chamado e vai provavelmente abortar na próxima passada
					// TODO verificar se essa solução é boa mesmo, ou se não,
					// bolar uma melhor
					continue;
				}
				// pinta tudo de preto
				canvas.drawRect(retanguloMaximo, preto);

				cena.realUpdate(delta);
				cena.realRender(canvas);

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

			if (proximaCena != null) {
				encerraCena();
				reInicializaCena(proximaCena);
				proximaCena = null;
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

	public void trocarCena(final AbstractGameScene novaCena) {
		loopThread.setShouldQuit(true);
		proximaCena = novaCena;
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

package org.vvgaming.harmegido.gameEngine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * Implementação de um canvas para jogos. Este canvas implementa uma Thread e recebe um {@link RootNode} para renderizar seus objetos
 * controlando o loop.
 * 
 * @author Vinicius Nogueira
 */
@SuppressLint("ViewConstructor")
public class GameCanvas extends SurfaceView implements Callback
{

	private LoopThread loopThread;
	private int fpsLock = 30;
	private boolean showFps = false;
	private RootNode root;

	public GameCanvas(final Context context, final RootNode theRoot)
	{
		super(context);
		getHolder().addCallback(this);
		this.root = theRoot;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		return root.onRealTouch(event);
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder)
	{
		root.setWidth(getWidth());
		root.setHeight(getHeight());

		root.realInit();

		loopThread = new LoopThread(getHolder());
		loopThread.start();
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height)
	{

		loopThread.setTheHolder(holder);
		root.setWidth(width);
		root.setHeight(height);
	}

	@Override
	public void surfaceDestroyed(final SurfaceHolder holder)
	{
		root.kill();
		try
		{
			// espera a thread encerrar
			loopThread.join();
		}
		catch (final InterruptedException ignored)
		{
			Log.e("erro harmegido", "erro harmegido", ignored);
		}
		loopThread = null;
	}

	private class LoopThread extends Thread
	{

		private SurfaceHolder theHolder;

		public LoopThread(final SurfaceHolder theHolder)
		{
			super();
			this.theHolder = theHolder;
		}

		public void setTheHolder(final SurfaceHolder theHolder)
		{
			this.theHolder = theHolder;
		}

		@Override
		public void run()
		{

			final int MIN_DELAY = 1000 / fpsLock;

			final Paint cinza = new Paint();
			cinza.setARGB(255, 255, 255, 255);

			final Paint preto = new Paint();
			preto.setARGB(255, 0, 0, 0);

			// criando um retangulo do tamanho m�ximo para limpar a tela
			Canvas canvas = theHolder.lockCanvas();
			final Rect retanguloMaximo = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
			theHolder.unlockCanvasAndPost(canvas);

			long delta = 0;

			while (!root.isDead())
			{

				final long initFrame = System.currentTimeMillis();

				canvas = theHolder.lockCanvas();
				if (canvas == null)
				{
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

				root.realUpdate(delta);
				root.realRender(canvas);

				if (showFps)
				{
					canvas.drawText("fps: " + (delta != 0 ? 1000 / delta : "-----"), 10, 10, cinza);
				}

				theHolder.unlockCanvasAndPost(canvas);

				// cálculo parcial do delta
				delta = System.currentTimeMillis() - initFrame;
				// espera um pouco por causa do fpslock
				delay(MIN_DELAY - delta);
				// cálculo definitivo do tempo gasto neste frame
				delta = System.currentTimeMillis() - initFrame;

			}
			root = null;

		}

	}

	private void delay(final long milis)
	{
		if (milis < 0)
		{
			return;
		}
		try
		{
			Thread.sleep(milis);
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	public void setFpsLock(final int fpsLock)
	{
		this.fpsLock = fpsLock;
	}

	public boolean isShowFps()
	{
		return showFps;
	}

	public void setShowFps(final boolean showFps)
	{
		this.showFps = showFps;
	}

}

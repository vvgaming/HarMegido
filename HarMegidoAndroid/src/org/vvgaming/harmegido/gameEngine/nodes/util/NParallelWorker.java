package org.vvgaming.harmegido.gameEngine.nodes.util;

import java.util.LinkedList;
import java.util.Queue;

import org.vvgaming.harmegido.gameEngine.GameNode;

import com.github.detentor.codex.function.Function0;

/**
 * {@link GameNode} que implementa uma Thread separada para realizar trabalhos paralelos
 * 
 * @author Vinicius Nogueira
 */
public class NParallelWorker extends GameNode
{

	private final Queue<Function0<Void>> tasks = new LinkedList<>();
	private Thread worker;
	private boolean shouldStop = false;

	@Override
	protected void init()
	{
		super.init();
		worker = new Thread()
		{
			@Override
			public void run()
			{
				while (!shouldStop)
				{
					// executa as tasks da fila
					synchronized (tasks)
					{
						while (!tasks.isEmpty())
						{
							final Function0<Void> task = tasks.poll();
							task.apply();
						}
					}
					
					synchronized (worker)
					{
						try
						{
							worker.wait();
						}
						catch (final InterruptedException ignored)
						{//

						}
					}
				}
			}
		};
		worker.start();
	}

	@Override
	public void update(final long delta)
	{
	}

	@Override
	protected void end()
	{
		// acorda a Thread e espera encerrar
		shouldStop = true;
		synchronized (worker)
		{
			worker.notify();	
		}
		try
		{
			worker.join();
		}
		catch (final InterruptedException ignored)
		{
		}
		super.end();
	}

	@Override
	public boolean isVisible()
	{
		// sempre invisível, não pinta nada na tela
		return false;
	}

	/**
	 * Adiciona uma task a ser executada na fila
	 * 
	 * @param task
	 */
	public void putTask(final Function0<Void> task)
	{
		synchronized (tasks)
		{
			// adiciona a task e acorda a Thread
			tasks.add(task);
		}
		synchronized (worker)
		{
			worker.notify();
		}
	}

	public int qtdPendingTasks()
	{
		return tasks.size();
	}

}

package org.vvgaming.harmegido.lib.async;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;

import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Classe que objetiva enviar mensagem de maneira assíncrona para o UOS, ao mesmo tempo em que
 * tenta se assegurar que a mensagem seja recebida.
 * 
 */
public class AsyncCallService
{
	private final Gateway gateway;
	private final Thread thread;
	private final LinkedList<AsyncTask> tasks = new LinkedList<AsyncTask>();
	private volatile boolean isRunning = true;

	public AsyncCallService(final Gateway gateway)
	{
		super();
		this.gateway = gateway;
		
		thread = new Thread(new CallWorker());
		thread.start();
	}

	
	/**
	 * Dispara o conjunto de mensagens passadas como parâmetro. <br/>
	 * É um set para assegurar que existe apenas uma mensagem para cada dispositivo nesse grupo. <br/>
	 */
	public void addCalls(final Set<AsyncCall> calls)
	{
		synchronized (tasks)
		{
			tasks.add(new AsyncTask(calls));
		}
		
		synchronized (thread)
		{
			thread.notifyAll();
		}
	}
	

	/**
	 * Dispara o conjunto de mensagens passadas como parâmetro. <br/>
	 * É um set para assegurar que existe apenas uma mensagem para cada dispositivo nesse grupo. <br/>
	 */
	public void addCall(final AsyncCall call)
	{
		final HashSet<AsyncCall> calls = new HashSet<AsyncCall>();
		calls.add(call);
		addCalls(calls);
	}
	
	/**
	 * Interrompe todos os trabalhos deste ServerCall, e destrói 
	 * a thread em execução.
	 */
	public void end()
	{
		isRunning = false;
		
		synchronized (thread)
		{
			thread.interrupt();
		}
	}

	private final class CallWorker implements Runnable
	{
		private static final int RETRY_TOLERANCE = 15000; //15 sec
		private final Map<String, Tuple2<Date, LinkedList<AsyncCall>>> blackList = 
				new HashMap<String, Tuple2<Date, LinkedList<AsyncCall>>>();
		
		@Override
		public void run()
		{
			while (isRunning)
			{
				AsyncTask curTask;
				final List<AsyncCall> failedCalls = new ArrayList<AsyncCall>();
				
				//Retira uma tarefa para trabalhar
				synchronized (tasks)
				{
					curTask = tasks.poll();
				}
				
				//Se não houver tarefa, cria uma tarefa vazia
				if (curTask == null)
				{
					curTask = AsyncTask.emptyTask();
				}

				//Executa todas as chamadas sequencialmente,
				//coletando aqueles que deu erro
				for (AsyncCall curCall : curTask)
				{
					final Option<Exception> response = curCall.apply(gateway);

					//Se houve erro no envio, tentará novamente
					if (response.notEmpty())
					{
						failedCalls.add(curCall);
					}
				}
				
				updateBlackList(failedCalls);
				retryBlackList();

				boolean isEmpty = true;
				
				synchronized (tasks)
				{
					isEmpty = tasks.isEmpty();
				}

				//Se não houver mais trabalho a fazer, espera até haver
				if (isEmpty)
				{
					synchronized (thread)
					{
						try
						{
							thread.wait();
						}
						catch (InterruptedException e)
						{
							//ignored
						}
					}
				}
			}
		}
		
		/**
		 * Tenta reenviar as mensagens que falharam. <br/>
		 * Tentará sempre uma mensagem por vez para cada dispositivo diferente.
		 */
		private void retryBlackList()
		{
			synchronized (blackList)
			{
				final Iterator<Entry<String, Tuple2<Date, LinkedList<AsyncCall>>>> ite = blackList.entrySet().iterator();
				
				while (ite.hasNext())
				{
					final Entry<String, Tuple2<Date, LinkedList<AsyncCall>>> curEntry = ite.next();
					final int elapsedSecs = (int) (new Date().getTime() - curEntry.getValue().getVal1().getTime());
					
					//Se o número de segundos toleráveis passou, remove da blacklist
					if (elapsedSecs > RETRY_TOLERANCE)
					{
						ite.remove();
					}
					else
					{
						Option<Exception> response = null;
						
						do
						{
							AsyncCall toTry = curEntry.getValue().getVal2().peek();
							response = toTry.apply(gateway);

							//Se executou corretamente, remove a chamada da lista de pendências
							if (response.isEmpty())
							{
								curEntry.getValue().getVal2().poll();
								curEntry.getValue().setVal1(new Date());
							}
							
						} while (response.isEmpty() && !curEntry.getValue().getVal2().isEmpty());
						
						//Se conseguiu executar todas as chamadas pendentes, remove o elemento da blackList
						if (curEntry.getValue().getVal2().isEmpty())
						{
							ite.remove();
						}
					}
				}
			}
		}
		
		/**
		 * Atualiza a blacklist com as chamadas passadas
		 * @param calls
		 */
		private void updateBlackList(final List<AsyncCall> calls)
		{
			synchronized (blackList)
			{
				for (AsyncCall curCall : calls)
				{
					Tuple2<Date, LinkedList<AsyncCall>> prevValue = blackList.get(curCall.getDeviceName());
					
					if (prevValue == null)
					{
						prevValue = Tuple2.from(new Date(), new LinkedList<AsyncCall>());
						blackList.put(curCall.getDeviceName(), prevValue);
					}

					//Adiciona a outra chamada não invocada
					prevValue.getVal2().add(curCall);
				}
			}
		}
	}
	
	/**
	 * Representa uma tarefa a ser executada. Contém um conjunto de chamadas.
	 *
	 */
	private static final class AsyncTask implements Iterable<AsyncCall>
	{
		private static final AsyncTask emptyTask = new AsyncTask(new HashSet<AsyncCall>());
		
		private final Set<AsyncCall> callSet;
		
		/**
		 * Retorna uma tarefa assíncrona vazia
		 */
		public static AsyncTask emptyTask()
		{
			return emptyTask;
		}
		
		public AsyncTask(Set<AsyncCall> callSet)
		{
			super();
			this.callSet = callSet;
		}

		@Override
		public Iterator<AsyncCall> iterator()
		{
			return callSet.iterator();
		}
	}

}

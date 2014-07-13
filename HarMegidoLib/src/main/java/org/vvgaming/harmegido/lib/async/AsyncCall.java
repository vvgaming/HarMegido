package org.vvgaming.harmegido.lib.async;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.Call;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Option;

/**
 * Representa um notificação do servidor para um cliente. <br/>
 * É diferente da chamada do cliente para o servidor no sentido que
 * o servidor só se preocupa se o cliente recebeu a mensagem ou não, ignorando
 * o restante da resposta
 */
public class AsyncCall implements Function1<Gateway, Option<Exception>>
{
	private final UpDevice device;
	private final Call call;

	protected AsyncCall(final UpDevice device, final Call call)
	{
		super();
		this.device = device;
		this.call = call;
	}

	/**
	 * Cria uma chamada para o servidor, a ser disparada para o upDevice com os
	 * dados de theCall passados como parâmetro
	 * @param upDevice O disposito a receber a mensagem
	 * @param theCall Os dados contidos na mensagem
	 * @return Uma instância de ServerCall que ao ser invocada disparará a mensagem
	 */
	public static AsyncCall from(final UpDevice upDevice, final Call theCall)
	{
		return new AsyncCall(upDevice, theCall);
	}

	@Override
	public Option<Exception> apply(final Gateway gateway)
	{
		try
		{
			gateway.callService(device, call);
			return Option.empty();
		}
		catch (ServiceCallException e)
		{
			return Option.from((Exception) e);
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (device == null ? 0 : device.getName().hashCode());
		return result;
	}
	
	/**
	 * Retorna o nome do dispositivo para o qual a chamada é dirigida
	 * @return O nome do dispositivo que a chamada é dirigida
	 */
	public String getDeviceName()
	{
		return device.getName();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final AsyncCall other = (AsyncCall) obj;
		if (device == null)
		{
			if (other.device != null)
			{
				return false;
			}
		}
		else if (!device.getName().equals(other.device.getName()))
		{
			return false;
		}
		return true;
	}
}

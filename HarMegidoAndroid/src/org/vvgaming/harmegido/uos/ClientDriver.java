package org.vvgaming.harmegido.uos;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;
import static org.vvgaming.harmegido.lib.util.JSONTransformer.toJson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.match.MatchState;
import org.vvgaming.harmegido.util.DeviceInfo;
import org.vvgaming.harmegido.util.MatchManager;

import com.github.detentor.codex.monads.Either;
import com.github.detentor.codex.monads.Option;

public class ClientDriver implements UosDriver
{
	private final UpDriver definition;
	private final String DRIVER_NAME = "uos.harmegido.client";
	
	// Cria um objeto que será o "monitor" desta Thread.
	// Ele possui um tipo próprio para facilitar debug se houver erro
	private static final class Lock	{}
	private final Object lock = new Lock();

	public ClientDriver()
	{
		//TODO: Adicionar os serviços aqui
		definition = new UpDriver(DRIVER_NAME);
		definition.addService("runState");
	}

	@Override
	public void init(final Gateway gateway, final InitialProperties properties, final String instanceId)
	{
		//Se registra no dispositivo
		DeviceInfo.setDriver(gateway.getCurrentDevice());
	}

	@Override
	public void destroy()
	{
	}

	@Override
	public UpDriver getDriver()
	{
		return definition;
	}

	@Override
	public List<UpDriver> getParent()
	{
		return new ArrayList<UpDriver>();
	}
	
	/**
	 * Faz a efetiva execução de um estado para uma partida
	 */
	public void runState(Call call, Response response, CallContext callContext)
	{
		final String nomePartida = call.getParameter("nomePartida").toString();
		final MatchState state = fromJson(call.getParameter("state").toString(), MatchState.class);
		final Date executionTime = fromJson(call.getParameter("executionTime").toString(), Date.class);
		
		//TODO: Aqui existe uma dependência direta do driver do cliente com o MatchManager.
		//Analisar se é necessário remover essa dependência direta
		final Option<Match> partida = MatchManager.getPartida();
		
		Either<Exception, Boolean> toReturn = null;
		String mensagem = null;
		
		if (partida.isEmpty())
		{
			mensagem = "Não é possível executar a alteração do estado: Nenhuma partida ativa";
		}
		else if (!partida.get().getNomePartida().equals(nomePartida))
		{
			mensagem = "Não é possível executar a alteração do estado: "
					+ "A partida que a alteração se refere não é a partida ativa";
		}
		else
		{
			//Efetua a alteração
			synchronized(lock)
			{
				partida.get().executarMudanca(state, executionTime);
			}
		}

		if (mensagem == null)
		{
			toReturn = Either.createRight(true);
		}
		else
		{
			final Exception exception = new IllegalStateException(mensagem);
			toReturn = Either.createLeft(exception);
		}
		
		response.addParameter("retorno", toJson(toReturn));
	}

}

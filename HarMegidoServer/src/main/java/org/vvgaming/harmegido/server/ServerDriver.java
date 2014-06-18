package org.vvgaming.harmegido.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.Match.MatchDuration;

import com.github.detentor.codex.monads.Either;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.*;

public class ServerDriver implements UosDriver
{

	private UpDriver definition;
	private String DRIVER_NAME = "uos.harmegido.server";
	private Map<String, Match> mapaPartidas = new HashMap<String, Match>();
	
	//Cria um objeto que será o "monitor" desta Thread. 
	//Ele possui um tipo próprio para facilitar debug se houver erro
	private static final class Lock { }
	private final Object lock = new Lock();

	public ServerDriver()
	{
		definition = new UpDriver(DRIVER_NAME);
		definition.addService("getClientCount");
		definition.addService("getHostName");
	}

	public void init(Gateway gateway, InitialProperties properties, String instanceId)
	{
	}

	public UpDriver getDriver()
	{
		return definition;
	}

	public List<UpDriver> getParent()
	{
		return new ArrayList<UpDriver>();
	}

	public void destroy()
	{
	}

	// Serviços deste Server Driver
	
	public void criarPartida(Call call, Response response, CallContext callContext)
	{
		final String nomePartida = call.getParameter("nomePartida").toString();
		final MatchDuration duracao = MatchDuration.from(call.getParameter("duracao").toString());
		final Match partida = Match.from(nomePartida, new Date(), duracao);
		
		if (findMatch(nomePartida).isRight()) //Existe partida
		{
			throw new IllegalArgumentException("Partida com esse nome já existe");
		}
		
		//Efetua a alteração
		synchronized(lock)
		{
			mapaPartidas.put(nomePartida, partida);
		}
		response.addParameter("partida", toJson(partida));
	}

	public void adicionarJogador(Call call, Response response, CallContext callContext)
	{
		final String nomePartida = call.getParameter("nomePartida").toString();
		final String idJogador = call.getParameter("idJogador").toString();
		
		final Either<Exception, Match> eMatch = findMatch(nomePartida);
		
		if (eMatch.isLeft()) //Não existe partida
		{
			throw new IllegalArgumentException(eMatch.getLeft());
		}
		
//		//Efetua a alteração
//		synchronized(lock)
//		{
//			eMatch.getRight().executarMudanca(stateChange);
//			mapaPartidas.put(nomePartida, partida);
//		}
//		
//		else
//		{
//			
//			synchronized(lock)
//			{
//				mapaPartidas.put(nomePartida, partida);
//			}
//		}
//		final String json = toJson(partida);
//		response.addParameter("partida", json);
	}
	
	/**
	 * Procura uma partida, retornando ou a partida ou uma exceção
	 * @param nomePartida A partida a ser procurada
	 * @return Uma instância de {@link Either} que irá conter a partida ou a exceção
	 */
	private Either<Exception, Match> findMatch(final String nomePartida)
	{
		Match theMatch;
		
		synchronized(lock)
		{
			theMatch = mapaPartidas.get(nomePartida);
		}

		if (theMatch == null)
		{
			final Exception theException = new IllegalArgumentException("A partida procurada não existe");
			return Either.createLeft(theException);
		}
		return Either.createRight(theMatch);
	}
	
	public void getClientCount(Call call, Response response, CallContext callContext)
	{
		List<UpDevice> listDevices = Main.getUos().getGateway().listDevices();
		int clients = listDevices == null ? 0 : listDevices.size();
		response.addParameter("clientCount", clients);
	}

	public void getHostName(Call call, Response response, CallContext callContext)
	{
		try
		{
			response.addParameter("hostName", InetAddress.getLocalHost().getHostName());
		}
		catch (UnknownHostException e)
		{
			response.addParameter("hostName", "unknown");
		}
	}
}

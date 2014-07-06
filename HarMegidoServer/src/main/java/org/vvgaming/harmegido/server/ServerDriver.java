package org.vvgaming.harmegido.server;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;
import static org.vvgaming.harmegido.lib.util.JSONTransformer.toJson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.Match.MatchDuration;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.lib.model.TeamType;
import org.vvgaming.harmegido.lib.model.match.MatchState;
import org.vvgaming.harmegido.lib.model.match.PlayerChangeDisenchant;
import org.vvgaming.harmegido.lib.model.match.PlayerChangeEnchant;

import com.github.detentor.codex.collections.mutable.SetSharp;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Either;
import com.github.detentor.codex.util.Reflections;

/**
 * O driver do servidor, provendo todos os seus serviços. <br/>
 * O contrato de cada serviço deste driver exige que a resposta
 * será dada através do parâmetro de nome 'retorno', e seu objeto
 * será uma instância de {@link Either} que irá conter o retorno 
 * da chamada, ou a exceção no caso de algum erro.
 */
public class ServerDriver implements UosDriver
{
	private UpDriver definition;
	//TODO: Extrair essa constante para alguma outra parte
	private static final String DRIVER_NAME = "uos.harmegido.server";
	private static final String CLIENT_DRIVER_NAME = "uos.harmegido.client";
	
	//Tempo a esperar para remover uma partida que já acabou
	private static final long CLEANUP_TIME = 30000;
	
	//Segura a referência ao Gateway
	private Gateway gateway;
	
	//TODO:O mapa de partidas contém tanto as partidas ativas quanto as inativas
	//deve-se fazer algum tipo de limpeza, para retirar do mapa as partidas que acabaram
	private Map<String, Match> mapaPartidas = new HashMap<String, Match>();

	//Cria um objeto que será o "monitor" desta Thread. 
	//Ele possui um tipo próprio para facilitar debug se houver erro
	private static final class Lock { }
	private final Object lock = new Lock();

	public ServerDriver()
	{
		definition = new UpDriver(DRIVER_NAME);

		//TODO: Adicionar os serviços aqui
		definition.addService("criarPartida");
		definition.addService("encontrarPartida");
		definition.addService("runState");
		definition.addService("listarPartidas");
		definition.addService("listarJogadores");
	}

	public void init(Gateway gateway, InitialProperties properties, String instanceId)
	{
		this.gateway = gateway;
	}
	
	public void destroy()
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


	// Serviços deste Server Driver
	
	/**
	 * Cria uma partida. <br/>
	 * Parâmetros: nomePartida, duracao
	 */
	public void criarPartida(Call call, Response response, CallContext callContext)
	{
		final String nomePartida = call.getParameter("nomePartida").toString();
		final MatchDuration duracao = MatchDuration.from(call.getParameter("duracao").toString());
		final Match partida = Match.from(nomePartida, new Date(), duracao);
		
		final Either<RuntimeException, Match> eMatch = findMatch(nomePartida);
		Either<RuntimeException, Match> toReturn;
		
		if (eMatch.isRight()) //Existe partida
		{
			final RuntimeException theValue = new IllegalArgumentException("Partida com esse nome já existe");
			toReturn = Either.createLeft(theValue);
		}
		else //Não existe partida
		{
			//Efetua a alteração
			synchronized(lock)
			{
				mapaPartidas.put(nomePartida, partida);
			}
			toReturn = Either.createRight(partida);
		}
		response.addParameter("retorno", toJson(toReturn));
	}
	
	/**
	 * Encontra uma partida já existente
	 */
	public void encontrarPartida(Call call, Response response, CallContext callContext)
	{
		final String idJogador = call.getParameter("idJogador").toString();
		Match partida = null;
		
		synchronized(lock)
		{
			for (Entry<String, Match> curEntry : mapaPartidas.entrySet())
			{
				final Match curMatch = curEntry.getValue();
				
				if (curMatch.isAtiva() && curMatch.contemJogador(idJogador))
				{
					partida = curMatch;
					break;
				}
			}
		}
		
		Either<RuntimeException, Match> toReturn = null;
		
		if (partida == null)
		{
			final RuntimeException rException = new NoSuchElementException("O jogador não pertence a nenhuma partida ativa");
			toReturn = Either.createLeft(rException);
		}
		else
		{
			toReturn = Either.createRight(partida);
		}

		response.addParameter("retorno", toJson(toReturn));
	}
	
	/**
	 * Faz a efetiva execução de um estado para uma partida
	 */
	public void runState(Call call, Response response, CallContext callContext)
	{
		final String nomePartida = call.getParameter("nomePartida").toString();
		final String stateJson = call.getParameter("state").toString();
		final MatchState state = fromJson(stateJson, MatchState.class);
		final Either<RuntimeException, Match> eMatch = findMatch(nomePartida);
		
		if (eMatch.isLeft()) //Não existe partida
		{
			response.addParameter("retorno", toJson(eMatch));
		}
		else
		{
			//Efetua a alteração
			synchronized(lock)
			{
				eMatch.getRight().executarMudanca(state);
			}
			
			response.addParameter("retorno", toJson(Either.createRight(true)));
			
			//Verifica o tipo de alteração
			if (state instanceof PlayerChangeEnchant || state instanceof PlayerChangeDisenchant)
			{
				//TODO: Colocar algum código para fazer algo com o retorno
				notifyClients(CLIENT_DRIVER_NAME, stateJson);
			}
		}
	}
	
	/**
	 * Lista todas as partidas ativas neste momento
	 */
	public void listarPartidas(Call call, Response response, CallContext callContext)
	{
		final List<String> listaPartidas = new ArrayList<String>();
		
		//Trava até terminar de ler
		synchronized(lock)
		{
			for (Entry<String, Match> entry : mapaPartidas.entrySet())
			{
				if (entry.getValue().isAtiva())
				{
					listaPartidas.add(entry.getKey());
				}
			}
		}
		
		final Either<RuntimeException, List<String>> toReturn = Either.createRight(listaPartidas);
		response.addParameter("retorno", toJson(toReturn));
	}
	
	/**
	 * Lista todas as partidas e uma contagem de jogadores de cada tipo
	 */
	public void listarJogadores(Call call, Response response, CallContext callContext)
	{
		final Map<String, Map<TeamType, Integer>> mapaRetorno = new HashMap<String, Map<TeamType, Integer>>();
		
		//Trava até terminar de ler
		synchronized(lock)
		{
			for (Match match : mapaPartidas.values())
			{
				if (match.isAtiva())
				{
					final Map<TeamType, Integer> mapaValores = new HashMap<TeamType, Integer>();
					
					//Inicializa o mapa
					for (TeamType tipoTime : TeamType.values())
					{
						mapaValores.put(tipoTime, 0);
					}

					//Conta quantos jogadores em cada time
					for (Player curJogador : match.getJogadores())
					{
						final Integer valorAnterior = mapaValores.get(curJogador.getTime());
						mapaValores.put(curJogador.getTime(), valorAnterior + 1);
					}
					
					mapaRetorno.put(match.getNomePartida(), mapaValores);
				}
			}
		}

		final Either<RuntimeException, Map<String, Map<TeamType, Integer>>> toReturn = Either.createRight(mapaRetorno);
		response.addParameter("retorno", toJson(toReturn));
	}
	
	/**
	 * Procura uma partida, retornando ou a partida ou uma exceção
	 * @param nomePartida A partida a ser procurada
	 * @return Uma instância de {@link Either} que irá conter a partida ou a exceção
	 */
	private Either<RuntimeException, Match> findMatch(final String nomePartida)
	{
		Match theMatch;
		
		synchronized(lock)
		{
			theMatch = mapaPartidas.get(nomePartida);
		}

		if (theMatch == null)
		{
			final RuntimeException theValue = new IllegalArgumentException("A partida procurada não existe");
			return Either.createLeft(theValue);
		}
		else if ((theMatch.getFimPartida().getTime() + CLEANUP_TIME) < new Date().getTime())
		{
			//A partida existia, mas já terminou faz tempo. Nesse caso, deve-se removê-la
			synchronized(lock)
			{
				theMatch = mapaPartidas.remove(nomePartida);
			}
			final RuntimeException theValue = new IllegalArgumentException("A partida procurada não existe");
			return Either.createLeft(theValue);
		}

		return Either.createRight(theMatch);
	}
	
	/**
	 * Notifica todos os clientes de uma atualização no servidor
	 * @return Um Either que contém uma exceção ou um boolean true
	 */
	private Either<Exception, Boolean> notifyClients(final String nomePartida, final String stateJson)
	{
		//A chamada genérica, a mesma para todos eles
		final Call call = new Call(CLIENT_DRIVER_NAME, "runState");
		call.addParameter("nomePartida", nomePartida);
		call.addParameter("state", stateJson);

		//TODO: granularizar o lock se for o caso. De qualquer forma assim está bom porquê
		//chamadas de notify devem esperar umas às outras
		synchronized(lock)
		{
			final Match partida = mapaPartidas.get(nomePartida);
			
			if (partida == null)
			{
				final Exception exception = new IllegalArgumentException("Não existe partida para o nome passado");
				return Either.createLeft(exception);
			}
			
			final Function1<Player, String> lift = Reflections.lift(Player.class, "getIdJogador");
			final SetSharp<String> jogadores = SetSharp.from(partida.getJogadores()).map(lift);

			for(DriverData curDriver : gateway.listDrivers(CLIENT_DRIVER_NAME))
			{
				//TODO: Perceba que não há garantia de ser enviado para todos os jogadores.
				//E também não é verificada a resolução da mensagem (erro ou não)
				if (jogadores.contains(curDriver.getDevice().getName()))
				{
					try
					{
						gateway.callService(curDriver.getDevice(), call);
					}
					catch (ServiceCallException e)
					{
						return Either.createLeft((Exception) e);
					}
				}
			}
		}
		return Either.createRight(true);
	}
}

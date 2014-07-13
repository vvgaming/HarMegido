package org.vvgaming.harmegido.server;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;
import static org.vvgaming.harmegido.lib.util.JSONTransformer.toJson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.vvgaming.harmegido.lib.async.AsyncCall;
import org.vvgaming.harmegido.lib.async.AsyncCallService;
import org.vvgaming.harmegido.lib.model.EnchantmentImage;
import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.Match.MatchDuration;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.lib.model.Scoreboard;
import org.vvgaming.harmegido.lib.model.TeamType;
import org.vvgaming.harmegido.lib.model.match.MatchState;
import org.vvgaming.harmegido.lib.model.match.PlayerChangeEnchant;

import com.github.detentor.codex.monads.Either;
import com.github.detentor.codex.product.Tuple2;

/**
 * O driver do servidor, provendo todos os seus serviços. <br/>
 * O contrato de cada serviço deste driver exige que a resposta
 * será dada através do parâmetro de nome 'retorno', e seu objeto
 * será uma instância de {@link Either} que irá conter o retorno 
 * da chamada, ou a exceção no caso de algum erro.
 */
public class ServerDriver implements UosDriver
{
	//TODO: Extrair essa constante para alguma outra parte
	private static final String DRIVER_NAME = "uos.harmegido.server";
	private static final String CLIENT_DRIVER_NAME = "uos.harmegido.client";
	
	//Tempo a esperar para remover uma partida que já acabou
	private static final long CLEANUP_TIME = 30000;
	
	//Segura a referência ao Gateway
	private Gateway gateway;
	private UpDriver definition;
	private AsyncCallService messenger = null;
	private Map<String, Match> mapaPartidas = new HashMap<String, Match>();

	public ServerDriver()
	{
		definition = new UpDriver(DRIVER_NAME);

		//TODO: Adicionar os serviços aqui
		definition.addService("criarPartida");
		definition.addService("encontrarPartida");
		definition.addService("getPontuacao");
		definition.addService("runState");
		definition.addService("listarPartidas");
		definition.addService("listarJogadores");
		definition.addService("getHoraServidor");
	}

	public void init(Gateway gateway, InitialProperties properties, String instanceId)
	{
		this.gateway = gateway;
		messenger = new AsyncCallService(gateway);
	}
	
	public void destroy()
	{
		messenger.end();
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
	 * Retorna o tempo do servidor, para propósitos de sincronização.
	 * Parâmetros: []
	 * Retorno: Date em Json
	 */
	public void getHoraServidor(Call call, Response response, CallContext callContext)
	{
		response.addParameter("retorno", toJson(Either.createRight(new Date())));
	}

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
			synchronized(mapaPartidas)
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
		
		synchronized(mapaPartidas)
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
	 * Retorna a pontuação de uma partida. <br/>
	 * Parâmetros: nomePartida
	 */
	public void getPontuacao(Call call, Response response, CallContext callContext)
	{
		final String nomePartida = call.getParameter("nomePartida").toString();
		final Either<RuntimeException, Match> eMatch = findMatch(nomePartida);
		
		Either<RuntimeException, Scoreboard> toReturn;
		
		if (eMatch.isLeft())
		{
			toReturn = Either.createLeft(eMatch.getLeft());
		}
		else
		{
			toReturn = Either.createRight(Scoreboard.from(eMatch.getRight()));
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
			Object aRetornar = null;
			
			final Date executionTime = new Date();
			
			//Efetua a alteração
			synchronized(mapaPartidas)
			{
				try
				{
					eMatch.getRight().executarMudanca(state, executionTime);
					aRetornar = Either.createRight(true);
				}
				catch (Exception e)
				{
					aRetornar = Either.createLeft(e);
				}
			}

			response.addParameter("retorno", toJson(aRetornar));

			//Notifica todo o tipo de alteração
			notifyClients(nomePartida, state, executionTime);
		}
	}
	
	/**
	 * Lista todas as partidas ativas neste momento
	 */
	public void listarPartidas(Call call, Response response, CallContext callContext)
	{
		final List<String> listaPartidas = new ArrayList<String>();
		
		//Trava até terminar de ler
		synchronized(mapaPartidas)
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
		final List<Tuple2<String, List<Tuple2<TeamType, Integer>>>> listaRetorno = 
				new ArrayList<Tuple2<String,List<Tuple2<TeamType,Integer>>>>();
		
		//Trava até terminar de ler
		synchronized(mapaPartidas)
		{
			for (Match match : mapaPartidas.values())
			{
				if (match.isAtiva())
				{
					final List<Tuple2<TeamType, Integer>> listaValores = new ArrayList<Tuple2<TeamType,Integer>>();
					
					//Inicializa o mapa
					for (TeamType tipoTime : TeamType.values())
					{
						listaValores.add(Tuple2.from(tipoTime, 0));
					}

					//Conta quantos jogadores em cada time
					for (Player curJogador : match.getJogadores())
					{
						final Tuple2<TeamType, Integer> valorAnterior = listaValores.get(curJogador.getTime().ordinal());
						valorAnterior.setVal2(valorAnterior.getVal2() + 1);
					}
					
					listaRetorno.add(Tuple2.from(match.getNomePartida(), listaValores));
				}
			}
		}

		final Either<RuntimeException, List<Tuple2<String, List<Tuple2<TeamType, Integer>>>>> toReturn = Either.createRight(listaRetorno);
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
		
		synchronized(mapaPartidas)
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
			synchronized(mapaPartidas)
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
	 * @param executionTime 
	 * @return Um Either que contém uma exceção ou um boolean true
	 */
	private void notifyClients(final String nomePartida, final MatchState state, Date executionTime)
	{
		final List<Tuple2<Player, UpDevice>> jogadoresNotificar = getPlayersToNotify(nomePartida);
		final Set<AsyncCall> callsToDo = new HashSet<AsyncCall>();
		
		for (Tuple2<Player, UpDevice> curJogador : jogadoresNotificar)
		{
			final Call theCall = getCall(nomePartida, executionTime, state, curJogador.getVal1());
			callsToDo.add(AsyncCall.from(curJogador.getVal2(), theCall));
		}

		//Dispara as mensagens assíncronamente
		messenger.addCalls(callsToDo);
	}
	
	/**
	 * Retorna um conjunto de jogadores a serem notificados sobre a alteração na partida passada como parâmetro
	 * @param nomePartida O nome da partida que os jogadores devem pertencer
	 * @return Um conjunto de jogadores a serem notificados sobre alterações nesta partida
	 */
	private List<Tuple2<Player, UpDevice>> getPlayersToNotify(final String nomePartida)
	{
		Match partida = null;
		
		synchronized(mapaPartidas)
		{
			partida = mapaPartidas.get(nomePartida);
		}
		
		//Não existe partida com o nome, sai
		if (partida == null)
		{
			return new ArrayList<Tuple2<Player,UpDevice>>();
		}
		
		List<Player> listaJogadores = new ArrayList<Player>();
		
		synchronized (mapaPartidas)
		{
			listaJogadores = partida.getJogadores();
		}
		
		final Map<String, Player> mapaJogadores = new HashMap<String, Player>();
		
		for (Player jogador : listaJogadores)
		{
			mapaJogadores.put(jogador.getIdJogador(), jogador);
		}
		
		final List<DriverData> listDrivers = gateway.listDrivers(CLIENT_DRIVER_NAME);
		final List<Tuple2<Player, UpDevice>> playersToCall = new ArrayList<Tuple2<Player,UpDevice>>();
		
		for (DriverData curDriver : listDrivers)
		{
			final String idJogador = curDriver.getDevice().getName();
			Player jogador = mapaJogadores.get(idJogador);
			
			if (jogador != null)
			{
				playersToCall.add(Tuple2.from(jogador, curDriver.getDevice()));
			}
		}
		
		return playersToCall;
	}
	
	/**
	 * Retorna uma chamada específica
	 * @param nomePartida
	 * @param executionTime
	 * @param state
	 * @param forPlayer
	 * @return
	 */
	private Call getCall(final String nomePartida, final Date executionTime, final MatchState state, final Player forPlayer)
	{
		MatchState novoEstado = state;
		
		if (state instanceof PlayerChangeEnchant)
		{
			final PlayerChangeEnchant pce = (PlayerChangeEnchant) state;
			final TeamType timeJogador = pce.getJogador().getTime();

			//Se o time for igual, é amigo, daí remove a informação do encantamento
			if (forPlayer.getTime().equals(timeJogador))
			{
				novoEstado = pce.createCopy(EnchantmentImage.dummy);
			}
		}
		
		//A chamada genérica, a mesma para todos eles
		final Call call = new Call(CLIENT_DRIVER_NAME, "runState");
		call.addParameter("nomePartida", nomePartida);
		call.addParameter("executionTime", toJson(executionTime));
		call.addParameter("state", toJson(novoEstado));
		
		return call;
	}
	
//	private final static Function1<Player, String> lift = Reflections.lift(Player.class, "getIdJogador");
	
//	/**
//	 * Notifica todos os clientes de uma atualização no servidor
//	 * @param executionTime 
//	 * @return Um Either que contém uma exceção ou um boolean true
//	 */
//	private void notifyClients(final String nomePartida, final String stateJson, Date executionTime)
//	{
//		//A chamada genérica, a mesma para todos eles
//		final Call call = new Call(CLIENT_DRIVER_NAME, "runState");
//		call.addParameter("nomePartida", nomePartida);
//		call.addParameter("state", stateJson);
//		call.addParameter("executionTime", toJson(executionTime));
//
//		final Match partida = mapaPartidas.get(nomePartida);
//		
//		//Não existe partida com o nome, sai
//		if (partida == null)
//		{
//			return;
//		}
//		
//		final SetSharp<String> jogadores = SetSharp.from(partida.getJogadores()).map(lift);
//		final List<DriverData> listDrivers = gateway.listDrivers(CLIENT_DRIVER_NAME);
//		final Set<AsyncCall> callsToDo = new HashSet<AsyncCall>();
//		
//		for(DriverData curDriver : listDrivers)
//		{
//			if (jogadores.contains(curDriver.getDevice().getName()))
//			{
//				callsToDo.add(AsyncCall.from(curDriver.getDevice(), call));
//			}
//		}
//		
//		//Dispara as mensagens assíncronamente
//		messenger.addCalls(callsToDo);
//	}
}

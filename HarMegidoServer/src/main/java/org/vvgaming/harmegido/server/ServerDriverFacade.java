package org.vvgaming.harmegido.server;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;

import java.util.List;

import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.Match.MatchDuration;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.lib.model.match.MatchState;

import com.github.detentor.codex.monads.Either;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.operations.ObjectOps;

/**
 * Classe de fachada para o ServerDriver, de modo a simplificar o seu uso. <br/>
 * Todos os métodos possíveis de serem utilizados pelo ServerDriver são métodos dessa classe.
 */
public class ServerDriverFacade
{
	private final UOS uos;
	private final ServerDriver serverDriver;
	
	protected ServerDriverFacade(final UOS uos)
	{
		super();
		this.uos = uos;
		this.serverDriver = new ServerDriver();
	}

	/**
	 * Cria um facade (fachada) para o driver do servidor a partir do UOS passado como parâmetro.
	 * 
	 * @param uos A instância do UOS a ser utilizada para a fachada
	 * @return Uma instância da fachada do servidor
	 */
	public static ServerDriverFacade from(final UOS uos)
	{
		if (uos == null)
		{
			throw new IllegalArgumentException("A instância do uos não pode ser nula");
		}
		if (uos.getGateway() == null)
		{
			throw new IllegalArgumentException("Erro: Gateway do uos não pode retornar null");
		}
		return new ServerDriverFacade(uos);
	}
	
	
	/**
	 * Cria uma partida no servidor para o nome e duração informados
	 * @param nomePartida O nome a ser vinculado com a partida. Não pode coincidir com o nome de nenhuma
	 * outra partida.
	 * @param duracao A duração da partida
	 * @return Uma instância de {@link Either} que irá conter em Right a partida criada, ou Left a exceção que 
	 * aconteceu. 
	 */
	@SuppressWarnings("unchecked")
	public Either<Exception, Match> criarPartida(final String nomePartida, final MatchDuration duracao)
	{
		final Tuple2<String, Object> arg1 = Tuple2.<String, Object> from("nomePartida", nomePartida);
		final Tuple2<String, Object> arg2 = Tuple2.<String, Object> from("duracao", duracao.toString());
		
		final Either<Exception, Response> response = callService("criarPartida", arg1, arg2);
		
		if (response.isLeft())
		{
			return Either.createLeft(response.getLeft());
		}
		
		final String jsonStr = response.getRight().getResponseData("partida").toString();
		return Either.createRight(fromJson(jsonStr, Match.class));
	}
	
	/**
	 * Adiciona o jogador na partida informada
	 * @param partida A partida à qual o jogador deve ser vinculado
	 * @param jogador O jogador a ser vinculado à partida
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se o jogador foi adicionado, ou a exceção
	 * em caso contrário
	 */
	@SuppressWarnings("unchecked")
	public Either<Exception, Boolean> adicionarJogador(final Match partida, final Player jogador)
	{
		final Tuple2<String, Object> arg1 = Tuple2.<String, Object> from("nomePartida", partida.getNomePartida());
		final Tuple2<String, Object> arg2 = Tuple2.<String, Object> from("idJogador", jogador.getIdJogador());
		
		MatchState.adicionarJogador(jogador);
		
		final Either<Exception, Response> response = callService("adicionarJogador", arg1, arg2);
		
		if (response.isLeft())
		{
			return Either.createLeft(response.getLeft());
		}
		return Either.createRight(true);
	}
	
	
	
	

	/**
	 * Retorna um dispositivo a partir do nome do driver. <br/>
	 * 
	 * @param driver O driver a partir do qual o dispositivo será encontrado
	 * @return Um dispositivo que corresponde ao driver
	 * @throws RuntimeException Se não houver dispositivo corresponde ao driver
	 */
	private UpDevice deviceFromDriver(final UosDriver driver)
	{
		final List<DriverData> listDrivers = uos.getGateway().listDrivers(driver.getDriver().getName());

		if (listDrivers == null || listDrivers.isEmpty())
		{
			throw new RuntimeException("Nenhum dispositivo encontrado para o driver passado como parâmetro");
		}

		return listDrivers.get(0).getDevice();
	}

	/**
	 * Chamada genérica para um serviço deste server.
	 * 
	 * @param serviceName O nome do servi�o a ser chamado
	 * @param params Os parâmetros a serem repassados para o serviço
	 * @return Uma instância de {@link Either} que irá conter o retorno ou a exceção no caso de erro.
	 */
	private Either<Exception, Response> callService(final String serviceName, final Tuple2<String, Object>... params)
	{
		final Call call = new Call(serverDriver.getDriver().getName(), serviceName);

		if (params != null)
		{
			for (Tuple2<String, Object> curParam : params)
			{
				call.addParameter(curParam.getVal1(), curParam.getVal2());
			}
		}

		try
		{
			final Response response = uos.getGateway().callService(deviceFromDriver(serverDriver), call);
			return Either.createRight(response);
		}
		catch (Exception e)
		{
			return Either.createLeft(e);
		}
	}

	/**
	 * Envia uma mensagem para o driver do servidor
	 * 
	 * @param message A mensagem a ser enviada
	 * @return Uma instância de {@link Either} que irá conter a mensagem de retorno ou a exceção no caso de erro.
	 */
	@SuppressWarnings("unchecked")
	public Either<Exception, String> sendMessage(final String message)
	{
		return callService("sendMessage", Tuple2.<String, Object> from("message", message)).map(ObjectOps.toString);
	}
}

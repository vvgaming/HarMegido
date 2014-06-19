package org.vvgaming.harmegido.uos;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;
import static org.vvgaming.harmegido.lib.util.JSONTransformer.toJson;

import java.util.List;

import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.vvgaming.harmegido.lib.model.Enchantment;
import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.Match.MatchDuration;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.lib.model.TeamType;
import org.vvgaming.harmegido.lib.model.match.MatchState;
import org.vvgaming.harmegido.lib.util.EnchantmentImage;

import com.github.detentor.codex.monads.Either;
import com.github.detentor.codex.product.Tuple2;

/**
 * Classe de fachada para o ServerDriver, de modo a simplificar o seu uso. <br/>
 * Todos os métodos possíveis de serem utilizados pelo ServerDriver são métodos dessa classe.
 */
public class ServerDriverFacade
{
	private static final String HAR_MEGIDO_DRIVER = "uos.harmegido.server";
	private final UOS uos;
	private final UpDevice device;
	

	protected ServerDriverFacade(final UOS uos, final UpDevice device)
	{
		super();
		this.uos = uos;
		this.device = device;
	}

	/**
	 * Cria um facade (fachada) para o driver do servidor a partir do UOS passado como parâmetro.
	 * 
	 * @param uos A instância do UOS a ser utilizada para a fachada
	 * @return Uma instância da fachada do servidor
	 */
	public static ServerDriverFacade from(final UOS uos, final long timeOut)
	{
		if (uos == null)
		{
			throw new IllegalArgumentException("A instância do uos não pode ser nula");
		}
		
		if (uos.getGateway() == null)
		{
			throw new IllegalStateException("Erro: Gateway do uos não pode retornar null");
		}
		
		long startTime = System.currentTimeMillis();
		List<DriverData> drivers;
		do{
			drivers = uos.getGateway().listDrivers(HAR_MEGIDO_DRIVER);
		}while((drivers == null || drivers.isEmpty()) && System.currentTimeMillis() - startTime < timeOut );
		
		if ((drivers == null || drivers.isEmpty()))
		{
			throw new IllegalStateException("Não foi encontrado o driver do Har Megido na instância do uos");
		}
		
		if (drivers.size() > 1)
		{
			throw new IllegalStateException("Mais de um driver encontrado para o Har Megido na instância do uos");
		}
		
		return new ServerDriverFacade(uos, drivers.get(0).getDevice());
	}
	
	public static ServerDriverFacade from(final UOS uos)
	{
		return from(uos, 5000);
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
	public Either<Exception, Boolean> adicionarJogador(final Match partida, final Player jogador)
	{
		return executarEstado(partida, MatchState.adicionarJogador(jogador));
	}
	
	/**
	 * Remove um jogador da partida informada
	 * @param partida A partida à qual o jogador deve ser desvinculado
	 * @param jogador O jogador a ser desvinculado da partida
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se o jogador foi removido, ou a exceção
	 * em caso contrário
	 */
	public Either<Exception, Boolean> removerJogador(final Match partida, final Player jogador)
	{
		return executarEstado(partida, MatchState.removerJogador(jogador));
	}
	
	/**
	 * Muda o time de um jogador da partida informada
	 * @param partida A partida que o jogador pertence
	 * @param jogador O jogador cujo time será alterado
	 * @param novoTime O novo time do jogador
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se o jogador teve o time trocado, ou a exceção
	 * em caso contrário
	 */
	public Either<Exception, Boolean> mudarTime(final Match partida, final Player jogador, final TeamType novoTime)
	{
		return executarEstado(partida, MatchState.mudarTime(jogador, novoTime));
	}
	
	/**
	 * Cria um encantamento para o jogador e a imagem informados
	 * @param partida A partida que o jogador pertence
	 * @param jogador O jogador que está efetuando o encantamento
	 * @param imagem A imagem do objeto encantado
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se o jogador encantou o objeto, ou a exceção
	 * em caso contrário
	 */
	public Either<Exception, Boolean> encantarObjeto(final Match partida, final Player jogador, final EnchantmentImage imagem)
	{
		return executarEstado(partida, MatchState.encantar(jogador, imagem));
	}
	
	/**
	 * Cria um desencantamento para o jogador e o encantamento informados
	 * @param partida A partida que o jogador pertence
	 * @param jogador O jogador que está efetuando o encantamento
	 * @param encantamento O encantamento a ser desencantado
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se o jogador desencantou o objeto, ou a exceção
	 * em caso contrário
	 */
	public Either<Exception, Boolean> desencantarObjeto(final Match partida, final Player jogador, final Enchantment encantamento)
	{
		return executarEstado(partida, MatchState.desencantar(jogador, encantamento));
	}
	
	/**
	 * Código comum a todos os métodos que fazem mudança a partir de uma mudança de estado
	 * @param partida A partida a ser alterada
	 * @param mState O estado que contém a alteração a ser executada
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se houve sucesso, ou a exceção
	 * que ocorreu em caso contrário 
	 */
	@SuppressWarnings("unchecked")
	private Either<Exception, Boolean> executarEstado(final Match partida, final MatchState mState)
	{
		final Tuple2<String, Object> arg1 = Tuple2.<String, Object> from("nomePartida", partida.getNomePartida());
		final Tuple2<String, Object> arg2 = Tuple2.<String, Object> from("state", toJson(mState));
		
		final Either<Exception, Response> response = callService("runState", arg1, arg2);
		
		if (response.isLeft())
		{
			return Either.createLeft(response.getLeft());
		}
		return Either.createRight(true);
	}
	
	/**
	 * Lista todas as partidas neste momento
	 * @return Uma instância de {@link Either} que conterá uma lista com o nome das partidas ativas,
	 * ou a exceção em caso de erro
	 */
	@SuppressWarnings("unchecked")
	public Either<Exception, List<String>> listarPartidas()
	{
		final Either<Exception, Response> response = callService("listarPartidas");
		
		if (response.isLeft())
		{
			return Either.createLeft(response.getLeft());
		}

		final List<String> fromJson = fromJson(response.getRight().getResponseData("retorno").toString(), List.class);
		return Either.createRight(fromJson);
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
		final Call call = new Call(HAR_MEGIDO_DRIVER, serviceName);

		if (params != null)
		{
			for (Tuple2<String, Object> curParam : params)
			{
				call.addParameter(curParam.getVal1(), curParam.getVal2());
			}
		}

		try
		{
			return Either.createRight(uos.getGateway().callService(device, call));
		}
		catch (Exception e)
		{
			return Either.createLeft(e);
		}
	}
}

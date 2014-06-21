package org.vvgaming.harmegido.uos;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;
import static org.vvgaming.harmegido.lib.util.JSONTransformer.toJson;

import java.util.List;
import java.util.Map;

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
	 * @param timeout O tempo a esperar (em milisegundos) até desistir de encontrar o driver
	 * @return Uma instância da fachada do servidor
	 */
	public static ServerDriverFacade from(final UOS uos, final long timeout)
	{
		if (uos == null)
		{
			throw new IllegalArgumentException("A instância do uos não pode ser nula");
		}
		
		if (uos.getGateway() == null)
		{
			throw new IllegalStateException("Erro: Gateway do uos não pode retornar null");
		}
		
		final List<DriverData> drivers = waitForDrivers(uos, timeout);

		if (drivers.isEmpty())
		{
			throw new IllegalStateException("Não foi encontrado o driver do Har Megido na instância do uos");
		}
		
		if (drivers.size() > 1)
		{
			throw new IllegalStateException("Mais de um driver encontrado para o Har Megido na instância do uos");
		}
		
		return new ServerDriverFacade(uos, drivers.get(0).getDevice());
	}
	
	/**
	 * Cria um facade (fachada) para o driver do servidor a partir do UOS passado como parâmetro, 
	 * esperando até 5 segundos pelo driver ficar pronto.
	 * 
	 * @param uos A instância do UOS a ser utilizada para a fachada
	 * @return Uma instância da fachada do servidor
	 */
	public static ServerDriverFacade from(final UOS uos)
	{
		return from(uos, 5000);
	}
	
	/**
	 * Espera por drivers uma quantidade de tempo (em milisegundos) definida em timeout
	 * @param uos A instância do UOS de onde o driver será retirado
	 * @param timeout O tempo (em milisegundos) a esperar pelo driver
	 * @return Uma lista de drivers (potencialmente vazia) retornado pelo gateway do uos
	 */
	private static List<DriverData> waitForDrivers(final UOS uos, final long timeout)
	{
		final long startTime = System.currentTimeMillis();
		List<DriverData> drivers;
		
		while(  (drivers = uos.getGateway().listDrivers(HAR_MEGIDO_DRIVER)).isEmpty() &&
				(System.currentTimeMillis() - startTime < timeout))
		{
		}

		return drivers;
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

		return callServiceUnwrap("criarPartida", arg1, arg2);
	}

	/**
	 * Encontra a partida ativa que o jogador passado como parâmetro está jogando, se ela existir
	 * @param jogador O jogador cuja partida será buscada
	 * @return Uma instância de {@link Either} que conterá a partida, ou uma exceção em caso contrário
	 */
	@SuppressWarnings("unchecked")
	public Either<Exception, Match> encontrarPartida(final Player jogador)
	{
		final Tuple2<String, Object> arg1 = Tuple2.<String, Object> from("idJogador", jogador.getIdJogador());
		return callServiceUnwrap("encontrarPartida", arg1);
	}

	/**
	 * Adiciona o jogador na partida informada
	 * @param nomePartida O nome da partida à qual o jogador deve ser vinculado
	 * @param jogador O jogador a ser vinculado à partida
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se o jogador foi adicionado, ou a exceção
	 * em caso contrário
	 */
	public Either<Exception, Boolean> adicionarJogador(final String nomePartida, final Player jogador)
	{
		return executarEstado(nomePartida, MatchState.adicionarJogador(jogador));
	}
	
	/**
	 * Remove um jogador da partida informada
	 * @param nomePartida O nome da partida à qual o jogador deve ser desvinculado
	 * @param jogador O jogador a ser desvinculado da partida
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se o jogador foi removido, ou a exceção
	 * em caso contrário
	 */
	public Either<Exception, Boolean> removerJogador(final String nomePartida, final Player jogador)
	{
		return executarEstado(nomePartida, MatchState.removerJogador(jogador));
	}
	
	/**
	 * Muda o time de um jogador da partida informada
	 * @param nomePartida O nome da partida que o jogador pertence
	 * @param jogador O jogador cujo time será alterado
	 * @param novoTime O novo time do jogador
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se o jogador teve o time trocado, ou a exceção
	 * em caso contrário
	 */
	public Either<Exception, Boolean> mudarTime(final String nomePartida, final Player jogador, final TeamType novoTime)
	{
		return executarEstado(nomePartida, MatchState.mudarTime(jogador, novoTime));
	}
	
	/**
	 * Cria um encantamento para o jogador e a imagem informados
	 * @param nomePartida O nome da partida que o jogador pertence
	 * @param jogador O jogador que está efetuando o encantamento
	 * @param imagem A imagem do objeto encantado
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se o jogador encantou o objeto, ou a exceção
	 * em caso contrário
	 */
	public Either<Exception, Boolean> encantarObjeto(final String nomePartida, final Player jogador, final EnchantmentImage imagem)
	{
		return executarEstado(nomePartida, MatchState.encantar(jogador, imagem));
	}
	
	/**
	 * Cria um desencantamento para o jogador e o encantamento informados
	 * @param nomePartida O nome da partida que o jogador pertence
	 * @param jogador O jogador que está efetuando o encantamento
	 * @param encantamento O encantamento a ser desencantado
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se o jogador desencantou o objeto, ou a exceção
	 * em caso contrário
	 */
	public Either<Exception, Boolean> desencantarObjeto(final String nomePartida, final Player jogador, final Enchantment encantamento)
	{
		return executarEstado(nomePartida, MatchState.desencantar(jogador, encantamento));
	}
	
	/**
	 * Código comum a todos os métodos que fazem mudança a partir de uma mudança de estado
	 * @param partida A partida a ser alterada
	 * @param mState O estado que contém a alteração a ser executada
	 * @return Uma instância de {@link Either} que conterá <tt>true</tt> se houve sucesso, ou a exceção
	 * que ocorreu em caso contrário 
	 */
	@SuppressWarnings("unchecked")
	private Either<Exception, Boolean> executarEstado(final String nomePartida, final MatchState mState)
	{
		final Tuple2<String, Object> arg1 = Tuple2.<String, Object> from("nomePartida", nomePartida);
		final Tuple2<String, Object> arg2 = Tuple2.<String, Object> from("state", toJson(mState));
		
		return callServiceUnwrap("runState", arg1, arg2);
	}

	/**
	 * Lista todas as partidas neste momento
	 * @return Uma instância de {@link Either} que conterá uma lista com o nome das partidas ativas,
	 * ou a exceção em caso de erro
	 */
	@SuppressWarnings("unchecked")
	public Either<Exception, List<String>> listarPartidas()
	{
		return callServiceUnwrap("listarPartidas");
	}

	/**
	 * Lista todas as partidas e o número de jogadores em cada um dos times
	 * @return Um instância de {@link Either} que conterá a lista de todas as partidas e quantos jogadores
	 * de cada tipo existem nela, ou a exceção em caso de erro
	 */
	@SuppressWarnings("unchecked")
	public Either<Exception, List<Map<String, Map<TeamType, Integer>>>> listarJogadores()
	{
		return callServiceUnwrap("listarJogadores");
	}

	/**
	 * Chama um serviço do server driver, retornando a resposta. <br/>
	 * ATENÇÃO: Esse método exige que o serviço chamado retorne um atributo de nome "retorno". Do contrário
	 * será disparada uma exceção.
	 * Para os casos que houver um atributo de resposta de nome "retorno", será retornado o seu valor. <br/>
	 * Em caso contrário, será retornado uma exceção.
	 * 
	 * @param serviceName O nome do serviço a ser chamado
	 * @param params Os parâmetros a serem repassados para o serviço
	 * @return Uma instância de {@link Either} que irá conter a resposta ou a exceção no caso de erro.
	 */
	@SuppressWarnings("unchecked")
	private <A> Either<Exception, A> callServiceUnwrap(final String serviceName, final Tuple2<String, Object>... params)
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
			final Response response = uos.getGateway().callService(device, call);
			final Object responseData = response.getResponseData("retorno");

			//Se houver um parâmetro 'retorno', é porquê a resposta é uma instância de Either.
			//Nesse caso, Deve-se verificar se é uma exceção, para evitar o wrap da exceção
			if (responseData == null)
			{
				final String mensagem = "O serviço " + serviceName + " não possui um atributo de resposta de nome 'retorno'";
				final Exception exception = new IllegalStateException(mensagem);
				return Either.createLeft(exception);
			}
			//Repassa o Either extraído do retorno
			return (Either<Exception, A>) fromJson(responseData.toString(), Either.class);
		}
		catch (Exception e)
		{
			return Either.createLeft(e);
		}
	}
}

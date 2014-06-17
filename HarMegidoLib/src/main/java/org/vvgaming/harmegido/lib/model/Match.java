package org.vvgaming.harmegido.lib.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.vvgaming.harmegido.lib.model.match.MatchState;
import org.vvgaming.harmegido.lib.model.match.PlayerChangeAdd;
import org.vvgaming.harmegido.lib.model.match.PlayerChangeDisenchant;
import org.vvgaming.harmegido.lib.model.match.PlayerChangeEnchant;
import org.vvgaming.harmegido.lib.model.match.PlayerChangeRemove;
import org.vvgaming.harmegido.lib.model.match.PlayerChangeTeam;

/**
 * Representa uma partida no HarMegido
 */
public class Match
{
	private final Date inicioPartida;
	private final MatchDuration duracao;

	// Usando mapa porque o Set não tem método get
	private final Map<Player, Player> jogadores = new HashMap<Player, Player>();
	private final Set<Enchantment> encantamentos = new HashSet<Enchantment>();

	private Match(final Date inicio, final MatchDuration duracao)
	{
		this.inicioPartida = inicio;
		this.duracao = duracao;
	}

	/**
	 * Cria uma nova partida, com o início e duração passados como parâmetro
	 * 
	 * @param inicio O instante no tempo que a partida teve/terá início
	 * @param duracao A duração da partida
	 * @return Uma partida com a duração e início passados como parâmetro
	 */
	public Match from(final Date inicio, final MatchDuration duracao)
	{
		if (inicio == null)
		{
			throw new IllegalArgumentException("O início não pode ser nulo");
		}

		if (duracao == null)
		{
			throw new IllegalArgumentException("A duração da partida não pode ser nula");
		}

		return new Match(inicio, duracao);
	}

	/**
	 * Retorna a pontuação dos time passado como parâmetro
	 * 
	 * @param time O time cuja pontuação deve ser retornada
	 * @return Um inteiro positivo que contém a pontuação do time passado como parâmetro
	 */
	public int getPontuacao(final TeamType time)
	{
		// TODO: calcular a pontuação
		return 0;
	}

	/**
	 * Retorna <tt>true</tt> se, e somente se, a partida ainda está ativa, ou seja, se o tempo para ela terminar ainda não passou, e nenhum
	 * time atingiu a pontuação para terminá-la.
	 * 
	 * @return <tt>true</tt> se a partida está ativa, ou <tt>false</tt> do contrário
	 */
	public boolean isAtiva()
	{
		return getEndTime() < new Date().getTime();
	}

	/**
	 * Retorna, em milis, quando a partida terminará
	 * 
	 * @return Um long que representa, em milisegundos, o instante que a partida terminá
	 */
	private long getEndTime()
	{
		return duracao.getInMilliseconds() + inicioPartida.getTime();
	}
	
	/**
	 * Executa a mudança de estado para esta partida, a partir da 
	 * mudança passada como parâmetro. 
	 * @param stateChange A classe que especifica qual tipo de mudança será feita nesta partida
	 */
	public void executeChange(final MatchState stateChange)
	{
		//Como StateChange é um ADT, apesar de feia, 
		//essa é a maneira correta de fazer 'Match' nos tipos possíveis
		if (stateChange instanceof PlayerChangeAdd)
		{
			final PlayerChangeAdd pca = (PlayerChangeAdd) stateChange;
			//modificações estruturais no jogador não refletirão no jogador da partida.
			jogadores.put(pca.getJogador(), pca.getJogador().copy());
		}
		else if (stateChange instanceof PlayerChangeRemove)
		{
			final PlayerChangeRemove pcr = (PlayerChangeRemove) stateChange;
			jogadores.remove(pcr.getJogador());
		}
		else if (stateChange instanceof PlayerChangeTeam)
		{
			final PlayerChangeTeam pct = (PlayerChangeTeam) stateChange;
			
			final Player mJogador = jogadores.get(pct.getJogador());

			if (mJogador == null)
			{
				throw new IllegalArgumentException("Erro: Jogador não pertence à partida");
			}
			mJogador.trocarDeTime(pct.getNovoTime());
		}
		else if (stateChange instanceof PlayerChangeEnchant)
		{
			final PlayerChangeEnchant pce = (PlayerChangeEnchant) stateChange;
			encantamentos.add(pce.getJogador().encantar(new Date(), pce.getEnchantmentImage()));
		}
		else if (stateChange instanceof PlayerChangeDisenchant)
		{
			final PlayerChangeDisenchant pcd = (PlayerChangeDisenchant) stateChange;
			//não precisa guardar porque o encantamento é desencantado como 'side-effect'
			pcd.getJogador().desencantar(pcd.getEncantamento(), new Date());
		}
		else
		{
			throw new IllegalArgumentException("O tipo de stateChange não é reconhecido: " + stateChange.getClass());
		}
	}

	/**
	 * Esse enum representa a duração de uma partida
	 */
	public enum MatchDuration
	{
		FIVE_MINUTES(5), TEN_MINUTES(10), FIFTEEN_MINUTES(15), UNLIMITED(Integer.MAX_VALUE);

		private final long inMilliseconds;

		private MatchDuration(final int minutes)
		{
			this.inMilliseconds = minutes * 60 * 1000;
		}

		public long getInMilliseconds()
		{
			return inMilliseconds;
		}
	}
}

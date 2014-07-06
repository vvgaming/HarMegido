package org.vvgaming.harmegido.lib.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	private final String matchName;
	private final Date inicioPartida;
	private final MatchDuration duracao;

	// Usando mapa porque o Set não tem método get
	private final Map<String, Player> jogadores = new HashMap<String, Player>();
	private final Set<Enchantment> encantamentos = new HashSet<Enchantment>();

	private Match(final String nomePartida, final Date inicio, final MatchDuration duracao)
	{
		this.matchName = nomePartida;
		this.inicioPartida = new Date(inicio.getTime());
		this.duracao = duracao;
	}

	/**
	 * Cria uma nova partida, com o início e duração passados como parâmetro
	 * 
	 * @param nomePartida O nome da partida
	 * @param inicio O instante no tempo que a partida teve/terá início
	 * @param duracao A duração da partida
	 * @return Uma partida com a duração e início passados como parâmetro
	 */
	public static Match from(final String nomePartida, final Date inicio, final MatchDuration duracao)
	{
		if (nomePartida == null)
		{
			throw new IllegalArgumentException("O nome da partida não pode ser nulo");
		}

		if (inicio == null)
		{
			throw new IllegalArgumentException("O início não pode ser nulo");
		}

		if (duracao == null)
		{
			throw new IllegalArgumentException("A duração da partida não pode ser nula");
		}

		return new Match(nomePartida, inicio, duracao);
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
		//TODO: Colocar a restrição da pontuação
		return getHoraFimMilis() > new Date().getTime();
	}
	
	/**
	 * Retorna o instante no tempo que a partida foi iniciada.
	 * @return Uma instância de date que contém o instante no tempo que a partida foi iniciada
	 */
	public Date getInicioPartida()
	{
		return new Date(inicioPartida.getTime());
	}
	
	/**
	 * Retorna o instante no tempo que a partida terminará.
	 * @return Uma instância de date que contém o instante no tempo que a partida terminará
	 */
	public Date getFimPartida()
	{
		return new Date(getHoraFimMilis());
	}

	/**
	 * Retorna, em milis, quando a partida terminará
	 * 
	 * @return Um long que representa, em milisegundos, o instante que a partida terminá
	 */
	private long getHoraFimMilis()
	{
		return duracao.getInMilliseconds() + inicioPartida.getTime();
	}

	/**
	 * Retorna <tt>true</tt> se esta partida contém o jogador passado como parâmetro
	 * 
	 * @param idJogador O identificador do jogador a ser verificado se pertence a essa partida
	 * @return <tt>true</tt> se esta partida contém o jogador, ou <tt>false</tt> do contrário
	 */
	public boolean contemJogador(final String idJogador)
	{
		return jogadores.containsKey(idJogador);
	}
	
	/**
	 * Retorna uma lista de jogadores contidos nesta partida. <br/>
	 * ATENÇÃO: Será retornado uma cópia dos jogadores, portanto mudanças estruturais nos jogadores
	 * retornados não afetarão esta partida.
	 * @return Uma lista com os jogadores desta partida.
	 */
	public List<Player> getJogadores()
	{
		final List<Player> toReturn = new ArrayList<Player>();
		
		for (Player jogador : jogadores.values())
		{
			toReturn.add(jogador.copy());
		}
		return toReturn;
	}

	/**
	 * Executa a mudança de estado para esta partida, a partir da mudança passada como parâmetro.
	 * 
	 * @param stateChange A classe que especifica qual tipo de mudança será feita nesta partida
	 */
	public void executarMudanca(final MatchState stateChange)
	{
		// Como StateChange é um ADT, apesar de feia,
		// essa é a maneira correta de fazer 'Match' nos tipos possíveis
		if (stateChange instanceof PlayerChangeAdd)
		{
			final PlayerChangeAdd pca = (PlayerChangeAdd) stateChange;
			// modificações estruturais no jogador não refletirão no jogador da partida.
			jogadores.put(pca.getJogador().getIdJogador(), pca.getJogador().copy());
		}
		else if (stateChange instanceof PlayerChangeRemove)
		{
			final PlayerChangeRemove pcr = (PlayerChangeRemove) stateChange;
			jogadores.remove(pcr.getJogador());
		}
		else if (stateChange instanceof PlayerChangeTeam)
		{
			final PlayerChangeTeam pct = (PlayerChangeTeam) stateChange;
			final Player mJogador = jogadores.get(pct.getJogador().getIdJogador());

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
			// não precisa guardar porque o encantamento é desencantado como 'side-effect'
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
		
		/**
		 * Tenta criar uma duração de partida a partir de uma String
		 * @param theString A string que será transformada em duração de partida
		 * @return A representação desta string em uma duração de partida
		 * @throws IllegalArgumentException Se a string não corresponder a uma partida
		 */
		public static MatchDuration from(final String theString)
		{
			for (MatchDuration curMatch : MatchDuration.values())
			{
				if (curMatch.toString().equals(theString))
				{
					return curMatch;
				}
			}
			throw new IllegalArgumentException("Não foi possível fazer parse da string '" + theString + "' para uma duração de partida");
		}

		public long getInMilliseconds()
		{
			return inMilliseconds;
		}
		
		@Override
		public String toString()
		{
			switch (this)
			{
				case FIVE_MINUTES:
				{
					return "Cinco minutos";
				}
				case TEN_MINUTES:
				{
					return "Dez minutos"; 
				}
				case FIFTEEN_MINUTES:
				{
					return "Quinze minutos";
				}
				case UNLIMITED:
				{
					return "Sem limite de tempo";
				}
				default:
				{
					throw new IllegalArgumentException("Tipo de duração não reconhecida: " + this.getClass());
				}
			}
		};
	}

	/**
	 * Retorna o nome associado com esta partida
	 * @return O nome associado à partida
	 */
	public String getNomePartida()
	{
		return matchName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (matchName == null ? 0 : matchName.hashCode());
		return result;
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
		final Match other = (Match) obj;
		if (matchName == null)
		{
			if (other.matchName != null)
			{
				return false;
			}
		}
		else if (!matchName.equals(other.matchName))
		{
			return false;
		}
		return true;
	}
}

package org.vvgaming.harmegido.lib.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Representa uma partida no HarMegido
 */
public class Match 
{
	private final Date inicioPartida;
	private final MatchDuration duracao;
	
	//Usando mapa porque o Set não tem método get
	private final Map<Player, Player> jogadores = new HashMap<Player, Player>();
	private final Set<Enchantment> encantamentos = new HashSet<Enchantment>();
	
	
	
	
	private Match(final Date inicio, final MatchDuration duracao)
	{
		this.inicioPartida = inicio;
		this.duracao = duracao;
	}
	
	/**
	 * Cria uma nova partida, com o início e duração passados como parâmetro
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
	 * @param time O time cuja pontuação deve ser retornada
	 * @return Um inteiro positivo que contém a pontuação do time passado como parâmetro
	 */
	public int getPontuacao(final TeamType time)
	{
		//TODO: calcular a pontuação
		return 0;
	}
	
	/**
	 * Retorna <tt>true</tt> se, e somente se, a partida ainda está ativa, ou seja,
	 * se o tempo para ela terminar ainda não passou, e nenhum time atingiu a 
	 * pontuação para terminá-la.
	 * 
	 * @return <tt>true</tt> se a partida está ativa, ou <tt>false</tt> do contrário
	 */
	public boolean isAtiva()
	{
		return getEndTime() < new Date().getTime();
	}
	
	/**
	 * Retorna, em milis, quando a partida terminará
	 * @return Um long que representa, em milisegundos, o instante que a partida terminá
	 */
	private long getEndTime()
	{
		return (duracao.getInMilliseconds() + inicioPartida.getTime());
	}
	
	/**
	 * Adiciona o jogador na partida. <br/>
	 * Em particular, será adicionada uma cópia do jogador, ou seja, modificações
	 * estruturais no jogador não refletirão no jogador da partida.
	 * @param jogador O jogador a ser adicionado na partida
	 */
	public void adicionarJogador(final Player jogador)
	{
		jogadores.put(jogador, jogador.copy());
	}

	/**
	 * Remove o jogador passado como parâmetro da partida
	 * @param jogador O jogador a ser removido da partida
	 * @return <tt>true</tt> se o jogador foi removido da partida, ou <tt>false</tt> do contrário
	 */
	public boolean removerJogador(final Player jogador)
	{
		return jogadores.remove(jogador) != null;
	}
	
	/**
	 * Troca de time o jogador passado como parâmetro. <br/>
	 * 
	 * @param jogador O jogador cujo time será trocado
	 * @param novoTime O novo time do jogador.
	 * @throws IllegalArgumentException Se o jogador não pertecer a esta partida
	 */
	public void trocarTime(final Player jogador, final TeamType novoTime)
	{
		final Player mJogador = jogadores.get(jogador);

		if (mJogador == null)
		{
			throw new IllegalArgumentException("Erro: Jogador não pertence à partida");
		}
		
		mJogador.trocarDeTime(novoTime);
	}

	/**
	 * Cria um encantamento para o jogador passado como parâmetro
	 * @param jogador O jogador a criar o encantamento
	 * @return O encantamento criado
	 */
	public Enchantment criarEncantamento(final Player jogador)
	{
		final Enchantment encantamento = jogador.encantar(new Date());
		encantamentos.add(encantamento);
		return encantamento;
	}
	
	/**
	 * Cria um desencantamento para o jogador e encantamentos passado como parâmetro
	 * @param jogador O jogador a fazer o desencantamento
	 * @param encantamento O encantamento a ser desencantado
	 * @return O desencantamento do jogador
	 */
	public Disenchantment criarDesencantamento(final Player jogador, final Enchantment encantamento)
	{
		return jogador.desencantar(encantamento, new Date());
	}
	
	/**
	 * Esse enum representa a duração de uma partida
	 */
	public enum MatchDuration
	{
		FIVE_MINUTES(5), TEN_MINUTES(10), FIFTEEN_MINUTES(15), UNLIMITED(Integer.MAX_VALUE);
		
		private final long inMilliseconds;
		
		private MatchDuration(int minutes) {
			this.inMilliseconds = minutes * 60 * 1000;
		}

		public long getInMilliseconds() {
			return inMilliseconds;
		}
		
	}

}

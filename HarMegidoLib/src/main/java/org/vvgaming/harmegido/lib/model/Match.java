package org.vvgaming.harmegido.lib.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Representa uma partida no HarMegido
 *
 */
public class Match 
{
	final Date inicioPartida;
	//Usando mapa porque o Set não tem método get
	final Map<Player, Player> jogadores = new HashMap<Player, Player>();
	
	//
	final Set<Enchantment> encantamentos = new HashSet<Enchantment>();
	
	public Match()
	{
		this.inicioPartida = new Date();
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

}

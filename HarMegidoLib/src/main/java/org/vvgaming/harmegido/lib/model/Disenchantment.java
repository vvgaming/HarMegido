package org.vvgaming.harmegido.lib.model;

import java.util.Date;

/**
 * Representa o desencantamento de algum encantamento. Imutável.<br/>
 */
public class Disenchantment
{
	private final Player jogador;
	private final Date timestamp;
	private final Enchantment encantamento;

	private Disenchantment(final Player jogador, final Date timestamp, final Enchantment encantamento)
	{
		super();
		this.jogador = jogador.copy(); // salva o estado do jogador na hora do desencantamento
		this.timestamp = new Date(timestamp.getTime());
		encantamento.desencantar(this); // faz o efeito colateral pra salvar o estado
		this.encantamento = encantamento.copy(); // salva o estado do encantamento
	}

	/**
	 * Cria um novo desencantamento, para o jogador, timestamp e encantamento passados como parâmetro. <br/>
	 * Como efeito colateral, o encantamento passado como parâmetro será alterado para refletir o desencantamento. <br/>
	 * 
	 * @param jogador O jogador responsável pelo desencantamento
	 * @param timestamp A data/hora do desencantamento
	 * @param encantamento O encantamento que está sendo desencantado
	 * @return Uma instância de {@link Disenchantment} que representa o desencantamento
	 * @throws IllegalArgumentException Se o jogador que estiver desencantando for do mesmo tipo que encantou
	 */
	public static Disenchantment from(final Player jogador, final Date timestamp, final Enchantment encantamento)
	{
		if (jogador.getTime().equals(encantamento.getJogador().getTime()))
		{
			throw new IllegalArgumentException("O time que desencanta deve ser diferente do time que encantou");
		}
		return new Disenchantment(jogador, timestamp, encantamento);
	}

	public Player getJogador()
	{
		return jogador;
	}

	public Date getTimestamp()
	{
		return timestamp;
	}

	public Enchantment getEncantamento()
	{
		return encantamento;
	}

	public int calcularPontuacao()
	{
		// TODO: efetivar o cálculo
		return 0;
	}
}

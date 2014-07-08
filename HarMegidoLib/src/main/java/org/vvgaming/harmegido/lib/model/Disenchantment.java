package org.vvgaming.harmegido.lib.model;

import java.util.Date;

/**
 * Representa o desencantamento de algum encantamento. Imutável.<br/>
 */
public class Disenchantment extends Spell
{
	//Essa referência é guardada somente antes de desencantar, para evitar a referência circular.
	//O motivo dessa variável existir aqui é só pra auxiliar no cálculo da pontuação do desencantamento.
	private final Enchantment encantamento;

	protected Disenchantment(final Player jogador, final Date timestamp, final Enchantment encantamento)
	{
		// salva o estado do jogador na hora do desencantamento
		super(jogador.copy(), new Date(timestamp.getTime()));
		this.encantamento = encantamento.copy(); // salva o estado do encantamento ANTES de encantar
		encantamento.desencantar(this); // faz o efeito colateral pra salvar o estado
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

	public Enchantment getEncantamento()
	{
		return encantamento;
	}

	/**
	 * Retorna a pontuação conferida por esse desencantamento. <br/>
	 * No momento o valor retornado é constante, baseado no máximo que
	 * um encantamento pode conferir de ponto.
	 * @return Um inteiro não negativo com o valor da pontuação conferida pelo desencantamento
	 */
	public int getPontuacao()
	{
		return Enchantment.getMaxPontuacao() / 2;
	}
}

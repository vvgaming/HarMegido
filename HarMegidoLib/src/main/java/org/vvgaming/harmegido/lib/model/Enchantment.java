package org.vvgaming.harmegido.lib.model;

import java.util.Date;

import org.vvgaming.harmegido.lib.util.Copyable;

import com.github.detentor.codex.monads.Option;

/**
 * Essa classe representa o encantamento de um determinado objeto.
 */
public class Enchantment implements Copyable {
	private final Player jogador;
	private final Date timestamp;
	private Option<Disenchantment> desencantamento;
	private Object histogram; // Informação necessária para desencantar

	protected Enchantment(final Player enchanter, final Date enchantTime) {
		super();
		this.jogador = enchanter.copy();
		this.timestamp = enchantTime;
	}

	/**
	 * Cria um novo encantamento para o jogador e hora passados como parâmetro. <br/>
	 * Em particular, será salvo o estado do jogador quando ele fez o
	 * encantamento. <br>
	 * 
	 * @param jogador
	 *            O jogador que fez o encantamento
	 * @param timestamp
	 *            A data/hora que o encantamento foi feito
	 * @return O encantamento feito pelo jogador na data/hora passados como
	 *         parâmetro
	 */
	public static Enchantment from(Player jogador, Date timestamp) {
		return new Enchantment(jogador, timestamp);
	}

	/**
	 * Desencanta este encantamento. <br/>
	 * 
	 * @param oDesencantamento
	 *            O desencantamento vinculado a este encantamento
	 * @throws IllegalStateException
	 *             Se o método for chamado mais de uma vez
	 */
	public void desencantar(final Disenchantment oDesencantamento) {
		if (desencantamento.notEmpty()) {
			throw new IllegalStateException(
					"Este encantamento já foi desencantado");
		}

		this.desencantamento = Option.from(oDesencantamento);
	}

	public Player getJogador() {
		return jogador;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public int calcularPontuacao() {
		// TODO: efetivar o cálculo
		return 0;
	}

	@Override
	public Enchantment copy() {
		final Enchantment enchantment = new Enchantment(this.jogador,
				this.timestamp);
		enchantment.histogram = this.histogram;
		enchantment.desencantamento = this.desencantamento;
		return enchantment;
	}
}

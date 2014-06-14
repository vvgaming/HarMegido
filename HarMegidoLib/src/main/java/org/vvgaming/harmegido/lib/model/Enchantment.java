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
	private Object histogram; // Informa��o necess�ria para desencantar

	protected Enchantment(final Player enchanter, final Date enchantTime) {
		super();
		this.jogador = enchanter.copy();
		this.timestamp = enchantTime;
	}

	/**
	 * Cria um novo encantamento para o jogador e hora passados como par�metro. <br/>
	 * Em particular, ser� salvo o estado do jogador quando ele fez o
	 * encantamento. <br>
	 * 
	 * @param jogador
	 *            O jogador que fez o encantamento
	 * @param timestamp
	 *            A data/hora que o encantamento foi feito
	 * @return O encantamento feito pelo jogador na data/hora passados como
	 *         par�metro
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
	 *             Se o m�todo for chamado mais de uma vez
	 */
	public void desencantar(final Disenchantment oDesencantamento) {
		if (desencantamento.notEmpty()) {
			throw new IllegalStateException(
					"Este encantamento j� foi desencantado");
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
		// TODO: efetivar o c�lculo
		return 0;
	}

	public Enchantment copy() {
		final Enchantment enchantment = new Enchantment(this.jogador,
				this.timestamp);
		enchantment.histogram = this.histogram;
		enchantment.desencantamento = this.desencantamento;
		return enchantment;
	}
}

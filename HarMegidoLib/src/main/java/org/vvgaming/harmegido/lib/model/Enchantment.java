package org.vvgaming.harmegido.lib.model;

import java.util.Date;

import org.vvgaming.harmegido.lib.util.Copyable;

import com.github.detentor.codex.monads.Option;

/**
 * Essa classe representa o encantamento de um determinado objeto.
 */
public class Enchantment extends Spell implements Copyable
{
	private final byte[] histogram; // Informação necessária para desencantar
	private Option<Disenchantment> desencantamento = Option.empty();

	protected Enchantment(final Player enchanter, final Date enchantTime, final  byte[] histogram)
	{
		//copia para guardar o estado do jogador no momento do encantamento
		super(enchanter.copy(), new Date(enchantTime.getTime()));
		this.histogram = histogram;
	}

	/**
	 * Cria um novo encantamento para o jogador e hora passados como parâmetro. <br/>
	 * Em particular, será salvo o estado do jogador quando ele fez o encantamento. <br>
	 * 
	 * @param jogador O jogador que fez o encantamento
	 * @param timestamp A data/hora que o encantamento foi feito
	 * @param histogram O histograma que representa a imagem
	 * @return O encantamento feito pelo jogador na data/hora passados como parâmetro
	 */
	public static Enchantment from(final Player jogador, final Date timestamp, final  byte[] histogram)
	{
		return new Enchantment(jogador, timestamp, histogram);
	}

	/**
	 * Desencanta este encantamento. <br/>
	 * 
	 * @param oDesencantamento O desencantamento vinculado a este encantamento
	 * @throws IllegalStateException Se o método for chamado mais de uma vez
	 */
	public void desencantar(final Disenchantment oDesencantamento)
	{
		if (desencantamento.notEmpty())
		{
			throw new IllegalStateException("Este encantamento já foi desencantado");
		}
		this.desencantamento = Option.from(oDesencantamento);
	}

	public int calcularPontuacao()
	{
		// TODO: efetivar o c�lculo
		return 0;
	}
	
	public byte[] getHistogram()
	{
		return histogram;
	}

	public Enchantment copy()
	{
		final Enchantment enchantment = new Enchantment(getJogador(), getTimestamp(), this.histogram);
		enchantment.desencantamento = this.desencantamento; //seguro porque no desencantamento uma nova Option é criada
		return enchantment;
	}
}

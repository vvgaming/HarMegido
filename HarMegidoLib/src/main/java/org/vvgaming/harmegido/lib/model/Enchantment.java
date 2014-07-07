package org.vvgaming.harmegido.lib.model;

import java.util.Date;

import org.vvgaming.harmegido.lib.util.Copyable;

import com.github.detentor.codex.monads.Option;

/**
 * Essa classe representa o encantamento de um determinado objeto.
 */
public class Enchantment extends Spell implements Copyable
{
	private final byte[] imagem; // Informação necessária para desencantar
	private Option<Disenchantment> desencantamento = Option.empty();

	protected Enchantment(final Player enchanter, final Date enchantTime, final  byte[] imagem)
	{
		//copia para guardar o estado do jogador no momento do encantamento
		super(enchanter.copy(), new Date(enchantTime.getTime()));
		this.imagem = imagem;
	}

	/**
	 * Cria um novo encantamento para o jogador e hora passados como parâmetro. <br/>
	 * Em particular, será salvo o estado do jogador quando ele fez o encantamento. <br>
	 * 
	 * @param jogador O jogador que fez o encantamento
	 * @param timestamp A data/hora que o encantamento foi feito
	 * @param imagem O histograma que representa a imagem
	 * @return O encantamento feito pelo jogador na data/hora passados como parâmetro
	 */
	public static Enchantment from(final Player jogador, final Date timestamp, final  byte[] imagem)
	{
		return new Enchantment(jogador, timestamp, imagem);
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
	
	/**
	 * Retorna uma Option que conterá o desencantamento deste encantamento, se ele existir
	 * @return Uma Option que conterá o desencantamento, se ele existir
	 */
	public Option<Disenchantment> getDesencantamento()
	{
		return desencantamento;
	}

	public int calcularPontuacao()
	{
		// TODO: efetivar o c�lculo
		return 0;
	}
	
	public byte[] getImagem()
	{
		return imagem;
	}

	public Enchantment copy()
	{
		final Enchantment enchantment = new Enchantment(getJogador(), getTimestamp(), this.imagem);
		enchantment.desencantamento = this.desencantamento; //seguro porque no desencantamento uma nova Option é criada
		return enchantment;
	}
}

package org.vvgaming.harmegido.lib.model;

import java.util.Date;

import org.vvgaming.harmegido.lib.util.Copyable;
import org.vvgaming.harmegido.lib.util.TimeSync;

import com.github.detentor.codex.monads.Option;

/**
 * Essa classe representa o encantamento de um determinado objeto.
 */
public class Enchantment extends Spell implements Copyable
{
	private final EnchantmentImage imagem; // Informação necessária para desencantar
	private Option<Disenchantment> desencantamento = Option.empty();
	private transient TimeSync timeSync;

	protected Enchantment(final Player enchanter, final Date enchantTime, final EnchantmentImage imagem)
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
	 * @param imagem A imagem que representa o encantamento
	 * @return O encantamento feito pelo jogador na data/hora passados como parâmetro
	 */
	public static Enchantment from(final Player jogador, final Date timestamp, final EnchantmentImage imagem)
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

	/**
	 * Retorna a pontuação que esse encantamento vale. <br/>
	 * ATENÇÃO: Se não houver sido definida uma classe de sincronização de tempo 
	 * (com o método {@link #setTimeSync(TimeSync)}
	 * os resultados retornados estarão potencialmente incorretos.
	 * @return Um inteiro não negativo com a pontuação que este encantamento rendeu 
	 */
	public int getPontuacao()
	{
//		final long toSubtract = desencantamento.isEmpty() ? new Date().getTime() : desencantamento.get().getTimestamp().getTime();
//		int nSeconds =  (int) (toSubtract - getTimestamp().getTime()) / 1000; //transforma em segundos;
//		nSeconds =  
		//o máximo de segundos que conta pontos é 80
		return calcPontuacao(Math.min(80, getElapsedSeconds()));
	}
	
	private int getElapsedSeconds()
	{
		final Date localStartDate = getLocalTime(getTimestamp());
		Date endDate;
		
		if (desencantamento.isEmpty())
		{
			endDate = new Date();
		}
		else
		{
			endDate = getLocalTime(desencantamento.get().getTimestamp());
		}
		
		return (int) ((Math.abs(endDate.getTime() - localStartDate.getTime())) / 1000);
	}
	
	private Date getLocalTime(final Date forDate)
	{
		//alteração feita para verificar se o timesync está sendo usado
		System.out.println("TIMESYNC == null ? " + (timeSync == null));
		System.out.println("TIMESYNC SENDO CHAMADO P/ " + forDate);
		return timeSync.getLocalTime(forDate);
//		return timeSync == null ? forDate : timeSync.getLocalTime(forDate);
	}

	/**
	 * Define para esta partida a classe básica a ser utilizada na sincronização de tempo
	 * dela com o servidor.
	 * @param timeSync A classe a ser utilizada para prover a sincronização
	 */
	public void setTimeSync(final TimeSync timeSync)
	{
		this.timeSync = timeSync;
	}

	/**
	 * Retorna a pontuação de uma determinada quantidade de segundos
	 */
	private static int calcPontuacao(final int secs)
	{
		//(-x^2 +80x)/500 parábola  (rende +/- 170 pontos se ficar até o fim)
		//(40*x*x - x*x*x/3)/500
		return (secs*secs*(120 - secs))/1500;
	}
	
	/**
	 * Retorna a pontuação máxima que este encantamento pode conferir
	 * @return Um inteiro positivo com a pontuação máxima que este encantamento pode conferir
	 */
	public static int getMaxPontuacao()
	{
		//Está como número aqui porque a fórmula para calcular a pontuação tem o
		//sua raiz em 80. Como é mais performático deixar a função geral, não será
		//gereralizado o cálculo para qualquer valor.
		return calcPontuacao(80);
	}
	
	public EnchantmentImage getImagem()
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

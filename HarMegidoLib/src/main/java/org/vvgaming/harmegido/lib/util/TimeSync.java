package org.vvgaming.harmegido.lib.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Representa uma sincronia de tempo entre dois locais de tempo diferentes. <br>
 * Para isso, recebe-se duas datas ao mesmo tempo, de forma a sincronizá-las. <br/>
 * Posteriores chamadas retornam o tempo de uma delas com base na outra.
 * 
 */
public class TimeSync
{
	private final Date baseDate;
	private final Date toSync;
	
	private TimeSync(Date baseDate, Date toSync)
	{
		super();
		this.baseDate = baseDate;
		this.toSync = toSync;
	}
	
	/**
	 * Retorna uma instância de {@link TimeSync} que representa a sincronia de
	 * tempo entre duas datas potencialmente diferentes.
	 * @param baseDate A data que será usada como base para a sincronia
	 * @param toSync A data que deve ser sincronizada de acordo com a data base
	 * @return Uma instância de TimeSync que representa a sincronização entre as datas
	 */
	public static final TimeSync from(final Date baseDate, final Date toSync)
	{
		return new TimeSync(baseDate, toSync);
	}
	
	/**
	 * Retorna a representação da data local a partir da data passada como parâmetro, 
	 * usando como sincronia a data base.
	 * @param forTime A data (do mesmo tipo da data base), potencialmente de outro lugar,
	 * que se quer descobrir a representação local
	 * @return Um inteiro que representa o número de milisegundos passados até a data
	 * considerada.
	 */
	public Date getLocalTime(final Date forTime)
	{
		final Calendar cal = Calendar.getInstance();

		cal.setTime(toSync);
		cal.add(Calendar.MILLISECOND, (int) (forTime.getTime() - baseDate.getTime()));
		
		return cal.getTime();
	}
	
	@Override
	public String toString()
	{
		return "TimeSync [baseDate=" + baseDate + ", toSync=" + toSync + "]";
	}
}

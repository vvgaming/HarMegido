package org.vvgaming.harmegido.server;

import java.util.logging.Level;

import org.unbiquitous.network.http.connection.ServerMode;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.UOSLogging;

public final class UOSInstance
{
	private static UOS instance; 
	
	private UOSInstance()
	{
		//previne instanciação
	}
	
	/**
	 * Cria e inicializa uma instância do UOS. <br/>

	 * @return Uma instância do UOS
	 */
	private static UOS createUOS()
	{
		final UOS toReturn = new UOS();
		UOSLogging.setLevel(Level.ALL);
		ServerMode.Properties properties = new ServerMode.Properties();
		properties.put("ubiquitos.driver.deploylist", ServerDriver.class.getName());
		toReturn.start(properties);
		return toReturn;
	}
	
	/**
	 * Retorna uma instância do UOS devidamente configurada.
	 * ATENÇÃO: Como o UOS pode demorar para iniciar, a instância pode não
	 * estar totalmente inicializada.
	 * 
	 * @return A instância do UOS
	 */
	public static UOS getUOS()
	{
		if (instance == null)
		{
			instance = createUOS();
		}
		return instance;
	}
}

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
		// previne instanciação
	}

	/**
	 * Cria e inicializa uma instância do UOS. <br/>
	 * 
	 * @return Uma instância do UOS
	 */
	private static UOS createUOS()
	{
		final UOS uosInstance = new UOS();
		UOSLogging.setLevel(Level.ALL);
		ServerMode.Properties properties = new ServerMode.Properties();
		properties.put("ubiquitos.driver.deploylist", ServerDriver.class.getName());
		properties.put("ubiquitos.websocket.messageBufferSize", 2 * 1024 * 1024); // 2 mb pra garantir

		uosInstance.start(properties);
		
		return uosInstance;
	}

	/**
	 * Retorna uma instância do UOS devidamente configurada. ATENÇÃO: Como o UOS pode demorar para iniciar, a instância pode não estar
	 * totalmente inicializada.
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

package org.vvgaming.harmegido.server;

import java.util.logging.Level;

import org.unbiquitous.network.http.connection.ServerMode;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.UOSLogging;

public class Main {

	private static UOS uos;

	public static void main(String[] args) {
		uos = new UOS();

		UOSLogging.setLevel(Level.ALL);

		ServerMode.Properties properties = new ServerMode.Properties();
		properties.put("ubiquitos.driver.deploylist",
				ServerDriver.class.getName());

		uos.start(properties);

	}

	public static UOS getUos() {
		if (uos == null || uos.getGateway() == null) {
			throw new IllegalStateException("uos ou gateway nulos");
		}
		return uos;
	}

}

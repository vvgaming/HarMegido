package org.vvgaming.harmegido.server;

import java.util.logging.Level;

import org.unbiquitous.network.http.connection.ServerMode;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.UOSLogging;

public class Main {
	public static void main(String[] args) {
		UOS uos = new UOS();

		UOSLogging.setLevel(Level.ALL);

		// InitialProperties properties = new InitialProperties();
		 
		//
		// properties.put("ubiquitos.radar", MulticastRadar.class.getName());
		// properties.put("ubiquitos.connectionManager",
		// TCPConnectionManager.class.getName());
		// properties.put("ubiquitos.eth.tcp.port", "14984");
		// properties.put("ubiquitos.eth.tcp.passivePortRange", "14985-15000");
		// properties.put("ubiquitos.eth.udp.port", "15001");
		// properties.put("ubiquitos.eth.udp.passivePortRange", "15002-15017");
		//
		// uos.start(properties);

		ServerMode.Properties properties = new ServerMode.Properties();
		properties.put("ubiquitos.driver.deploylist",
				 ServerDriver.class.getName());
		
		uos.start(properties);

	}
}

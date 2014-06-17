package org.vvgaming.harmegido.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

public class ServerDriver implements UosDriver {

	private UpDriver definition;
	private String DRIVER_NAME = "uos.harmegido.server";

	public ServerDriver() {

		definition = new UpDriver(DRIVER_NAME);

		definition.addService("getClientCount");
		definition.addService("getHostName");
	}

	public void init(Gateway gateway, InitialProperties properties,
			String instanceId) {
	}

	public UpDriver getDriver() {
		return definition;
	}

	public List<UpDriver> getParent() {
		return null;
	}

	public void destroy() {
	}

	public void getClientCount(Call call, Response response,
			CallContext callContext) {
		List<UpDevice> listDevices = Main.getUos().getGateway().listDevices();
		int clients = listDevices == null ? 0 : listDevices.size();
		response.addParameter("clientCount", clients);
	}

	public void getHostName(Call call, Response response,
			CallContext callContext) {
		try {
			response.addParameter("hostName", InetAddress.getLocalHost()
					.getHostName());
		} catch (UnknownHostException e) {
			response.addParameter("hostName", "unknown");
		}
	}
}

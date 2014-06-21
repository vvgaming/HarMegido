package org.vvgaming.harmegido.util;

import org.unbiquitous.uos.core.driverManager.UosDriver;

public final class DeviceInfo
{
	private final String deviceId;
	
	private DeviceInfo(final UosDriver driver)
	{
		//Singleton
		this.deviceId = driver.getDriver().getName();
	}
	
	/**
	 * Retorna um identificador único associado a este dispositivo
	 * @return Um identificador único associado a este dispositivo
	 */
	public String getDeviceId()
	{
		return deviceId;
	}
}

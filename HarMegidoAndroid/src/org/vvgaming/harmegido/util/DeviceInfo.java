package org.vvgaming.harmegido.util;

import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;

/**
 * Fornece um id único para este dispositivo a partir do driver de UOS
 * utilizado. 
 */
public final class DeviceInfo
{
	private static String deviceId;
	
	private DeviceInfo()
	{
	}
	
	/**
	 * Define o driver que está sendo usado por este dispositivo
	 * @param device
	 */
	public static void setDriver(final UpDevice device)
	{
		deviceId = device.getName();
	}
	
	/**
	 * Retorna um identificador único associado a este dispositivo
	 * @return Um identificador único associado a este dispositivo
	 */
	public static String getDeviceId()
	{
		return deviceId;
	}
}

package org.vvgaming.harmegido.uos;

import java.util.logging.Level;

import org.unbiquitous.network.http.connection.ClientMode;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.UOSLogging;

import android.os.AsyncTask;

public class UOSFacade
{
	// private static final String SERVER_IP = "harmegido.servegame.com"; //
	// servidor
	// amazon
	private static final String SERVER_IP = "192.168.0.100";

	// //////////////////////////////// UOS //////////////////////////////////
	private static UOS uos;
	private static boolean uosStarted = false;

	public static UOS getUos()
	{
		if (uos == null)
		{
			startUos();
			while (!uosStarted)
			{

			}
		}
		return uos;
	}

	public static void startUos()
	{
		if (uos != null)
		{
			return;
		}
		UOSLogging.setLevel(Level.ALL);
		uos = new UOS();
		new AsyncTask<Void, Void, Void>()
		{
			protected Void doInBackground(Void... params)
			{

				ClientMode.Properties props = new ClientMode.Properties();

				props.setServer(SERVER_IP);
				uos.start(props);
				uosStarted = true;

				return null;
			}
		}.execute();

	}

	public static void stopUos()
	{
		if (uos == null)
			return;
		uos.stop();
		uos = null;
		driverFacade = null;
		uosStarted = false;
	}

	public static boolean isUosStarted()
	{
		return uosStarted;
	}

	// ///////////////////////////////////////////////////////////////////////

	// ////////////////////////////// DRIVER FACADE //////////////////////////////////
	private static ServerDriverFacade driverFacade;
	private static boolean trying = false;

	public static boolean isDriverFacadeCreated()
	{
		return driverFacade != null;
	}

	public static void createFacadeAsync()
	{
		if (driverFacade != null || trying)
		{
			return;
		}
		new AsyncTask<Void, Void, Void>()
		{
			protected Void doInBackground(Void... params)
			{

				try
				{
					trying = true;
					createFacade();
				}
				catch (IllegalStateException | IllegalArgumentException exc)
				{
					// simplesmente não cria
					driverFacade = null;
				}
				finally
				{
					trying = false;
				}
				return null;
			}
		}.execute();

	}

	private static void createFacade()
	{
		driverFacade = ServerDriverFacade.from(getUos());
	}

	public static ServerDriverFacade getDriverFacade()
	{
		if (driverFacade == null)
		{
			createFacadeAsync();
		}
		return driverFacade;
	}

}

package org.vvgaming.harmegido.theGame;

import java.util.logging.Level;

import org.unbiquitous.network.http.connection.ClientMode;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.UOSLogging;

import android.os.AsyncTask;

public class UOSFacade {
	private static final String SERVER_IP = "harmegido.servegame.com"; // servidor
																		// amazon
	private static UOS uos;
	private static boolean started = false;

	public static UOS getUos() {
		if (uos == null) {
			startUos();
		}
		return uos;
	}

	public static void startUos() {
		if (uos != null) {
			return;
		}
		UOSLogging.setLevel(Level.ALL);
		uos = new UOS();
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {

				ClientMode.Properties props = new ClientMode.Properties();

				props.setServer(SERVER_IP);
				uos.start(props);
				started = true;

				return null;
			}
		}.execute();
	}

	public static void stopUos() {
		if (uos == null)
			return;
		uos.stop();
		uos = null;
		started = false;
	}

	public static boolean isStarted() {
		return started;
	}

}

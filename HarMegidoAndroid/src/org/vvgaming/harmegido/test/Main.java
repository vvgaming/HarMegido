package org.vvgaming.harmegido.test;

import java.util.List;
import java.util.logging.Level;

import org.unbiquitous.network.http.connection.ClientMode;
import org.unbiquitous.uos.core.ClassLoaderUtils;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.UOSLogging;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.test.bubbleTest.BubbleTestActivity;
import org.vvgaming.harmegido.test.vision.ObjDetectTestActivity;
import org.vvgaming.harmegido.theGame.HarMegidoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Main extends Activity {

	private static final String SERVER_IP = "harmegido.servegame.com"; //servidor da amazon


	private UOS uos = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);

		ClassLoaderUtils.builder = new ClassLoaderUtils.DefaultClassLoaderBuilder() {
			public ClassLoader getParentClassLoader() {
				return getClassLoader();
			};
		};

		((Button) findViewById(R.id.btnHarMegido))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(),
								HarMegidoActivity.class);
						startActivity(i);
					}
				});

		((Button) findViewById(R.id.btnBubblesTest))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(),
								BubbleTestActivity.class);
						startActivity(i);
					}
				});

		((Button) findViewById(R.id.btnCameraTest))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(),
								ObjDetectTestActivity.class);
						startActivity(i);
					}
				});
		
		((Button) findViewById(R.id.btnMyTest))
		.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						MainGame.class);
				startActivity(i);
			}
		});

		((Button) findViewById(R.id.btnMessageTest))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						new AsyncTask<Void, Void, String>() {
							protected String doInBackground(Void... params) {

								final Gateway gateway = uos.getGateway();
								final List<DriverData> drivers = gateway
										.listDrivers("uos.harmegido.server");

								final UpDevice device = drivers.get(0)
										.getDevice();

								Call call = new Call("uos.harmegido.server",
										"sendMsg");
								call.addParameter("msg", "Mensagem Teste");

								Response response;
								String responseStr;
								try {
									response = gateway
											.callService(device, call);
									responseStr = response.toString();

								} catch (ServiceCallException e) {
									responseStr = e.getMessage();
								}

								System.out.println(responseStr);
								return responseStr;
							}
						}.execute();

					}
				});

		startUos();
	}

	private void startUos() {
		UOSLogging.setLevel(Level.ALL);
		if (uos != null)
			return;
		uos = new UOS();
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {

				// InitialProperties props = new InitialProperties();
				//
				// props.put("ubiquitos.radar", MulticastRadar.class.getName());
				// props.put("ubiquitos.connectionManager",
				// TCPConnectionManager.class.getName());
				// props.put("ubiquitos.eth.tcp.port", "14984");
				// props.put("ubiquitos.eth.tcp.passivePortRange",
				// "14985-15000");
				// props.put("ubiquitos.eth.udp.port", "15001");
				// props.put("ubiquitos.eth.udp.passivePortRange",
				// "15002-15017");

				ClientMode.Properties props = new ClientMode.Properties();

				props.setServer(SERVER_IP);
				uos.start(props);

				return null;
			}
		}.execute();
	}

	private void stopUos() {
		if (uos == null)
			return;
		uos.stop();
		uos = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopUos();
	}

}

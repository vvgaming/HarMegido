package org.vvgaming.harmegido.theGame.scenes;

import java.util.List;

import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.vvgaming.harmegido.gameEngine.AbstractGameScene;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.gos.TextGO;
import org.vvgaming.harmegido.gameEngine.gos.TimerGO;
import org.vvgaming.harmegido.theGame.HarMegidoActivity;
import org.vvgaming.harmegido.theGame.UOSFacade;

import android.app.Activity;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.github.detentor.codex.function.Function0;

public class MenuScene extends AbstractGameScene {

	private TextGO toqueNaTela;
	private TextGO serverName;
	private TextGO clientInfo;

	public MenuScene(Activity act) {
		super(act);
	}

	@Override
	public void init() {
		super.init();

		toqueNaTela = new TextGO(getWidth() / 2, getHeight() / 2,
				"Toque na tela para iniciar");
		toqueNaTela.face = Typeface.createFromAsset(getAssetManager()
				.getAndroidAssets(), "fonts/Radio Trust.ttf");
		toqueNaTela.paint.setTextAlign(Align.CENTER);
		toqueNaTela.size = 70;

		serverName = toqueNaTela.clone();
		serverName.pos = new Ponto(getWidth() / 2, 2 * getHeight() / 10);
		serverName.text = "";
		serverName.size = 40;

		clientInfo = serverName.clone();
		clientInfo.pos = clientInfo.pos.translate(0, 40);

		addObject(toqueNaTela);
		addObject(serverName);
		addObject(clientInfo);

		addObject(new TimerGO(500, new Function0<Void>() {
			@Override
			public Void apply() {
				toqueNaTela.setVisible(!toqueNaTela.isVisible());
				return null;
			}
		}));

		addObject(new TimerGO(5000, new Function0<Void>() {
			@Override
			public Void apply() {
				checkUOS();
				return null;
			}
		}).setCounting(10000));

	}

	@Override
	public void update(long delta) {
	}

	private void checkUOS() {
		UOS uos = UOSFacade.getUos();

		final Gateway gateway = uos.getGateway();
		final List<DriverData> drivers = gateway
				.listDrivers("uos.harmegido.server");

		if (!UOSFacade.isStarted()) {
			serverName.text = "Nenhum servidor encontrado";
		} else if (drivers == null || drivers.size() == 0) {
			// nenhum hm server encontrado
			serverName.text = "Nenhum servidor encontrado";
		} else if (drivers.size() > 1) {
			// mais de um hm server encontrado
			serverName.text = "Mais de um servidor encontrado";
		} else {
			final DriverData dev = drivers.get(0);

			final UpDevice device = dev.getDevice();

			Call callHostName = new Call("uos.harmegido.server", "getHostName");
			Call callClientCount = new Call("uos.harmegido.server",
					"getClientCount");

			try {
				Response callService = gateway
						.callService(device, callHostName);
				String hostName = callService.getResponseString("HostName");
				Integer clientCount = (Integer) gateway.callService(device,
						callClientCount).getResponseData("clientCount");

				serverName.text = hostName + "";
				clientInfo.text = "(clientes conectados: " + clientCount + ")";

			} catch (ServiceCallException e) {
				serverName.text = e.getMessage();
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			HarMegidoActivity.activity.trocarCena(new MainScene(
					getAssetManager().getActivity()));
		}
	}

}

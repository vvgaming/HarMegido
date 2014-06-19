package org.vvgaming.harmegido.theGame.mainNodes;

import java.util.List;

import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.NTextBlinking;
import org.vvgaming.harmegido.gameEngine.nodes.NTimer;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonImage;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NGroupToggleButton;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NToggleButton;
import org.vvgaming.harmegido.theGame.UOSFacade;

import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Option;

public class N2Menu extends GameNode {

	private NTextBlinking toqueNaTela;
	private NText serverName;
	private NText clientInfo;

	private NToggleButton anjos;
	private NToggleButton demonios;

	@Override
	public void init() {
		super.init();

		toqueNaTela = new NTextBlinking((int) getGameWidth(.5f),
				(int) getGameHeight(.5f), "Toque na tela para iniciar");
		toqueNaTela.face = Typeface.createFromAsset(getGameAssetManager()
				.getAndroidAssets(), "fonts/Radio Trust.ttf");
		toqueNaTela.paint.setTextAlign(Align.CENTER);
		toqueNaTela.size = 70;

		serverName = toqueNaTela.clone();
		serverName.pos = new Ponto(getGameWidth(.5f), getGameHeight(.2f));
		serverName.text = "";
		serverName.size = 40;

		clientInfo = serverName.clone();
		clientInfo.pos = clientInfo.pos.translate(0, 40);

		final NButtonImage anjosBtn = new NButtonImage(new NImage(new Ponto(
				getGameWidth(.25f), getGameHeight(.8f)), getGameAssetManager()
				.getBitmap(R.drawable.kayle)));
		anjosBtn.getImage().setWidthKeepingRatio(getGameWidth(.4f));
		anjos = new NToggleButton(anjosBtn);

		final NButtonImage demoniosBtn = new NButtonImage(new NImage(new Ponto(
				getGameWidth(.75f), getGameHeight(.8f)), getGameAssetManager()
				.getBitmap(R.drawable.morgana)));
		demoniosBtn.getImage().setWidthKeepingRatio(getGameWidth(.4f));
		demonios = new NToggleButton(demoniosBtn);

		NGroupToggleButton group = new NGroupToggleButton(anjos, demonios);
		group.setOnToggleChange(new Function1<Option<Integer>, Void>() {

			@Override
			public Void apply(Option<Integer> arg0) {
				if (arg0.notEmpty()) {
					System.out.println(arg0);
					if (arg0.get() == 0) {
						getGameAssetManager().playSound(R.raw.kayle);
					}
					if (arg0.get() == 1) {
						getGameAssetManager().playSound(R.raw.morgana);
					}
				}
				return null;
			}
		});

		addSubNode(group);

		addSubNode(toqueNaTela);
		addSubNode(serverName);
		addSubNode(clientInfo);

		addSubNode(new NTimer(5000, new Function0<Void>() {
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
	public boolean onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			RootNode.getInstance().changeMainNode(new N3Partida());
			return true;
		}
		return false;
	}

}

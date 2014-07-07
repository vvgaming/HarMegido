package org.vvgaming.harmegido.theGame.mainNodes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencv.core.Mat;
import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButton;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonImage;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NGroupToggleButton;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NToggleButton;
import org.vvgaming.harmegido.lib.model.Enchantment;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.theGame.objNodes.NHMBackgroundPartida;
import org.vvgaming.harmegido.theGame.objNodes.NHMEnchantingCam;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;
import org.vvgaming.harmegido.theGame.objNodes.NProgressBar;
import org.vvgaming.harmegido.theGame.objNodes.NSimpleBox;

import android.view.MotionEvent;

import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Option;

public class N5Partida extends NHMMainNode
{

	private final Player player;

	private NHMEnchantingCam cam;

	private NProgressBar progressBar;

	private boolean charging = false;
	private final float chargingDelay = NHMEnchantingCam.ENCANTAMENTO_CASTING_TIME;

	private List<Enchantment> encantamentos = new ArrayList<>();

	private Modo modo = Modo.ENCANTANDO;

	private NButton btnAvancar;
	private NButton btnVoltar;

	private enum Modo
	{
		ENCANTANDO, DESENCANTANDO;
	}

	public N5Partida(final Player player)
	{
		super();
		this.player = player;
	}

	@Override
	public void init()
	{

		super.init();

		final boolean isAngels;
		switch (player.getTime())
		{
		case DARK:
			isAngels = false;
			break;
		case LIGHT:
			isAngels = true;
			break;
		default:
			throw new IllegalArgumentException("Time '" + player.getTime() + "' desconhecido");
		}

		cam = new NHMEnchantingCam(new Ponto(getGameWidth(.5f), getGameHeight(.35f)), getGameHeight(.5f));

		final NSimpleBox bg = new NSimpleBox(0, 0, getGameWidth(), getGameHeight(), 0, 0, 0);

		if (isAngels)
		{
			bg.setColor(43, 167, 203);
		}
		else
		{
			bg.setColor(255, 0, 0);
		}

		final NImage imgEncantar = new NImage(new Ponto(getGameWidth(.3f), getGameHeight(.8f)), getGameAssetManager().getBitmap(
				R.drawable.encantar));
		imgEncantar.setWidth(getGameWidth(.2f), true);
		final NButtonImage btnEncantar = new NButtonImage(imgEncantar);

		final NImage imgDesencantar = new NImage(new Ponto(getGameWidth(.7f), getGameHeight(.8f)), getGameAssetManager().getBitmap(
				R.drawable.desencantar));
		imgDesencantar.setWidth(getGameWidth(.2f), true);
		final NButtonImage btnDesencantar = new NButtonImage(imgDesencantar);

		final NToggleButton tglEncantar = new NToggleButton(btnEncantar);
		tglEncantar.getBoundingRectPaint().setARGB(80, 200, 200, 200);
		tglEncantar.getBoundingRectPaint().setStrokeWidth(10);
		final NToggleButton tglDesencantar = new NToggleButton(btnDesencantar);
		tglDesencantar.getBoundingRectPaint().setARGB(80, 200, 200, 200);
		tglDesencantar.getBoundingRectPaint().setStrokeWidth(10);
		final NGroupToggleButton tglGroup = new NGroupToggleButton(tglEncantar, tglDesencantar);
		tglGroup.toggle(0);

		tglGroup.setOnToggleChange(new Function1<Option<Integer>, Void>()
		{
			@Override
			public Void apply(Option<Integer> arg0)
			{
				if (arg0.notEmpty())
				{
					switch (arg0.get())
					{
					case 0:
						sendConsoleMsg("Toque na tela para encantar");
						modo = Modo.ENCANTANDO;
						break;
					case 1:
						if (encantamentos.isEmpty())
						{
							tglGroup.toggle(0);
							sendConsoleMsg("Não há o que desencantar...");
							getGameAssetManager().playSound(R.raw.error);
						}
						else
						{
							sendConsoleMsg("Procure o objeto a desencantar");
							modo = Modo.DESENCANTANDO;
						}
						break;
					default:
						throw new IllegalArgumentException("Modo desconhecido: " + arg0.get());
					}
				}
				return null;
			}
		});

		final NImage imgAvancar = new NImage(new Ponto(getGameWidth(.9f), getGameHeight(.35f)), getGameAssetManager().getBitmap(
				R.drawable.avancar));
		imgAvancar.setWidth(getGameWidth(.15f), true);
		btnAvancar = new NButtonImage(imgAvancar);
		btnAvancar.setVisible(false);

		final NImage imgVoltar = new NImage(new Ponto(getGameWidth(.1f), getGameHeight(.35f)), getGameAssetManager().getBitmap(
				R.drawable.voltar));
		imgVoltar.setWidth(getGameWidth(.15f), true);
		btnVoltar = new NButtonImage(imgVoltar);
		btnVoltar.setVisible(false);

		addSubNode(bg, 0);
		addSubNode(cam, 1);

		addSubNode(progressBar = new NProgressBar(0, getGameHeight(.58f), getGameWidth(), getGameHeight(.08f)), 2);

		addSubNode(new NHMBackgroundPartida(isAngels), 3);

		addSubNode(tglGroup, 4);
		addSubNode(btnAvancar, 4);
		addSubNode(btnVoltar, 4);

	}

	@Override
	public void update(final long delta)
	{

		if (charging)
		{
			progressBar.setProgress(progressBar.getProgress() + 1 / chargingDelay * delta);
		}

		System.out.println();

		if (modo.equals(Modo.DESENCANTANDO))
		{
			btnAvancar.setVisible(true);
			btnVoltar.setVisible(true);
		}
		else
		{
			btnAvancar.setVisible(false);
			btnVoltar.setVisible(false);
		}

	}

	@Override
	public boolean onTouch(final MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			charging = true;
			sendConsoleMsg("Encantamento sendo preparado...");
			cam.iniciaEncantamento(new Function1<Option<Mat>, Void>()
			{
				@Override
				public Void apply(final Option<Mat> arg0)
				{
					if (arg0.notEmpty())
					{
						sendConsoleMsg("Encantamento finalizado com sucesso");
						getGameAssetManager().playSound(R.raw.encantament_sucesso);
						charging = false;
						progressBar.reset();

						final Mat mat = arg0.get();
						byte[] array = new byte[(int) (mat.total() * mat.channels())];
						mat.get(0, 0, array);

						encantamentos.add(Enchantment.from(player, new Date(), array));

					}
					else
					{
						sendConsoleMsg("Falhou");
						getGameAssetManager().playSound(R.raw.encantament_falha);
						charging = false;
						progressBar.reset();
					}

					return null;
				}
			});
			return true;
		case MotionEvent.ACTION_UP:
			charging = false;
			if (cam.pararEncantamento())
			{
				getGameAssetManager().playSound(R.raw.encantament_falha);
				sendConsoleMsg("Cancelado");
			}
			progressBar.reset();
			return true;

		default:
			return false;
		}
	}

}

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
import org.vvgaming.harmegido.lib.model.EnchantmentImage;
import org.vvgaming.harmegido.lib.model.OpenCVMatWrapper;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.theGame.objNodes.NHMBackgroundPartida;
import org.vvgaming.harmegido.theGame.objNodes.NHMEnchantingCam;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;
import org.vvgaming.harmegido.theGame.objNodes.NProgressBar;
import org.vvgaming.harmegido.theGame.objNodes.NSimpleBox;
import org.vvgaming.harmegido.util.Constantes;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.view.MotionEvent;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.codex.product.Tuple3;

public class N5Partida extends NHMMainNode
{

	private final Player player;

	private NHMEnchantingCam cam;
	private NGroupToggleButton tglGroupModo;
	private NProgressBar progressBar;
	private NButton btnAvancar;
	private NButton btnVoltar;

	private boolean charging = false;
	private float chargingDelay = NHMEnchantingCam.ENCANTAMENTO_CASTING_TIME;
	private Modo modo = Modo.ENCANTANDO;

	//
	private final List<Enchantment> encantamentos = new ArrayList<>();

	public N5Partida(final Player player)
	{
		super();
		this.player = player;
	}

	@Override
	public void init()
	{

		super.init();
		final Tuple3<Integer, Integer, Integer> color;
		final boolean angelTeam;
		switch (player.getTime())
		{
		case DARK:
			angelTeam = false;
			color = Constantes.DEMON_COLOR;
			break;
		case LIGHT:
			angelTeam = true;
			color = Constantes.ANGEL_COLOR;
			break;
		default:
			throw new IllegalArgumentException("Time '" + player.getTime() + "' desconhecido");
		}

		//
		cam = new NHMEnchantingCam(new Ponto(getGameWidth(.5f), getGameHeight(.35f)), getGameHeight(.5f));

		// fundo
		final NSimpleBox bg = new NSimpleBox(0, 0, getGameWidth(), getGameHeight(), 0, 0, 0);
		// cor de acordo com o time
		bg.setColor(color.getVal1(), color.getVal2(), color.getVal3());

		// toggles de encantar / desencantar
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
		tglGroupModo = new NGroupToggleButton(tglEncantar, tglDesencantar);
		tglGroupModo.setOnToggleChange(tglGroupModoFunction);

		// botões de avançar e voltar os encantamentos para desencantar
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

		// modo padrão
		tglGroupModo.toggle(0);
		setModoEncantar();

		// adicionando nas camadas
		addSubNode(bg, 0);
		addSubNode(cam, 1);
		addSubNode(progressBar = new NProgressBar(getGameWidth(.1f), getGameHeight(.58f), getGameWidth(.8f), getGameHeight(.08f)), 2);

		addSubNode(new NHMBackgroundPartida(angelTeam), 3);

		addSubNode(tglGroupModo, 4);
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

	}

	private void setModoEncantar()
	{
		sendConsoleMsg("Toque na tela para encantar");
		modo = Modo.ENCANTANDO;
		chargingDelay = NHMEnchantingCam.ENCANTAMENTO_CASTING_TIME;
		btnAvancar.setVisible(false);
		btnVoltar.setVisible(false);
		cam.parar();
	}

	private void setModoDesencantar()
	{
		if (encantamentos.isEmpty())
		{
			tglGroupModo.toggle(0);
			sendConsoleMsg("Não há o que desencantar...");
			getGameAssetManager().playSound(R.raw.error);
			getGameAssetManager().vibrate(50);
		}
		else
		{
			cam.parar();
			sendConsoleMsg("Procure o objeto a desencantar");
			modo = Modo.DESENCANTANDO;
			chargingDelay = NHMEnchantingCam.DESENCANTAMENTO_CASTING_TIME;
			btnAvancar.setVisible(true);
			btnVoltar.setVisible(true);

			final Enchantment primeiro = encantamentos.get(0);

			final OCVUtil ocvUtil = OCVUtil.getInstance();
			final Mat imagem = ocvUtil.toMat(primeiro.getImagem().getImagem());
			final Mat features = ocvUtil.toMat(primeiro.getImagem().getFeatures());

			cam.iniciaDesencantamento(Tuple2.from(imagem, features), new Function0<Void>()
			{
				@Override
				public Void apply()
				{
					sendConsoleMsg("Desencantando...");
					getGameAssetManager().vibrate(50);
					return null;
				}
			}, new Function1<Boolean, Void>()
			{

				@Override
				public Void apply(final Boolean arg0)
				{

					if (arg0)
					{
						sendConsoleMsg("Desencantado com sucesso");
						getGameAssetManager().playSound(R.raw.encantament_sucesso);
						getGameAssetManager().vibrate(500);
					}
					else
					{
						sendConsoleMsg("Falhou");
						getGameAssetManager().playSound(R.raw.encantament_falha);
						getGameAssetManager().vibrate(100);
					}
					return null;
				}
			});

		}
	}

	@Override
	public boolean onTouch(final MotionEvent event)
	{
		if (modo.equals(Modo.ENCANTANDO))
		{
			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				charging = true;
				sendConsoleMsg("Encantamento sendo preparado...");
				getGameAssetManager().vibrate(50);
				cam.iniciaEncantamento(new Function1<Option<Tuple2<Mat, Mat>>, Void>()
				{
					@Override
					public Void apply(final Option<Tuple2<Mat, Mat>> encantado)
					{
						if (encantado.notEmpty())
						{
							sendConsoleMsg("Encantamento finalizado com sucesso");
							getGameAssetManager().playSound(R.raw.encantament_sucesso);
							getGameAssetManager().vibrate(500);
							charging = false;
							progressBar.reset();

							final Tuple2<Mat, Mat> encantTuple = encantado.get();

							final OCVUtil ocvUtil = OCVUtil.getInstance();

							final OpenCVMatWrapper preview = ocvUtil.toOpenCVMatWrapper(encantTuple.getVal1());
							final OpenCVMatWrapper features = ocvUtil.toOpenCVMatWrapper(encantTuple.getVal2());
							final EnchantmentImage ei = EnchantmentImage.from(preview, features);

							encantamentos.add(Enchantment.from(player, new Date(), ei));
						}
						else
						{
							sendConsoleMsg("Falhou");
							getGameAssetManager().playSound(R.raw.encantament_falha);
							getGameAssetManager().vibrate(100);
							charging = false;
							progressBar.reset();
						}

						return null;
					}
				});
				return true;
			case MotionEvent.ACTION_UP:
				charging = false;
				if (cam.parar())
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

		return false;

	}

	private final Function1<Option<Integer>, Void> tglGroupModoFunction = new Function1<Option<Integer>, Void>()
	{
		@Override
		public Void apply(final Option<Integer> arg0)
		{
			if (arg0.notEmpty())
			{
				switch (arg0.get())
				{
				case 0:
					setModoEncantar();
					break;
				case 1:
					setModoDesencantar();
					break;
				default:
					throw new IllegalArgumentException("Modo desconhecido: " + arg0.get());
				}
			}
			return null;
		}

	};

	private enum Modo
	{
		ENCANTANDO, DESENCANTANDO;
	}

}

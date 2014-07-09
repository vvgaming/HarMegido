package org.vvgaming.harmegido.theGame.mainNodes;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.opencv.core.Mat;
import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.NText.VerticalAlign;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButton;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonImage;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NGroupToggleButton;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NToggleButton;
import org.vvgaming.harmegido.gameEngine.nodes.util.NParallelWorker;
import org.vvgaming.harmegido.lib.model.Enchantment;
import org.vvgaming.harmegido.lib.model.EnchantmentImage;
import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.OpenCVMatWrapper;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.lib.model.TeamType;
import org.vvgaming.harmegido.theGame.objNodes.NHMBackgroundPartida;
import org.vvgaming.harmegido.theGame.objNodes.NHMEnchantingCam;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;
import org.vvgaming.harmegido.theGame.objNodes.NProgressBar;
import org.vvgaming.harmegido.theGame.objNodes.NSimpleBox;
import org.vvgaming.harmegido.uos.UOSFacade;
import org.vvgaming.harmegido.util.Constantes;
import org.vvgaming.harmegido.util.MatchManager;
import org.vvgaming.harmegido.vision.OCVUtil;

import android.graphics.Paint.Align;
import android.view.MotionEvent;

import com.github.detentor.codex.collections.immutable.ListSharp;
import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.codex.product.Tuple3;

public class N5Partida extends NHMMainNode
{

	private final Player player;

	private NHMEnchantingCam cam;
	private NSimpleBox greenCam;
	private NGroupToggleButton tglGroupModo;
	private NProgressBar progressBar;
	private NButton btnAvancar;
	private NText stats;
	private NText qtdEnchants;

	private boolean charging = false;
	private float chargingDelay = NHMEnchantingCam.ENCANTAMENTO_CASTING_TIME;
	private Modo modo = Modo.ENCANTANDO;

	private NParallelWorker worker;

	private final Queue<Enchantment> encantamentos = new LinkedList<>();

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
		final Ponto camPoint = new Ponto(getGameWidth(.5f), getGameHeight(.35f));
		final float camHeight = getGameHeight(.5f);
		cam = new NHMEnchantingCam(camPoint, camHeight);

		greenCam = new NSimpleBox(0, (int) (camPoint.y - camHeight / 2), getGameWidth(), (int) camHeight, 0, 255, 0);
		greenCam.setColor(80, 0, 255, 0);
		greenCam.setVisible(false);

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
		btnAvancar.setOnClickFunction(new Function0<Void>()
		{
			@Override
			public Void apply()
			{
				if (encantamentos.size() > 1)
				{
					getGameAssetManager().vibrate(100);
					encantamentos.add(encantamentos.poll());
					setModoDesencantar();
				}
				return null;
			}
		});

		stats = new NText(getGameWidth(.5f), getGameHeight(.065f), "");
		stats.paint.setTextAlign(Align.CENTER);
		stats.face = getDefaultFace();

		qtdEnchants = new NText(btnDesencantar.getBoundingRect().right, btnDesencantar.getBoundingRect().bottom, "");
		qtdEnchants.paint.setTextAlign(Align.RIGHT);
		qtdEnchants.paint.setARGB(200, 255, 255, 255);
		qtdEnchants.vAlign = VerticalAlign.BOTTOM;

		// modo padrão
		tglGroupModo.toggle(0);
		setModoEncantar();

		// coloca um trabalhador paralelo para ficar fazendo acesso a rede e não travar a thread principal
		addSubNode(worker = new NParallelWorker());

		// adicionando nas camadas
		addSubNode(bg, 0);
		addSubNode(cam, 1);
		addSubNode(greenCam, 2);
		addSubNode(progressBar = new NProgressBar(getGameWidth(.1f), getGameHeight(.58f), getGameWidth(.8f), getGameHeight(.08f)), 3);

		addSubNode(new NHMBackgroundPartida(angelTeam), 4);

		addSubNode(tglGroupModo, 5);
		addSubNode(btnAvancar, 5);
		addSubNode(stats, 5);

		addSubNode(qtdEnchants, 6);

	}

	@Override
	public void update(final long delta)
	{
		encantamentos.clear();
		final Option<Match> opPartida = getPartidaEmAndamento();
		if (opPartida.notEmpty())
		{
			final Match partida = opPartida.get();

			// filtra os encantamentos do(s) outro(s) time(s) que precisam ser desencantados
			final List<Enchantment> paraDesencantar = ListSharp.from(partida.getEncantamentosNot(player.getTime()))
					.filter(new Function1<Enchantment, Boolean>()
					{
						@Override
						public Boolean apply(final Enchantment arg0)
						{
							return arg0.getDesencantamento().isEmpty();
						}
					}).toList();
			encantamentos.addAll(paraDesencantar);

			qtdEnchants.text = encantamentos.size() + "";

			String stats = "| ";
			for (final TeamType t : TeamType.values())
			{
				stats += t.toString() + ": " + partida.getPontuacao(t) + " |";
			}
			stats += "  " + partida.getTimeRemaining() / 1000;
			this.stats.text = stats;

		}

		if (charging)
		{
			progressBar.setProgress(progressBar.getProgress() + 1 / chargingDelay * delta);
		}

	}

	private void setModoEncantar()
	{
		sendConsoleMsg("Toque na tela para encantar");
		greenCam.setVisible(false);
		modo = Modo.ENCANTANDO;
		chargingDelay = NHMEnchantingCam.ENCANTAMENTO_CASTING_TIME;
		btnAvancar.setVisible(false);
		cam.parar();
	}

	private void setModoDesencantar()
	{
		greenCam.setVisible(false);
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

			final Enchantment primeiro = encantamentos.peek();

			final OCVUtil ocvUtil = OCVUtil.getInstance();
			final Mat imagem = ocvUtil.toMat(primeiro.getImagem().getImagem());
			final Mat features = ocvUtil.toMat(primeiro.getImagem().getFeatures());

			cam.iniciaDesencantamento(Tuple2.from(imagem, features), new Function0<Void>()
			{
				@Override
				public Void apply()
				{
					charging = true;
					sendConsoleMsg("Desencantando...");
					getGameAssetManager().vibrate(50);
					greenCam.setVisible(true);
					return null;
				}
			}, new Function1<Boolean, Void>()
			{

				@Override
				public Void apply(final Boolean arg0)
				{

					final Option<Match> opPart = getPartidaEmAndamento();
					if (arg0 && opPart.notEmpty())
					{
						sendConsoleMsg("Desencantado com sucesso");
						getGameAssetManager().playSound(R.raw.encantament_sucesso);
						getGameAssetManager().vibrate(500);
						charging = false;
						progressBar.reset();
						setModoDesencantar();

						worker.putTask(new Function0<Void>()
						{
							@Override
							public Void apply()
							{
								UOSFacade.getDriverFacade().desencantarObjeto(opPart.get().getNomePartida(), player, encantamentos.poll());
								return null;
							}
						});

					}
					else
					{
						sendConsoleMsg("Falhou");
						getGameAssetManager().playSound(R.raw.encantament_falha);
						getGameAssetManager().vibrate(100);
						charging = false;
						progressBar.reset();
						greenCam.setVisible(false);
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
						final Option<Match> part = getPartidaEmAndamento();
						if (encantado.notEmpty() && part.notEmpty())
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

							worker.putTask(new Function0<Void>()
							{
								@Override
								public Void apply()
								{
									UOSFacade.getDriverFacade().encantarObjeto(part.get().getNomePartida(), player, ei);
									return null;
								}
							});

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

	private Option<Match> getPartidaEmAndamento()
	{
		final Option<Match> part = MatchManager.getPartida();
		if (part.isEmpty() || !part.get().contemJogador(player.getIdJogador()) || !part.get().isAtiva())
		{
			sendConsoleMsg("Saindo da partida...");
			RootNode.getInstance().changeMainNode(new N3SelecaoPartida());
			MatchManager.limparPartida();
			return Option.empty();
		}
		return part;
	}

}

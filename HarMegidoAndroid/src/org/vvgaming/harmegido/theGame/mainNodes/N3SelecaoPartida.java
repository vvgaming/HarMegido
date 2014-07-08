package org.vvgaming.harmegido.theGame.mainNodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonText;
import org.vvgaming.harmegido.gameEngine.nodes.util.NTimer;
import org.vvgaming.harmegido.lib.model.Match.MatchDuration;
import org.vvgaming.harmegido.theGame.objNodes.NHMBackground;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;
import org.vvgaming.harmegido.theGame.objNodes.NSimpleBox;
import org.vvgaming.harmegido.uos.ServerDriverFacade;
import org.vvgaming.harmegido.uos.UOSFacade;
import org.vvgaming.harmegido.util.RandomNames;

import android.graphics.Paint.Align;
import android.graphics.Typeface;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.monads.Either;

/**
 * Menu de opções para entrar em uma partida. <br/>
 * - Mostra partidas em andamento <br/>
 * - Escolhe partida para entrar <br/>
 * - Solicita criação de nova partida <br/>
 * 
 * @author Vinicius Nogueira
 */
public class N3SelecaoPartida extends NHMMainNode
{

	private NText orientacao;
	private NText nenhumaPartida;
	private RandomNames rnd;

	private List<NButtonText> btnsPartidas = new ArrayList<>();

	@Override
	public void init()
	{
		super.init();

		rnd = new RandomNames(getGameAssetManager());

		final Typeface font = Typeface.createFromAsset(getGameAssetManager().getAndroidAssets(), "fonts/Radio Trust.ttf");

		orientacao = new NText(getGameWidth(.5f), getGameHeight(.25f), "Selecione a partida desejada:");
		orientacao.face = font;
		orientacao.paint.setTextAlign(Align.CENTER);

		NSimpleBox hr = new NSimpleBox(0, (int) (getGameHeight(.25f)), getGameWidth(), 5, 255, 255, 255);

		nenhumaPartida = orientacao.clone();
		nenhumaPartida.pos = nenhumaPartida.pos.translate(0, getGameHeight(.2f));
		nenhumaPartida.text = "procurando partidas...";

		final NText criarPartidaTexto = nenhumaPartida.clone();
		criarPartidaTexto.text = "Criar nova partida";
		criarPartidaTexto.pos = new Ponto(getGameWidth(.5f), getGameHeight(.8f));
		NButtonText criarPartidaBtn = new NButtonText(criarPartidaTexto);
		criarPartidaBtn.setOnClickFunction(new Function0<Void>()
		{
			@Override
			public Void apply()
			{
				UOSFacade.getDriverFacade().criarPartida(rnd.getRandomMatchName(), MatchDuration.FIVE_MINUTES);
				sendConsoleMsg("A partida será criada em breve, aguarde...");
				return null;
			}
		});

		addSubNode(new NHMBackground(), 0);
		addSubNode(orientacao, 1);
		addSubNode(hr, 1);
		addSubNode(nenhumaPartida, 1);
		addSubNode(criarPartidaBtn, 1);

		addSubNode(new NTimer(2500, new Function0<Void>()
		{
			@Override
			public Void apply()
			{
				for (NButtonText btn : btnsPartidas)
				{
					btn.kill();
				}
				btnsPartidas.clear();

				List<String> partidas = getPartidas();
				nenhumaPartida.setVisible(partidas.isEmpty());

				if (!partidas.isEmpty())
				{
					float height = getGameHeight(.35f) - getSmallFontSize();
					for (final String partida : partidas)
					{
						final NText texto = new NText(getGameWidth(.5f), height = height + (getSmallFontSize() * 1.3f), partida);
						texto.face = font;
						texto.paint.setTextAlign(Align.CENTER);
						final NButtonText btnPartida = new NButtonText(texto);
						btnPartida.setOnClickFunction(new Function0<Void>()
						{
							@Override
							public Void apply()
							{
								addSubNode(new NTimer(1000, new Function0<Void>()
								{
									@Override
									public Void apply()
									{
										RootNode.getInstance().changeMainNode(new N4SelecaoChar(partida));
										return null;
									}
								}, true));

								sendConsoleMsg("Entrando na partida...");

								return null;
							}

						});
						btnsPartidas.add(btnPartida);
						addSubNode(btnPartida, 1);
					}
				}

				return null;
			}
		}));

	}

	@Override
	public void update(final long delta)
	{

	}

	private List<String> getPartidas()
	{
		final ServerDriverFacade sdf = UOSFacade.getDriverFacade();
		final Either<Exception, List<String>> partidas = sdf.listarPartidas();
		if (partidas.isRight())
		{
			return partidas.getRight();
		}
		else
		{
			return Arrays.asList();
		}
	}

}

package org.vvgaming.harmegido.theGame.mainNodes;

import java.util.ArrayList;
import java.util.List;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.NText.VerticalAlign;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonImage;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonText;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NGroupToggleButton;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NToggleButton;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;
import org.vvgaming.harmegido.theGame.util.RandomNames;
import org.vvgaming.harmegido.uos.UOSFacade;

import android.graphics.Paint.Align;
import android.graphics.Typeface;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Either;
import com.github.detentor.codex.monads.Option;

/**
 * Menu de opções para criar seu char antes de entrar na partida. <br/>
 * - Escolhe o time <br/>
 * - Escolhe o nome do jogador (dentre 3 possíveis aleatórios) <br/>
 * 
 * @author Vinicius Nogueira
 */
public class N4SelecaoChar extends NHMMainNode
{

	protected static final int QTD_NOMES = 3;

	private final String NOME_PARTIDA;

	private NToggleButton anjosTglBtn;
	private NToggleButton demoniosTglBtn;
	private NGroupToggleButton timeTglGroup;

	private List<NButtonText> anjosNomesBtns = new ArrayList<>();
	private List<NButtonText> demoniosNomesBtns = new ArrayList<>();

	private NText orientacao;

	private RandomNames rnd;

	public N4SelecaoChar(final String nomePartida)
	{
		this.NOME_PARTIDA = nomePartida;
	}

	@Override
	public void init()
	{
		super.init();

		// gerador de nomes aleatorios
		rnd = new RandomNames(getGameAssetManager());

		// imagem de fundo
		NImage bgImg = new NImage(new Ponto(getGameWidth(.5f), getGameHeight(.5f)), getGameAssetManager().getBitmap(R.drawable.bg_intro));
		bgImg.setHeightKeepingRatio(getGameHeight());

		// título com o nome da partida
		NText title = new NText((int) getGameWidth(.5f), 0, NOME_PARTIDA);
		title.face = Typeface.createFromAsset(getGameAssetManager().getAndroidAssets(), "fonts/Radio Trust.ttf");
		title.paint.setTextAlign(Align.CENTER);
		title.vAlign = NText.VerticalAlign.TOP;
		title.size = getBigFontSize();

		// botão de seleção dos demonios
		final NButtonImage demoniosBtn = new NButtonImage(new NImage(new Ponto(getGameWidth(.25f), getGameHeight(.35f)),
				getGameAssetManager().getBitmap(R.drawable.morgana)));
		demoniosBtn.getImage().setWidthKeepingRatio(getGameWidth(.4f));
		demoniosTglBtn = new NToggleButton(demoniosBtn);

		// botão de seleção dos anjos
		final NButtonImage anjosBtn = new NButtonImage(new NImage(new Ponto(getGameWidth(.75f), getGameHeight(.35f)), getGameAssetManager()
				.getBitmap(R.drawable.kayle)));
		anjosBtn.getImage().setWidthKeepingRatio(getGameWidth(.4f));
		anjosTglBtn = new NToggleButton(anjosBtn);

		// agrupando os toogles de demonios e anjos
		timeTglGroup = new NGroupToggleButton(demoniosTglBtn, anjosTglBtn);
		timeTglGroup.setOnToggleChange(getTeamSelectionFunction());

		orientacao = new NText(getGameWidth(.5f), getGameHeight(.90f), "");
		orientacao.face = getDefaultFace();
		orientacao.paint.setTextAlign(Align.CENTER);
		orientacao.vAlign = VerticalAlign.MIDDLE;

		addSubNode(bgImg, 0);
		addSubNode(title, 1);
		addSubNode(timeTglGroup, 1);
		addSubNode(orientacao, 1);
	}

	@Override
	public void update(final long delta)
	{
		if (timeTglGroup.getToggledIndex().isEmpty())
		{
			orientacao.text = "Selecione seu time...";
		}
		else
		{
			orientacao.text = "Escolha seu nome\n e entrará na partida...";
		}
	}

	private Function1<Option<Integer>, Void> getTeamSelectionFunction()
	{
		return new Function1<Option<Integer>, Void>()
		{

			@Override
			public Void apply(final Option<Integer> arg0)
			{
				if (arg0.notEmpty())
				{
					final List<NButtonText> opposite;
					final List<NButtonText> mine;
					final float width;
					switch (arg0.get())
					{
					case 0:
						// demonios
						getGameAssetManager().playSound(R.raw.morgana);
						mine = demoniosNomesBtns;
						opposite = anjosNomesBtns;
						// centralizado no botao dos demonios
						width = demoniosTglBtn.getBoundingRect().centerX();
						break;
					case 1:
						// anjos
						getGameAssetManager().playSound(R.raw.kayle);
						mine = anjosNomesBtns;
						opposite = demoniosNomesBtns;
						// centralizado no botao dos demonios
						width = anjosTglBtn.getBoundingRect().centerX();
						break;

					default:
						throw new IllegalArgumentException("time desconhecido: " + arg0);
					}

					// limpa os do time oposto
					killsNodes(opposite.toArray(new GameNode[0]));
					opposite.clear();

					// abaixo dos toggle
					float height = anjosTglBtn.getBoundingRect().bottom + getSmallFontSize();

					// cria os do time a favor
					for (int i = 0; i < QTD_NOMES; i++)
					{
						final String nome;
						switch (arg0.get())
						{
						case 0:
							// demonios
							nome = rnd.getRandomDemonName();
							break;
						case 1:
							// anjos
							nome = rnd.getRandomAngelName();
							break;

						default:
							throw new IllegalArgumentException("time desconhecido: " + arg0);
						}

						final NText texto = new NText(width, height = height + (getSmallFontSize() * 1.3f), nome);
						texto.face = getDefaultFace();
						texto.paint.setTextAlign(Align.CENTER);
						final NButtonText btn = new NButtonText(texto);
						addSubNode(btn);
						mine.add(btn);

						btn.setOnClickFunction(new Function0<Void>()
						{

							@Override
							public Void apply()
							{
								sendConsoleMsg("Entrando na partida...");

								Player p = Player.from(nome, "qualquer um " + Math.random());
								Either<Exception, Boolean> resposta = UOSFacade.getDriverFacade().adicionarJogador(NOME_PARTIDA, p);
								if (resposta.isRight() && resposta.getRight())
								{
									RootNode.getInstance().changeMainNode(new N5Partida());
								}
								else
								{
									sendConsoleMsg("Falha ao entrar na partida...");
								}

								return null;
							}
						});

					}

				}
				return null;
			}
		};
	}

	private void killsNodes(GameNode... nodesParaLimpar)
	{
		for (GameNode node : nodesParaLimpar)
		{
			node.kill();
		}
	}

}

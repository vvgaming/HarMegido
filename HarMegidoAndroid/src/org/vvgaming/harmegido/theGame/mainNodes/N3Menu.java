package org.vvgaming.harmegido.theGame.mainNodes;

import java.util.Arrays;
import java.util.List;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.NTimer;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonImage;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonText;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NGroupToggleButton;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NToggleButton;
import org.vvgaming.harmegido.uos.ServerDriverFacade;
import org.vvgaming.harmegido.uos.UOSFacade;

import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.github.detentor.codex.collections.immutable.ListSharp;
import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.function.Function1;
import com.github.detentor.codex.monads.Either;
import com.github.detentor.codex.monads.Option;

/**
 * Menu de opções para entrar em uma partida. <br/>
 * - Mostra partidas em andamento <br/>
 * - Escolhe o time <br/>
 * - Escolhe o nome do jogador (dentre 3 possíveis aleatórios) <br/>
 * - Escolhe partida para entrar <br/>
 * - Solicita criação de nova partida <br/>
 * 
 * @author Vinicius Nogueira
 */
public class N3Menu extends GameNode
{

	private NText partidasTxt;

	private NToggleButton anjos;
	private NToggleButton demonios;

	@Override
	public void init()
	{
		super.init();

		partidasTxt = new NText((int) getGameWidth(.5f), (int) getGameHeight(.5f), "procurando partidas...");
		partidasTxt.face = Typeface.createFromAsset(getGameAssetManager().getAndroidAssets(), "fonts/Radio Trust.ttf");
		partidasTxt.paint.setTextAlign(Align.CENTER);
		partidasTxt.vAlign = NText.VerticalAlign.MIDDLE;
		partidasTxt.size = 50;

		final NButtonImage anjosBtn = new NButtonImage(new NImage(new Ponto(getGameWidth(.25f), getGameHeight(.8f)), getGameAssetManager()
				.getBitmap(R.drawable.kayle)));
		anjosBtn.getImage().setWidthKeepingRatio(getGameWidth(.4f));
		anjos = new NToggleButton(anjosBtn);

		final NButtonImage demoniosBtn = new NButtonImage(new NImage(new Ponto(getGameWidth(.75f), getGameHeight(.8f)),
				getGameAssetManager().getBitmap(R.drawable.morgana)));
		demoniosBtn.getImage().setWidthKeepingRatio(getGameWidth(.4f));
		demonios = new NToggleButton(demoniosBtn);

		final NGroupToggleButton group = new NGroupToggleButton(anjos, demonios);
		group.setOnToggleChange(new Function1<Option<Integer>, Void>()
		{

			@Override
			public Void apply(final Option<Integer> arg0)
			{
				if (arg0.notEmpty())
				{
					System.out.println(arg0);
					if (arg0.get() == 0)
					{
						getGameAssetManager().playSound(R.raw.kayle);
					}
					if (arg0.get() == 1)
					{
						getGameAssetManager().playSound(R.raw.morgana);
					}
				}
				return null;
			}
		});

		NImage bgImg = new NImage(new Ponto(getGameWidth(.5f), getGameHeight(.5f)), getGameAssetManager().getBitmap(R.drawable.bg_intro));
		bgImg.setHeightKeepingRatio(getGameHeight());

		addSubNode(bgImg, 0);

		addSubNode(group, 1);
		addSubNode(partidasTxt, 1);
		addSubNode(new NTimer(5000, new Function0<Void>()
		{
			@Override
			public Void apply()
			{
				partidasTxt.text = ListSharp.from(getPartidas()).mkString("\n");
				return null;
			}
		}));

		addSubNode(new NButtonText(new NText(100, 100, "ola mundo")), 1);

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
			if (partidas.getRight().isEmpty())
			{
				Arrays.asList("nenhuma");
			}
			return partidas.getRight();
		}
		else
		{
			return Arrays.asList("nenhuma");
		}
	}

	@Override
	public boolean onTouch(final MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			RootNode.getInstance().changeMainNode(new N4Partida());
			return true;
		}
		return false;
	}

}

package org.vvgaming.harmegido.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.lib.model.Enchantment;
import org.vvgaming.harmegido.lib.model.EnchantmentImage;
import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.Match.MatchDuration;
import org.vvgaming.harmegido.lib.model.OpenCVMatWrapper;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.lib.model.Scoreboard;
import org.vvgaming.harmegido.lib.model.TeamType;
import org.vvgaming.harmegido.uos.ServerDriverFacade;
import org.vvgaming.harmegido.uos.UOSFacade;
import org.vvgaming.harmegido.util.DeviceInfo;
import org.vvgaming.harmegido.util.MatchManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.detentor.codex.monads.Either;
import com.github.detentor.codex.product.Tuple2;
import com.github.detentor.codex.util.Reflections;

public class TesteState extends Activity
{
	private final ServerDriverFacade sdf = ServerDriverFacade.from(UOSFacade.getUos(), 20000);
	private Match partida;
	private Player jogador;
	private Player inimigo;
	private List<Enchantment> encantamentos = new ArrayList<>();

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.teste_state);

		final String playerName = "VINICIUS";
		jogador = Player.from(playerName, DeviceInfo.getDeviceId(), TeamType.DARK);
		inimigo = Player.from("inimigo", "enemyDevice", TeamType.LIGHT);

		((Button) findViewById(R.id.btnListarPartidas)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				final Either<Exception, List<String>> retorno = sdf.listarPartidas();
				setText(formatarRetorno(retorno));
			}
		});

		((Button) findViewById(R.id.btnCriarPartida)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				final Either<Exception, Match> retorno = sdf.criarPartida("partida" + new Random().nextInt(), MatchDuration.FIVE_MINUTES);
				setText(formatarRetorno(retorno));
				if (retorno.isRight())
				{
					partida = retorno.getRight();
				}
			}
		});

		((Button) findViewById(R.id.btnEntrarPartida)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (partida == null)
				{
					setText("Crie uma partida antes de entrar.");
					return;
				}
				final Either<Exception, Boolean> retorno = sdf.adicionarJogador(partida.getNomePartida(), jogador);
				setText(formatarRetorno(retorno));
				sdf.adicionarJogador(partida.getNomePartida(), inimigo);
			}
		});

		((Button) findViewById(R.id.btnSairPartida)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (partida == null)
				{
					setText("Crie uma partida antes de sair.");
					return;
				}
				final Either<Exception, Boolean> retorno = sdf.removerJogador(partida.getNomePartida(), jogador);
				setText(formatarRetorno(retorno));
			}
		});

		((Button) findViewById(R.id.btnEncontrarPartida)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				final Either<Exception, Match> retorno = sdf.encontrarPartida(jogador);
				if (retorno.isLeft())
				{
					setText(formatarRetorno(retorno));
				}
				else
				{
					partida = retorno.getRight();
					MatchManager.definirPartida(partida);
					setText(retorno.getRight().getNomePartida() + "/" + retorno.getRight().getFimPartida());
				}
			}
		});

		((Button) findViewById(R.id.btnListarJogadores)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				final Either<Exception, List<Tuple2<String, List<Tuple2<TeamType, Integer>>>>> retorno = sdf.listarJogadores();
				setText(formatarRetorno(retorno));
			}
		});

		((Button) findViewById(R.id.btnMudarTime)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				final TeamType novoTime = jogador.getTime() == TeamType.DARK ? TeamType.LIGHT : TeamType.DARK;
				final Either<Exception, Boolean> retorno = sdf.mudarTime(partida.getNomePartida(), jogador, novoTime);
				setText(formatarRetorno(retorno));
			}
		});

		((Button) findViewById(R.id.btnEncantar)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (partida == null)
				{
					setText("Crie uma partida antes de encantar.");
					return;
				}
				final byte[] bytes = new byte[20];
				Arrays.fill(bytes, (byte) -100);
				final byte[] bytes2 = new byte[20];
				Arrays.fill(bytes2, (byte) -100);
				
				final EnchantmentImage enchant = EnchantmentImage.from(OpenCVMatWrapper.from(bytes, 10, 10, 0),
						OpenCVMatWrapper.from(bytes2, 10, 10, 0));
				final Either<Exception, Boolean> retorno = sdf.encantarObjeto(partida.getNomePartida(), jogador, enchant);
				setText(formatarRetorno(retorno));
			}
		});

		((Button) findViewById(R.id.btnListarEncantamentos)).setOnClickListener(new View.OnClickListener()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(final View v)
			{
				final Either<Exception, Object> retorno = sdf.encontrarPartida(jogador).map(
						Reflections.lift(Match.class, "getEncantamentos"));
				if (retorno.isRight())
				{
					encantamentos = (List<Enchantment>) retorno.getRight();
				}
				setText(formatarRetorno(retorno));
			}
		});

		((Button) findViewById(R.id.btnDesencantar)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (partida == null)
				{
					setText("Crie uma partida antes de desencantar.");
					return;
				}

				if (partida.getEncantamentos().isEmpty())
				{
					setText("Nenhum encantamento para desencantar.");
					return;
				}

				final Either<Exception, Boolean> retorno = sdf.desencantarObjeto(partida.getNomePartida(), inimigo, partida.getEncantamentos().get(0));
				setText(formatarRetorno(retorno));
			}
		});

		((Button) findViewById(R.id.btnListarDesencamentos)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				final StringBuilder sBuilder = new StringBuilder();

				for (final Enchantment enchant : encantamentos)
				{
					sBuilder.append("[" + enchant.toString() + " / " + enchant.getDesencantamento().toString() + "]\n");
				}

				setText(sBuilder.toString());
			}
		});

		((Button) findViewById(R.id.btnVerPontuacao)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (partida == null)
				{
					setText("Crie uma partida antes de calcular a pontuacao.");
					return;
				}

				final Either<Exception, Scoreboard> retorno = sdf.getPontuacao(partida.getNomePartida());

				if (retorno.isLeft())
				{
					setText(formatarRetorno(retorno));
				}
				else
				{
					String theStr = "";
					for (final TeamType time : TeamType.values())
					{
						theStr += time.toString() + ":" + retorno.getRight().getPontuacao(time) + "\n";
					}
					setText(theStr);
				}
			}
		});
		
		((Button) findViewById(R.id.btnVerHorario)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				final Either<Exception, Date> retorno = sdf.getHoraServidor();
				sdf.getTimeSync().getLocalTime(partida.getInicioPartida());
				setText(formatarRetorno(retorno));
			}
		});
		
		((Button) findViewById(R.id.btnVerPontuacaoTodasPartidas)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				Either<Exception, List<String>> partidas = sdf.listarPartidas();
				
				if (partidas.isRight())
				{
					String toShow = "";
					for (String partida : partidas.getRight())
					{
						Either<Exception, Scoreboard> pontuacao = sdf.getPontuacao(partida);
						if (pontuacao.isRight())
						{
							toShow += pontuacao.getRight() + "\n";
						}
					}
					setText(toShow);
				}
			}
		});
		
		((Button) findViewById(R.id.btnVerPontuacaoTodasPartidasLocal)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				Either<Exception, List<String>> partidas = sdf.listarPartidas();
				
				if (partidas.isRight())
				{
					String toShow = "";
					for (String partida : partidas.getRight())
					{
						//Adiciona o jogador
						sdf.adicionarJogador(partida, jogador);
						
						Match match = sdf.encontrarPartida(jogador).getRight();
						match.setSync(sdf.getTimeSync());
						toShow += match.getPontuacao(TeamType.DARK) + " / " + match.getPontuacao(TeamType.LIGHT) + "\n";
						sdf.removerJogador(partida, jogador);
					}
					setText(toShow);
				}
			}
		});
		
		((Button) findViewById(R.id.btnVerPontuacaoLocal)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				setText("Dark: " + partida.getPontuacao(TeamType.DARK) + "/ Light: " + partida.getPontuacao(TeamType.DARK));
			}
		});

		// métodos
		// sdf.adicionarJogador(nomePartida, jogador) //OK
		// sdf.criarPartida(nomePartida, duracao) //OK
		// sdf.desencantarObjeto(nomePartida, jogador, encantamento) //OK
		// sdf.encantarObjeto(nomePartida, jogador, imagem) //OK
		// sdf.encontrarPartida(jogador) //OK
		// sdf.listarJogadores() //OK
		// sdf.listarPartidas() //OK
		// sdf.mudarTime(nomePartida, jogador, novoTime) //OK
		// sdf.removerJogador(nomePartida, jogador) //OK
		// sdf.getPontuacao(nomePartida) //OK

	}

	private static <A> String formatarRetorno(final Either<Exception, A> either)
	{
		return either.isLeft() ? either.getLeft().getMessage() : either.getRight().toString();
	}

	private final void setText(final String text)
	{
		((TextView) findViewById(R.id.txtViewRetorno)).setText(text);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}

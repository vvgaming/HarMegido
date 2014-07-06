package org.vvgaming.harmegido.test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.Match.MatchDuration;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.lib.model.TeamType;
import org.vvgaming.harmegido.uos.ServerDriverFacade;
import org.vvgaming.harmegido.uos.UOSFacade;
import org.vvgaming.harmegido.util.DeviceInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.detentor.codex.monads.Either;

public class TesteState extends Activity
{
	private final ServerDriverFacade sdf = ServerDriverFacade.from(UOSFacade.getUos(), 20000);
	private Match partida;
	private Player jogador;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.teste_state);
		
		final String playerName = "VINICIUS";
		jogador = Player.from(playerName, DeviceInfo.getDeviceId(), TeamType.DARK);
		

		((Button) findViewById(R.id.btnListarPartidas)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final Either<Exception, List<String>> retorno = sdf.listarPartidas();
				setText(formatarRetorno(retorno));
			}
		});
		
		((Button) findViewById(R.id.btnCriarPartida)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Either<Exception, Match> retorno = sdf.criarPartida("partida" + new Random().nextInt(), MatchDuration.FIVE_MINUTES);
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
			public void onClick(View v)
			{
				if (partida == null)
				{
					setText("Crie uma partida antes de entrar.");
					return;
				}
				Either<Exception, Boolean> retorno = sdf.adicionarJogador(partida.getNomePartida(), jogador);
				setText(formatarRetorno(retorno));
			}
		});
		
		((Button) findViewById(R.id.btnEncontrarPartida)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Either<Exception, Match> retorno = sdf.encontrarPartida(jogador);
				setText(formatarRetorno(retorno));
			}
		});
		
		((Button) findViewById(R.id.btnListarJogadores)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Either<Exception, Map<String, Map<TeamType, Integer>>> retorno = sdf.listarJogadores();
				setText(formatarRetorno(retorno));
			}
		});
		
		((Button) findViewById(R.id.btnMudarTime)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final TeamType novoTime = jogador.getTime() == TeamType.DARK ? TeamType.LIGHT : TeamType.DARK; 
				Either<Exception, Boolean> retorno = sdf.mudarTime(partida.getNomePartida(), jogador, novoTime);
				setText(formatarRetorno(retorno));
			}
		});
		
//		((Button) findViewById(R.id.btnEncantar)).setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Either<Exception, Match> retorno = sdf.encantarObjeto(nomePartida, jogador, imagem);
//			}
//		});
		
		//métodos
//		sdf.adicionarJogador(nomePartida, jogador) 						//OK
//		sdf.criarPartida(nomePartida, duracao)   						//OK
//		sdf.desencantarObjeto(nomePartida, jogador, encantamento)
//		sdf.encantarObjeto(nomePartida, jogador, imagem)
//		sdf.encontrarPartida(jogador)									//OK
//		sdf.listarJogadores()											//OK
//		sdf.listarPartidas()     										//OK
//		sdf.mudarTime(nomePartida, jogador, novoTime)
//		sdf.removerJogador(nomePartida, jogador)
		
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

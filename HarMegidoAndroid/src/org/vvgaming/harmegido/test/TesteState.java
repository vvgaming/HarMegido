package org.vvgaming.harmegido.test;

import java.util.List;
import java.util.Random;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.lib.model.Match;
import org.vvgaming.harmegido.lib.model.Match.MatchDuration;
import org.vvgaming.harmegido.lib.model.Player;
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
		jogador = Player.from(playerName, DeviceInfo.getDeviceId());
		

		((Button) findViewById(R.id.btnListarPartidas)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Either<Exception, List<String>> retorno = sdf.listarPartidas();
				
				if (retorno.isLeft())
				{
					((TextView) findViewById(R.id.txtViewRetorno)).setText(retorno.getLeft().getMessage());
				}
				else
				{
					((TextView) findViewById(R.id.txtViewRetorno)).setText(retorno.getRight().toString());
				}
			}
		});
		
		((Button) findViewById(R.id.btnCriarPartida)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Either<Exception, Match> retorno = sdf.criarPartida("partida" + new Random().nextInt(), MatchDuration.FIVE_MINUTES);
				if (retorno.isLeft())
				{
					((TextView) findViewById(R.id.txtViewRetorno)).setText(retorno.getLeft().getMessage());
				}
				else
				{
					((TextView) findViewById(R.id.txtViewRetorno)).setText(retorno.getRight().toString());
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
					((TextView) findViewById(R.id.txtViewRetorno)).setText("Crie uma partida antes de entrar.");
					return;
				}
				Either<Exception, Boolean> retorno = sdf.adicionarJogador(partida.getNomePartida(), jogador);
				if (retorno.isLeft())
				{
					((TextView) findViewById(R.id.txtViewRetorno)).setText(retorno.getLeft().getMessage());
				}
				else
				{
					((TextView) findViewById(R.id.txtViewRetorno)).setText(retorno.getRight().toString());
					
				}
			}
		});
		
		((Button) findViewById(R.id.btnEncontrarPartida)).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Either<Exception, Match> retorno = sdf.encontrarPartida(jogador);
				
				if (retorno.isLeft())
				{
					((TextView) findViewById(R.id.txtViewRetorno)).setText(retorno.getLeft().getMessage());
				}
				else
				{
					((TextView) findViewById(R.id.txtViewRetorno)).setText(retorno.getRight().toString());
					
				}
			}
		});
		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}

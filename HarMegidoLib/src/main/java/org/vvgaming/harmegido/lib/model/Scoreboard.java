package org.vvgaming.harmegido.lib.model;

import java.util.ArrayList;
import java.util.List;

import com.github.detentor.codex.product.Tuple2;

/**
 * Representa a pontuação de uma partida do HarMegido
 */
public class Scoreboard
{
	private final String matchName;
	private final List<Tuple2<TeamType, Integer>> pontuacao;
	
	private Scoreboard(String matchName, List<Tuple2<TeamType, Integer>> pontuacao)
	{
		super();
		this.matchName = matchName;
		this.pontuacao = pontuacao;
	}
	
	/**
	 * Cria uma classe de pontuação para a partida informada
	 * @param match A partida a ser criada a pontuação
	 * @return Uma classe com a pontuação de cada equipe na partida
	 */
	public static Scoreboard from(final Match match)
	{
		final List<Tuple2<TeamType, Integer>> pontuacao = new ArrayList<Tuple2<TeamType,Integer>>();
		
		for (TeamType time : TeamType.values())
		{
			pontuacao.add(Tuple2.from(time, match.getPontuacao(time)));
		}
		
		return new Scoreboard(match.getNomePartida(), pontuacao);
	}
	
	/**
	 * Retorna o nome da partida à qual essa pontuação se refere
	 * @return O nome da partida que essa pontuação se refere
	 */
	public String getNomePartida()
	{
		return matchName;
	}

	/**
	 * Retorna a pontuação do time informado
	 * @param time O time a ser retornada a pontuação
	 * @return Um inteiro não negativo com a pontuação do time informado
	 */
	public int getPontuacao(final TeamType time)
	{
		for (Tuple2<TeamType, Integer> curTuple : pontuacao)
		{
			if (curTuple.getVal1().equals(time))
			{
				return curTuple.getVal2();
			}
		}
		throw new IllegalArgumentException("Pontuação não encontrada para o time passado como parâmetro: " + time);
	}
}

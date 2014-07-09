package org.vvgaming.harmegido.util;

import org.vvgaming.harmegido.lib.model.Match;

import com.github.detentor.codex.monads.Option;

/**
 * Classe criada para controlar a partida ativa (se ela existir). <br/>
 * Ao invés de manter uma referência direta a uma partida, quem precisar de referência a uma partida pode usar apenas essa classe.
 */
public final class MatchManager
{
	// A partida atual
	private static Match partida;

	private MatchManager()
	{
		// singleton
	}

	/**
	 * Defina a partida atual como a partida passada como parâmetro
	 * 
	 * @param aPartida A partida a ser definida como partida atual
	 * @throws IllegalArgumentException Se a partida estiver nula
	 */
	public static void definirPartida(final Match aPartida)
	{
		if (aPartida == null)
		{
			throw new IllegalArgumentException("A partida não pode ser nula");
		}

		partida = aPartida;
	}

	/**
	 * Retira a partida atual da memória, útil para quando for sair da partida para entrar em outra
	 */
	public static void limparPartida()
	{
		partida = null;
	}

	/**
	 * Retorna a partida ativa neste momento, se ela existir
	 * 
	 * @return Uma {@link Option} que irá conter a partida ativa, se ela existir
	 */
	public static Option<Match> getPartida()
	{
		return Option.from(partida);
	}
}

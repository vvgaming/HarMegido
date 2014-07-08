package org.vvgaming.harmegido.lib.model.match;

import org.vvgaming.harmegido.lib.model.Enchantment;
import org.vvgaming.harmegido.lib.model.EnchantmentImage;
import org.vvgaming.harmegido.lib.model.Player;
import org.vvgaming.harmegido.lib.model.TeamType;

/**
 * Representa a troca de estado de uma partida. ADT (Algebraic Data-Type). <br/>
 * ATENÇÃO: Todas as alterações que forem dependentes de tempo (data/hora atual, por exemplo),
 * dependerão do tempo do servidor. Isso significa que pode haver uma ligeira discrepância de 
 * quando alguém encantou algo, por exemplo, e de quando o servidor registrou que algo foi desencantado.
 */
public abstract class MatchState
{
	/**
	 * Cria uma mudança de estado que representa a adição de um jogador à partida.
	 * @param jogador O jogador a ser adicionado a uma partida.
	 * @return Um mudança de estado que representa a adição de um jogador numa partida.
	 */
	public static MatchState adicionarJogador(final Player jogador)
	{
		return new PlayerChangeAdd(validatePlayer(jogador));
	}
	
	/**
	 * Cria uma mudança de estado para remover um jogador da partida.
	 * @param jogador O jogador a ser removido de uma partida.
	 * @return Um mudança de estado que representa a remoção de um jogador de uma partida.
	 */
	public static MatchState removerJogador(final Player jogador)
	{
		return new PlayerChangeRemove(validatePlayer(jogador));
	}
	
	/**
	 * Cria uma mudança de estado para mudar o time de um jogador
	 * @param jogador O jogador a ser mudado de time.
	 * @param novoTime O novo time do jogador
	 * @return Um mudança de estado que representa a alteração do time de um jogador.
	 */
	public static MatchState mudarTime(final Player jogador, final TeamType novoTime)
	{
		if (novoTime == null)
		{
			throw new IllegalArgumentException("O novo time não pode ser nulo");
		}
		return new PlayerChangeTeam(validatePlayer(jogador), novoTime);
	}
	
	/**
	 * Cria uma mudança de estado que representa o encantamento de algum objeto
	 * @param jogador O jogador a efetuar o encantamento
	 * @param enchantImage A imagem que representa o objeto encantado
	 * @return Um mudança de estado que representa a criação do encantamento pelo jogador
	 */
	public static MatchState encantar(final Player jogador, final EnchantmentImage enchantImage)
	{
		if (enchantImage == null)
		{
			throw new IllegalArgumentException("A imagem do encantamento não pode ser nulo");
		}
		return new PlayerChangeEnchant(validatePlayer(jogador), enchantImage);
	}
	
	/**
	 * Cria uma mudança de estado que representa o desencantamento de algum objeto
	 * @param jogador O jogador a efetuar o desencantamento
	 * @param enchantment O encantamento a ser desencantado
	 * @return Um mudança de estado que representa o desencantamento pelo jogador
	 */
	public static MatchState desencantar(final Player jogador, final Enchantment enchantment)
	{
		if (enchantment == null)
		{
			throw new IllegalArgumentException("O encantamento a ser desencantado não pode ser nulo");
		}
		return new PlayerChangeDisenchant(validatePlayer(jogador), enchantment);
	}
	
	/**
	 * Valida se a variável jogador está nula, disparando exceção se estiver
	 * @param jogador O jogador a ser validado
	 * @return O jogador passado como parâmetro, se a variável não estiver nula
	 */
	private static Player validatePlayer(final Player jogador)
	{
		if (jogador == null)
		{
			throw new IllegalArgumentException("O jogador não pode ser nulo");
		}
		return jogador;
	}

}

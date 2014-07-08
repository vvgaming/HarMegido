package org.vvgaming.harmegido.lib.model;

import java.util.Date;

import org.vvgaming.harmegido.lib.util.Copyable;

/**
 * Essa classe modela uma entidade jogador do jogo
 */
public class Player implements Copyable
{
	private final String playerName;
	private final String dispositivo;
	private TeamType time; // o time que o jogador pertence

	private Player(final String playerName, final String device, final TeamType time)
	{
		this.playerName = playerName;
		this.dispositivo = device;
		this.time = time;
	}

//	/**
//	 * Cria um novo jogador a partir de um nome e do seu identificador do dispositivo
//	 * 
//	 * @param playerName O nome do jogador
//	 * @param device O identificador único do dispositivo do jogador
//	 * @return Uma instância de um jogador, inicializado com as informações passadas como parâmetro
//	 */
//	public static Player from(final String playerName, final String device)
//	{
//		return from(playerName, device, null);
//	}

	/**
	 * Cria um novo jogador a partir de um nome, identificador do dispositivo e qual time ele pertence.
	 * 
	 * @param playerName O nome do jogador
	 * @param device O identificador único do dispositivo do jogador
	 * @param time O time ao qual aquele jogador estará vinculado. Pode ser nulo.
	 * @return Uma instância de um jogador, inicializado com as informações passadas como parâmetro
	 */
	public static Player from(final String playerName, final String device, final TeamType time)
	{
		if (playerName == null || playerName == "")
		{
			throw new IllegalArgumentException("O nome do jogador não pode ser nulo nem vazio");
		}

		if (device == null || device == "")
		{
			throw new IllegalArgumentException("O identificador do dispositivo do jogador não pode ser nulo nem vazio");
		}
		
		if (time == null)
		{
			throw new IllegalArgumentException("O time não pode estar nulo.");
		}

		return new Player(playerName, device, time);
	}

	public TeamType getTime()
	{
		return time;
	}

	public String getPlayerName()
	{
		return playerName;
	}
	
	/**
	 * Retorna a identificação única deste jogador
	 * @return Uma identificação única para este jogador
	 */
	public String getIdJogador()
	{
		return dispositivo;
	}

	public void trocarDeTime(final TeamType novoTime)
	{
		this.time = novoTime;
	}
	
	

	/**
	 * Cria um encantamento a partir da data/hora passada como parâmetro
	 * 
	 * @param timestamp A data/hora que o encantamento aconteceu
	 * @param imagem A imagem do objeto a ser encantado
	 * @return Um encantamento criado por este jogador
	 */
	public Enchantment encantar(final Date timestamp, final EnchantmentImage imagem)
	{
		return Enchantment.from(this, timestamp, imagem);
	}

	/**
	 * Cria um desencantamento para o encantamento passado como par�metro
	 * 
	 * @param encantamento O encantamento que est� sendo desencantado
	 * @param timestamp A data/hora que o desencantamento aconteceu
	 * @return O desencantamento criado por este jogador
	 */
	public Disenchantment desencantar(final Enchantment encantamento, final Date timestamp)
	{
		return Disenchantment.from(this, timestamp, encantamento);
	}

	public Player copy()
	{
		return from(this.playerName, this.dispositivo, this.time);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (dispositivo == null ? 0 : dispositivo.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Player other = (Player) obj;
		if (dispositivo == null)
		{
			if (other.dispositivo != null)
			{
				return false;
			}
		}
		else if (!dispositivo.equals(other.dispositivo))
		{
			return false;
		}
		return true;
	}

}

package org.vvgaming.harmegido.lib.model;

/**
 * Esse enum representa os tipos possíveis de time
 */
public enum TeamType
{
	LIGHT, DARK;

	@Override
	public String toString()
	{
		String toReturn = "";

		switch (this)
		{
		case LIGHT:
			toReturn = "Light";
			break;
		case DARK:
			toReturn = "Dark";
			break;
		default:
			throw new IllegalArgumentException("Tipo não reconhecido: " + this);
		}
		return toReturn;
	}
}

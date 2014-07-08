package org.vvgaming.harmegido.lib.model;

import java.util.Arrays;

/**
 * Representa um Encantamento. <br/>
 * Possui a imagem a ser exibida e as features.
 */
public class EnchantmentImage
{
	//ATENÇÃO: SE MUDAR REESCREVA O EQUALS E O HASHCODE
	private final byte[] imagem;
	private final byte[] features;

	private EnchantmentImage(final byte[] imagem, final byte[] features)
	{
		super();
		this.imagem = imagem;
		this.features = features;
	}

	/**
	 * Cria uma nova imagem de encantamento a partir dos bytes da imagem e das features 
	 * @param imagem A imagem que representa o encantamento
	 * @param features As features da imagem, usadas para o reconhecimento
	 * @return Uma imagem de encantamento a partir da imagem e das features
	 */
	public static EnchantmentImage from(final byte[] imagem, final byte[] features)
	{
		return new EnchantmentImage(imagem, features);
	}
	
	
	/**
	 * Retorna os bytes da imagem representa por este encantamento
	 * @return
	 */
	public byte[] getImagem()
	{
		return imagem;
	}

	/**
	 * Retorna o vetor de bytes que contém as features deste encantamento
	 * @return
	 */
	public byte[] getFeatures()
	{
		return features;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(features);
		result = prime * result + Arrays.hashCode(imagem);
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
		final EnchantmentImage other = (EnchantmentImage) obj;
		if (!Arrays.equals(features, other.features))
		{
			return false;
		}
		if (!Arrays.equals(imagem, other.imagem))
		{
			return false;
		}
		return true;
	}

}

package org.vvgaming.harmegido.lib.model;

/**
 * Representa um Encantamento. <br/>
 * Possui a imagem a ser exibida e as features.
 */
public class EnchantmentImage
{
	// ATENÇÃO: SE MUDAR REESCREVA O EQUALS E O HASHCODE
	private final OpenCVMatWrapper imagem;
	private final OpenCVMatWrapper features;
	
	//referência ao encantamento dummy
	public static final EnchantmentImage dummy = from(OpenCVMatWrapper.from("", 0, 0, 0), OpenCVMatWrapper.from("", 0, 0, 0));
	

	public EnchantmentImage(final OpenCVMatWrapper imagem, final OpenCVMatWrapper features)
	{
		super();
		this.imagem = imagem;
		this.features = features;
	}

	/**
	 * Cria uma nova imagem de encantamento a partir dos bytes da imagem e das features
	 * 
	 * @param imagem A imagem que representa o encantamento
	 * @param features As features da imagem, usadas para o reconhecimento
	 * @return Uma imagem de encantamento a partir da imagem e das features
	 */
	public static EnchantmentImage from(final OpenCVMatWrapper imagem, final OpenCVMatWrapper features)
	{
		return new EnchantmentImage(imagem, features);
	}

	/**
	 * Retorna o wrapper da mat da imagem representa por este encantamento
	 * 
	 * @return
	 */
	public OpenCVMatWrapper getImagem()
	{
		return imagem;
	}

	/**
	 * Retorna o wrapper da mat que contém as features deste encantamento
	 * 
	 * @return
	 */
	public OpenCVMatWrapper getFeatures()
	{
		return features;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (features == null ? 0 : features.hashCode());
		result = prime * result + (imagem == null ? 0 : imagem.hashCode());
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
		if (features == null)
		{
			if (other.features != null)
			{
				return false;
			}
		}
		else if (!features.equals(other.features))
		{
			return false;
		}
		if (imagem == null)
		{
			if (other.imagem != null)
			{
				return false;
			}
		}
		else if (!imagem.equals(other.imagem))
		{
			return false;
		}
		return true;
	}
}

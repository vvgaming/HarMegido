package org.vvgaming.harmegido.lib.util;

/**
 * Abstração de uma imagem capturada ao se encantar um objeto
 */
public interface EnchantmentImage 
{
	/**
	 * Transforma esta interface numa String no formato JSON.
	 * @return Uma String que representa esta interface no formato JSON
	 */
	String getAsJSON();
	
	/**
	 * Transforma uma String no formato JSON para uma inst�ncia desta
	 * interface
	 * @param json A String que representa esta interface
	 * @return Uma instância desta interface, a partir dos dados extraídos da String JSON
	 */
	EnchantmentImage fromJSON(final String json);
}

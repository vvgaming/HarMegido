package org.vvgaming.harmegido.lib.util;

/**
 * Classes que implementam esta interface permitem a operação de cópia
 * 
 */
public interface Copyable 
{
	/**
	 * Retorna uma cópia profunda desta classe, ou seja, guarda retorna "foto" do estado desta classe.
	 * @return Uma cópia profunda desta classe.
	 */
	Copyable copy();
}

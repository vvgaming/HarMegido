package org.vvgaming.harmegido.lib.util;

import java.text.DateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe utilitária para transformar objetos de/para JSON 
 */
public final class JSONTransformer
{
	private static Gson gson;
	
	private JSONTransformer()
	{
		//construtor privado, singleton
	}
	
	/**
	 * Retorna um Gson configurado adequadamente para converter objetos de/para JSON.
	 * @return Uma instância de Gson que converte objetos de/para o formato JSON.
	 */
	public static Gson getGson()
	{
		if (gson == null)
		{
			final GsonBuilder gBuilder = new GsonBuilder();
			gBuilder.setDateFormat(DateFormat.FULL);
			gson = gBuilder.create();
		}
		return gson;
	}
	
	/**
	 * Equivale chamar getGson().toJson 
	 * @param source O objeto a ser transformado em Json
	 * @return Uma string que representa o objeto passado como parâmetro
	 */
	public static String toJson(final Object source)
	{
		return gson.toJson(source);
	}
	
	/**
	 * Equivale chamar getGson().fromJson
	 * @param source A string que contém os dados Json do objeto
	 * @param classOf A classe do objeto a ser transformado
	 * @return Uma instância do objeto do tipo informado
	 */
	public static <T> T fromJson(final String source, final Class<T> classOf)
	{
		return gson.fromJson(source, classOf);
	}
			
	
}

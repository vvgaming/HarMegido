package org.vvgaming.harmegido.lib.util;

import java.text.DateFormat;

import com.github.detentor.codex.monads.Either;
import com.github.detentor.codex.monads.Either.Left;
import com.github.detentor.codex.monads.Either.Right;
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
		if (source instanceof Either<?, ?>)
		{
			//Cria o tipo de either de acordo com a instância
			final EitherWrapper.EitherType eType = (source instanceof Right<?, ?> ? 
						EitherWrapper.EitherType.RIGHT : EitherWrapper.EitherType.LEFT);
			return getGson().toJson(new EitherWrapper(getGson().toJson(source), eType));
		}
		return getGson().toJson(source);
	}

	/**
	 * Equivale chamar getGson().fromJson
	 * @param source A string que contém os dados Json do objeto
	 * @param classOf A classe do objeto a ser transformado
	 * @return Uma instância do objeto do tipo informado
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(final String source, final Class<T> classOf)
	{
		if (classOf.equals(Either.class))
		{
			return (T) getGson().fromJson(source, EitherWrapper.class).getEither();
		}
		return getGson().fromJson(source, classOf);
	}
	
	/**
	 * Um wrapper pro Either funcionar com o JSON
	 */
	private static final class EitherWrapper
	{
		private final String eJson;
		private final EitherType type;
		
		public EitherWrapper(String eitherJson, EitherType type)
		{
			super();
			this.eJson = eitherJson;
			this.type = type;
		}
		
		/**
		 * Retorna o Either representado por esta classe.
		 * @return A instância de Either guardada por esta classe
		 */
		@SuppressWarnings("unchecked")
		public <B, A> Either<B, A> getEither()
		{
			return fromJson(eJson, type == EitherType.LEFT ? Left.class : Right.class);
		}
		
		public enum EitherType
		{
			LEFT, RIGHT;
		}
	}
	
}

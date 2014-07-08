package org.vvgaming.harmegido.lib.util;

import java.lang.reflect.Type;
import java.text.DateFormat;

import org.vvgaming.harmegido.lib.model.match.MatchState;
import org.vvgaming.harmegido.lib.util.wrappers.EitherWrapper;
import org.vvgaming.harmegido.lib.util.wrappers.ExceptionWrapper;
import org.vvgaming.harmegido.lib.util.wrappers.MatchStateWrapper;

import com.github.detentor.codex.monads.Either;
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
	private static Gson getGson()
	{
		if (gson == null)
		{
			final GsonBuilder gBuilder = new GsonBuilder();
			gBuilder.setDateFormat(DateFormat.FULL);
//			gBuilder.registerTypeAdapter(Tuple2.class, new Tuple2Wrapper(null));
			gson = gBuilder.create();
		}
		return gson;
	}
	
	/**
	 * @param source O objeto a ser transformado em Json
	 * @return Uma string que representa o objeto passado como parâmetro
	 */
	public static String toJson(final Object source)
	{
		if (source instanceof Either<?, ?>)
		{
			return getGson().toJson(new EitherWrapper((Either<?, ?>) source));
		}
		else if (source instanceof Exception)
		{
			return getGson().toJson(new ExceptionWrapper((Exception) source));
		}
		else if (source instanceof MatchState)
		{
			final MatchState mState = (MatchState) source;
			final String theJson =  getGson().toJson(source);
			return getGson().toJson(new MatchStateWrapper(mState.getClass().getName(), theJson));
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
		if (isSubclass(classOf, Either.class))
		{
			return (T) getGson().fromJson(source, EitherWrapper.class).getEither();
		}
		else if (isSubclass(classOf, Exception.class))
		{
			return (T) getGson().fromJson(source, ExceptionWrapper.class).getException();
		}
		else if (classOf.getName().equals(MatchState.class.getName()))
		{
			return (T) getGson().fromJson(source, MatchStateWrapper.class).getMatchState();
		}
		return getGson().fromJson(source, classOf);
	}

	/**
	 * Equivale chamar getGson().fromJson
	 * @param source A string que contém os dados Json do objeto
	 * @param typeOf O tipo do objeto a ser transformado
	 * @return Uma instância do objeto do tipo informado
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(final String source, final Type typeOf)
	{
		return (T) getGson().fromJson(source, typeOf);
	}

	/**
	 * Retorna se a classe passada como parâmetro é uma subclasse da classe.
	 * 
	 * @param theClass A classe a ser verificada
	 * @param ofClass A classe que supostamente é uma superclasse
	 * @return Um boolean que representa se o primeiro parâmetro é subclasse do segundo
	 */
	private static <T, U> boolean isSubclass(final Class<T> theClass, final Class<U> ofClass)
	{
		return theClass != null && (theClass.equals(ofClass) || isSubclass(theClass.getSuperclass(), ofClass));
	}
}

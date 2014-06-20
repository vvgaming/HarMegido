package org.vvgaming.harmegido.lib.util;

import java.lang.reflect.Constructor;
import java.text.DateFormat;

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
		return getGson().fromJson(source, classOf);
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
	
	/**
	 * Um wrapper pro Either funcionar com o JSON
	 */
	private static final class EitherWrapper
	{
		private final String eJson;
		private final String elementClass;
		private final EitherType type;
		
		public EitherWrapper(final Either<?, ?> either)
		{
			super();
			
			if (either.isLeft())
			{
				this.type = EitherType.LEFT;
				this.elementClass = either.getLeft().getClass().getName();
				eJson = toJson(either.getLeft());
			}
			else
			{
				this.type = EitherType.RIGHT;
				this.elementClass = either.getRight().getClass().getName();
				eJson = toJson(either.getRight());
			}
		}
		
		/**
		 * Retorna o Either representado por esta classe.
		 * @return A instância de Either guardada por esta classe
		 */
		@SuppressWarnings("unchecked")
		public <B, A> Either<B, A> getEither()
		{
			try
			{
				//Recria recursivamente o valor deste Either
				final Object element = fromJson(eJson, Class.forName(elementClass));

				if (type == EitherType.LEFT)
				{
					return (Either<B, A>) Either.createLeft(element);
				}
				return (Either<B, A>) Either.createRight(element);
			}
			catch (ClassNotFoundException e)
			{
				throw new IllegalArgumentException(e);
			}
		}
		
		public enum EitherType
		{
			LEFT, RIGHT;
		}
	}
	
	/**
	 * Um wrapper pra transformar exceção em JSON
	 */
	private static final class ExceptionWrapper
	{
		private final String exceptionClass;
		private final String message;
		private final StackTraceElement[] stackTrace;
		
		public ExceptionWrapper(final Exception exception)
		{
			super();
			
			exceptionClass = exception.getClass().getName();
			stackTrace = exception.getStackTrace();
			message = exception.getMessage();
		}
		
		/**
		 * Retorna a exceção que esta classe representa
		 * @return A exceção representada por esta classe.
		 */
		public Exception getException()
		{
			try
			{
				final Constructor<?> constructor = Class.forName(exceptionClass).getDeclaredConstructor(String.class);
				final Exception toReturn = (Exception) constructor.newInstance(message);
				toReturn.setStackTrace(stackTrace);
				return toReturn;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
}

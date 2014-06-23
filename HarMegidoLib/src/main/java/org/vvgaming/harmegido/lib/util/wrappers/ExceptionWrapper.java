package org.vvgaming.harmegido.lib.util.wrappers;

import java.lang.reflect.Constructor;

/**
 * Um wrapper pra transformar exceção em JSON
 */
public class ExceptionWrapper
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

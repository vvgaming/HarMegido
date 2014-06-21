package org.vvgaming.harmegido.lib.util.wrappers;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;

import org.vvgaming.harmegido.lib.model.match.MatchState;

/**
 * Um wrapper pra transformar MatchState em JSON
 */
public final class MatchStateWrapper
{
	private final String matchStateClass;
	private final String stateJson;
	
	public MatchStateWrapper(final String className, final String jSon)
	{
		super();
		
		this.matchStateClass = className;
		this.stateJson = jSon;
	}
	
	/**
	 * Retorna a exceção que esta classe representa
	 * @return A exceção representada por esta classe.
	 */
	public MatchState getMatchState()
	{
		try
		{
			return (MatchState) fromJson(stateJson, Class.forName(matchStateClass));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

}

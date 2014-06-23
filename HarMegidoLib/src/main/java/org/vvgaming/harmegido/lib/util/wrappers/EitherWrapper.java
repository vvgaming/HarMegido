package org.vvgaming.harmegido.lib.util.wrappers;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;
import static org.vvgaming.harmegido.lib.util.JSONTransformer.toJson;

import com.github.detentor.codex.monads.Either;

/**
 * Um wrapper pro Either funcionar com o JSON
 */
public final class EitherWrapper
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

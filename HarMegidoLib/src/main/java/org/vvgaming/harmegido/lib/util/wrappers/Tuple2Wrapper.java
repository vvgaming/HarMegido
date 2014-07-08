package org.vvgaming.harmegido.lib.util.wrappers;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;
import static org.vvgaming.harmegido.lib.util.JSONTransformer.toJson;

import com.github.detentor.codex.product.Tuple2;

/**
 * Um wrapper pro Tuple2 funcionar com o JSON
 */
public final class Tuple2Wrapper
{
	private final String ele1Class;
	private final String eJson1;
	
	private final String ele2Class;
	private final String eJson2;

	public Tuple2Wrapper(final Tuple2<?, ?> tuple)
	{
		super();
		
		this.ele1Class = tuple.getVal1().getClass().getName();
		this.eJson1 = toJson(tuple.getVal1());
		
		this.ele2Class = tuple.getVal2().getClass().getName();
		this.eJson2 = toJson(tuple.getVal2());
	}
	
	/**
	 * Retorna o Tuple2 representado por esta classe.
	 * @return A instância de Tuple2 guardada por esta classe
	 */
	@SuppressWarnings("unchecked")
	public <A, B> Tuple2<A, B> getTuple2()
	{
		try
		{
			final Object element1 = fromJson(eJson1, Class.forName(ele1Class));
			final Object element2 = fromJson(eJson2, Class.forName(ele2Class));

			return (Tuple2<A, B>) Tuple2.from(element1, element2);
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
}

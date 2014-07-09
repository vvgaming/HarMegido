package org.vvgaming.harmegido.lib.util.wrappers;

import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;
import static org.vvgaming.harmegido.lib.util.JSONTransformer.toJson;

import java.util.Arrays;
import java.util.List;

/**
 * Um wrapper pro Either funcionar com o JSON
 */
public final class ListWrapper
{
	private final String[] eJson;
	private final String elementClass;
	
	public ListWrapper(final List<?> list)
	{
		super();
		
		if (list.isEmpty())
		{
			elementClass = Object.class.getName();
		}
		else
		{
			elementClass = list.get(0).getClass().getName();
		}
		
		eJson = new String[list.size()];
		
		for (int i = 0; i < list.size(); i++)
		{
			eJson[i] = toJson(list.get(i));
		}
	}

	/**
	 * Retorna a List representada por esta classe.
	 * @return A instância de List guardada por esta classe
	 */
	@SuppressWarnings("unchecked")
	public <A> List<A> getList()
	{
		try
		{
			//Recria recursivamente o valor deste Either
			final Object[] element = new Object[eJson.length];
			
			for (int i = 0; i < element.length; i++)
			{
				element[i] = fromJson(eJson[i], Class.forName(elementClass));
			}
			
			return (List<A>) Arrays.asList(element);
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	

}

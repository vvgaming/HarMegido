//package org.vvgaming.harmegido.lib.util.wrappers;
//
//import static org.vvgaming.harmegido.lib.util.JSONTransformer.fromJson;
//import static org.vvgaming.harmegido.lib.util.JSONTransformer.toJson;
//
//import java.lang.reflect.Type;
//
//import com.github.detentor.codex.product.Tuple2;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.google.gson.JsonPrimitive;
//import com.google.gson.JsonSerializationContext;
//import com.google.gson.JsonSerializer;
//
///**
// * Um wrapper pro Tuple2 funcionar com o JSON
// */
//public final class Tuple2Wrapper implements JsonSerializer<Tuple2<Object, Object>>, JsonDeserializer<Tuple2<Object, Object>>
//{
//	private final String ele1Class;
//	private final String eJson1;
//	
//	private final String ele2Class;
//	private final String eJson2;
//
//	public Tuple2Wrapper(final Tuple2<?, ?> tuple)
//	{
//		super();
//		
//		this.ele1Class = null;
//		this.ele2Class = null;
//		this.eJson1 = null;
//		this.eJson2 = null;
//		
////		this.ele1Class = tuple.getVal1().getClass().getName();
////		this.eJson1 = toJson(tuple.getVal1());
////		
////		this.ele2Class = tuple.getVal2().getClass().getName();
////		this.eJson2 = toJson(tuple.getVal2());
//	}
//	
//	/**
//	 * Retorna o Tuple2 representado por esta classe.
//	 * @return A instância de Tuple2 guardada por esta classe
//	 */
//	@SuppressWarnings("unchecked")
//	public <A, B> Tuple2<A, B> getTuple2()
//	{
//		try
//		{
//			final Object element1 = fromJson(eJson1, Class.forName(ele1Class));
//			final Object element2 = fromJson(eJson2, Class.forName(ele2Class));
//
//			return (Tuple2<A, B>) Tuple2.from(element1, element2);
//		}
//		catch (ClassNotFoundException e)
//		{
//			throw new IllegalArgumentException(e);
//		}
//	}
//
//	@Override
//	public JsonElement serialize(Tuple2<Object, Object> src, Type typeOfSrc, JsonSerializationContext context)
//	{
//		JsonArray jsonArray = new JsonArray();
//		
//		final String ele1Class = src.getVal1().getClass().getName();
//		final String ele2Class = src.getVal1().getClass().getName();
//		
//		jsonArray.add(context.serialize(ele1Class));
//		jsonArray.add(context.serialize(ele2Class));
//
//		jsonArray.add(context.serialize(src.getVal1()));
//		jsonArray.add(context.serialize(src.getVal2()));
//		
//		return jsonArray;
//	}
//
//	@Override
//	public Tuple2<Object, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
//	{
//		JsonArray jsonArray = json.getAsJsonArray();
//		
//		final String ele1Class = jsonArray.get(0).getAsString();
//		final String ele2Class = jsonArray.get(1).getAsString();
//		
//		try
//		{
//			Object firstEle = context.deserialize(jsonArray.get(2), Class.forName(ele1Class));
//			Object secondEle = context.deserialize(jsonArray.get(3), Class.forName(ele2Class));
//			return Tuple2.from(firstEle, secondEle);
//		}
//		catch (ClassNotFoundException e)
//		{
//			throw new IllegalArgumentException(e);
//		}
//	}
//}

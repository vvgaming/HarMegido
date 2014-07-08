package org.vvgaming.harmegido.util;

import org.opencv.core.Size;

import com.github.detentor.codex.product.Tuple3;

public class Constantes
{
	// TODO nao sei pq, em alguns lugares eu me
	// confundi entre altura e largura, aí virou uma baderna
	// em cada lugar em faço uma inversão, depois tem que
	// varrer o código e fazer uma limpeza dessas inversões entre altura e largura
	public static final Size camNormalSize = new Size(640, 480);
	public static final Size camPreviewSize = new Size(160, 120);
	
	
//	public static final Tuple3<Integer, Integer, Integer> ANGEL_COLOR = Tuple3.from(43, 167, 203);
	public static final Tuple3<Integer, Integer, Integer> ANGEL_COLOR = Tuple3.from(166,148,112);
	public static final Tuple3<Integer, Integer, Integer> DEMON_COLOR = Tuple3.from(255, 0, 0);

}

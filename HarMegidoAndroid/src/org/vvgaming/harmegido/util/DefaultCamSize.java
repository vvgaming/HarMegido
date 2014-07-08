package org.vvgaming.harmegido.util;

import org.opencv.core.Size;

public class DefaultCamSize
{
	// TODO nao sei pq, em alguns lugares eu me
	// confundi entre altura e largura, aí virou uma baderna
	// em cada lugar em faço uma inversão, depois tem que
	// varrer o código e fazer uma limpeza dessas inversões entre altura e largura
	public static final Size normalSize = new Size(640, 480);
	public static Size previewSize = new Size(160, 120);
}

package org.vvgaming.harmegido.vision;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class OCVUtil {

	public static void releaseMat(Mat mat) {
		if (mat != null)
			mat.release();
	}

	public static void releaseMat(Mat... mats) {
		for (Mat m : mats)
			releaseMat(m);
	}

	public static Bitmap toBmp(Mat mat) {
		Bitmap retorno = Bitmap.createBitmap(mat.cols(), mat.rows(),
				Config.RGB_565);
		Utils.matToBitmap(mat, retorno);
		return retorno;
	}

}

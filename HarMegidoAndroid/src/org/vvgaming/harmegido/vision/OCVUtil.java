package org.vvgaming.harmegido.vision;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * Classe de utilitários para interação com Objetos da OpenCV
 * 
 * @author Vinicius Nogueira
 */
public class OCVUtil {

	/**
	 * Libera as matrizes, se não nulas
	 * 
	 * @param mats
	 */
	public static void releaseMat(Mat... mats) {
		for (Mat m : mats) {
			if (m != null) {
				m.release();
			}
		}
	}

	/**
	 * Converte Mat da OpenCV em Bitmap do Android
	 * 
	 * @param mat
	 * @return o Bitmap
	 */
	public static Bitmap toBmp(Mat mat) {
		Bitmap retorno = Bitmap.createBitmap(mat.cols(), mat.rows(),
				Config.RGB_565);
		Utils.matToBitmap(mat, retorno);
		return retorno;
	}

}

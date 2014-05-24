package org.vvgaming.harmegido.vision;

import java.util.Arrays;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * Classe de utilitários para interação com Objetos da OpenCV
 * 
 * @author Vinicius Nogueira
 */
public class OCVUtil {

	private Mat empty = new Mat();

	// parâmetros para o cálculo de histograma em HSV (ignorando o V)
	private MatOfInt channels;
	private MatOfInt histSize;
	private MatOfFloat ranges;

	private OCVUtil() {
		// parâmetros para o cálculo de histograma em HSV (ignorando o V)
		channels = new MatOfInt(0, 1);
		histSize = new MatOfInt(50, 60);
		ranges = new MatOfFloat();
		ranges.put(0, 0, 0.0f);
		ranges.put(0, 1, 256.0f);
		ranges.put(1, 0, 0.0f);
		ranges.put(1, 1, 180.0f);
	}

	/**
	 * Libera as matrizes, se não nulas
	 * 
	 * @param mats
	 */
	public void releaseMat(Mat... mats) {
		for (Mat m : mats) {
			if (m != null) {
				m.release();
			}
		}
	}

	/**
	 * Converte Mat da OpenCV em Bitmap do Android
	 * 
	 * @param toConvert
	 * @return o Bitmap
	 */
	public Bitmap toBmp(Mat mat) {

		Mat toConvert = mat.clone();

		Bitmap retorno = Bitmap.createBitmap(toConvert.cols(),
				toConvert.rows(), Config.RGB_565);
		Utils.matToBitmap(toConvert, retorno);

		releaseMat(toConvert);

		return retorno;
	}

	/**
	 * Cálcula o histograma de H e S de uma imagem RGBA. Isto é, converte a
	 * imagem para HSV, ignora o V e cácula o histograma
	 * 
	 * @param mat
	 *            a imagem em RGBA
	 * @return o histograma
	 */
	public Mat calcHistHS(final Mat mat) {

		Mat retorno = new Mat();
		Mat imagem = mat.clone();

		// converte para hsv
		Imgproc.cvtColor(imagem, imagem, Imgproc.COLOR_RGBA2RGB);
		Imgproc.cvtColor(imagem, imagem, Imgproc.COLOR_RGB2HSV);

		// cálcula o histograma apenas de H e S
		Imgproc.calcHist(Arrays.asList(imagem), channels, empty, retorno,
				histSize, ranges);

		// normalizando o histograma para comparar grandezas de mesmo range
		Core.normalize(retorno, retorno, 0, 1, Core.NORM_MINMAX, -1, empty);
		releaseMat(imagem);

		return retorno;
	}

	private static OCVUtil instance;

	public static OCVUtil getInstance() {
		if (instance == null) {
			instance = new OCVUtil();
		}
		return instance;
	}

}

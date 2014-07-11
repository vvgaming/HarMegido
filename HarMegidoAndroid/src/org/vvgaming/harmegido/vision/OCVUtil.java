package org.vvgaming.harmegido.vision;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;
import org.vvgaming.harmegido.lib.model.OpenCVMatWrapper;
import org.vvgaming.harmegido.util.CompressionUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Base64;

/**
 * Classe de utilitários para interação com Objetos da OpenCV
 * 
 * @author Vinicius Nogueira
 */
public class OCVUtil {

	private final Mat empty = new Mat();

	// parâmetros para o cálculo de histograma em HSV (ignorando o V)
	private final MatOfInt channels;
	private final MatOfInt histSize;
	private final MatOfFloat ranges;

	// parâmetros para cálculo de features e descriptors
	private final FeatureDetector fd;
	private final DescriptorExtractor de;
	private final DescriptorMatcher dm;

	private OCVUtil() {
		// parâmetros para o cálculo de histograma em HSV (ignorando o V)
		channels = new MatOfInt(0, 1);
		histSize = new MatOfInt(50, 60);
		ranges = new MatOfFloat();
		ranges.put(0, 0, 0.0f);
		ranges.put(0, 1, 256.0f);
		ranges.put(1, 0, 0.0f);
		ranges.put(1, 1, 180.0f);

		// parâmetros para cálculo de features e descriptors
		fd = FeatureDetector.create(FeatureDetector.ORB);
		de = DescriptorExtractor.create(DescriptorExtractor.ORB);
		dm = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
	}

	/**
	 * Libera as matrizes, se não nulas
	 * 
	 * @param mats
	 */
	public void releaseMat(final Mat... mats) {
		for (final Mat m : mats) {
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
	public Bitmap toBmp(final Mat mat) {

		final Mat toConvert = mat.clone();

		final Bitmap retorno = Bitmap.createBitmap(toConvert.cols(),
				toConvert.rows(), Config.RGB_565);
		Utils.matToBitmap(toConvert, retorno);

		releaseMat(toConvert);

		return retorno;
	}

	/**
	 * Converte Bitmap do Android em Mat da OpenCV
	 * 
	 * @param toConvert
	 * @return a Mat
	 */
	public Mat toMat(final Bitmap toConvert) {
		final Mat retorno = new Mat();
		Utils.bitmapToMat(toConvert, retorno);
		return retorno;
	}

	/**
	 * Converte Mat da OpenCV em um array de bytes
	 * 
	 * @param mat
	 * @return
	 */
	public byte[] toByteArray(final Mat mat) {
		final byte[] retorno = new byte[(int) (mat.total() * mat.channels())];
		mat.get(0, 0, retorno);
		return retorno;
	}

	/**
	 * Converte um array de bytes em Mat da OpenCV
	 * 
	 * @param mat
	 * @return
	 */
	public Mat toMat(final byte[] array, final Size size, final int cvType) {
		final Mat retorno = new Mat(size, cvType);
		retorno.put(0, 0, array);
		return retorno;
	}

	/**
	 * Converte um {@link OpenCVMatWrapper} em Mat da OpenCV, descomprimindo o
	 * conteúdo, deixando-o pronto para uso
	 * 
	 * @param matWrapper
	 * @return
	 */
	public Mat toMat(final OpenCVMatWrapper matWrapper) {
		try {
			// tira da base64, e pegar os bytes, mas lembrando que eles estão
			// comprimidos
			final byte[] compressedBytes = Base64.decode(
					matWrapper.getContent(), Base64.DEFAULT);
			// descomprime os bytes para preparar para utilização
			byte[] uncompressedBytes = CompressionUtils
					.decompress(compressedBytes);

			// gera e retorna a Mat
			final Mat retorno = new Mat(matWrapper.getWidth(),
					matWrapper.getHeight(), matWrapper.getCvType());
			retorno.put(0, 0, uncompressedBytes);
			return retorno;
		} catch (IOException | DataFormatException e) {
			throw new IllegalStateException("Erro ao descomprimir os bytes", e);
		}
	}

	/**
	 * Converte uma Mat da OpenCV em {@link OpenCVMatWrapper}, comprimindo seu
	 * conteúdo para agilizar no transporte
	 * 
	 * @param mat
	 * @return
	 */
	public OpenCVMatWrapper toOpenCVMatWrapper(final Mat mat) {
		try {
			// recupera os bytes brutos
			final byte[] uncompressedBytes = toByteArray(mat);
			// vamos comprimir para diminuir um pouco
			byte[] compressedBytes = CompressionUtils
					.compress(uncompressedBytes);
			// agora mudar pra base64 para facilitar no transporte, na rede
			final String content = Base64.encodeToString(compressedBytes,
					Base64.DEFAULT);
			// por fim, gera o wrapper
			return OpenCVMatWrapper.from(content, mat.rows(), mat.cols(),
					mat.type());
		} catch (IOException e) {
			throw new IllegalStateException("Erro ao comprimir os bytes", e);
		}

	}

	/**
	 * Calcula o histograma de H e S de uma imagem RGBA. Isto é, converte a
	 * imagem para HSV, ignora o V e calcula o histograma
	 * 
	 * @param mat
	 *            a imagem em RGBA
	 * @return o histograma
	 */
	public Mat calcHistHS(final Mat mat) {

		final Mat retorno = new Mat();
		final Mat imagem = mat.clone();

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

	/**
	 * Detecta features e computa seus descritores
	 * 
	 * @param mat
	 *            a imagem em escala de CINZA
	 * @return os descritores
	 */
	public Mat extractFeatureDescriptors(final Mat mat) {
		final Mat retorno = new Mat();
		final MatOfKeyPoint kps = new MatOfKeyPoint();
		fd.detect(mat, kps);
		de.compute(mat, kps, retorno);
		return retorno;
	}

	/**
	 * Compara dois descritores extraidos em
	 * {@link OCVUtil#extractFeatureDescriptors(Mat)}
	 * 
	 * @param descs1
	 * @param descs2
	 * @return de 0 a 1, onde 1 é o mais "próximo"
	 */
	public float compareDescriptors(final Mat descs1, final Mat descs2) {
		try {

			final float DISTANCE_THRESHOLD = 50;

			final MatOfDMatch retorno = new MatOfDMatch();
			dm.match(descs1, descs2, retorno);

			float sum = 0;
			for (final DMatch m : retorno.toArray()) {
				if (m.distance < DISTANCE_THRESHOLD) {
					sum++;
				}
			}

			if (retorno.rows() != 0) {
				return sum / retorno.rows();
			} else {
				return 0.0f;
			}
		} catch (final CvException ignored) {
			// esse ignore na exceção eu coloquei pq alguns frames da
			// camera vem diferente e dá pau na comparação dos descritores
			// TODO verificar como resolver esse problema "de verdade"
			return 0.0f;
		}
	}

	private static OCVUtil instance;

	public static OCVUtil getInstance() {
		if (instance == null) {
			instance = new OCVUtil();
		}
		return instance;
	}

}

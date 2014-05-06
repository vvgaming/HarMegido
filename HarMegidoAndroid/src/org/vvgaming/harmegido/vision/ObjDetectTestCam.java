package org.vvgaming.harmegido.vision;

import java.util.Arrays;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ObjDetectTestCam implements CvCameraViewListener2 {

	private static final Scalar RED_SCALAR = new Scalar(255, 0, 0);
	private static final Scalar GREEN_SCALAR = new Scalar(0, 255, 0);

	// empty mask
	private Mat empty;

	// imagens
	private Mat toShow;
	private Mat marcada;
	private Mat red;
	private Mat green;
	private Mat last;

	// histogramas
	private Mat histMarcada;
	private Mat histLast;

	// parametros para c�lculos do histograma
	private MatOfInt channels;
	private MatOfInt histSize;
	private MatOfFloat ranges;

	@Override
	public void onCameraViewStarted(int width, int height) {

		empty = new Mat();

		last = new Mat();
		toShow = new Mat();

		histMarcada = new Mat();
		histLast = new Mat();

		channels = new MatOfInt(0, 1);
		histSize = new MatOfInt(50, 60);

		ranges = new MatOfFloat();
		ranges.put(0, 0, 0.0f);
		ranges.put(0, 1, 256.0f);
		ranges.put(1, 0, 0.0f);
		ranges.put(1, 1, 180.0f);

	}

	@Override
	public void onCameraViewStopped() {
		releaseMat(empty);

		releaseMat(toShow);
		releaseMat(marcada);
		releaseMat(red);
		releaseMat(green);
		releaseMat(last);

		releaseMat(histMarcada);
		releaseMat(histLast);

		releaseMat(channels);
		releaseMat(histSize);
		releaseMat(ranges);
	}

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

		releaseMat(last);

		last = inputFrame.rgba();
		toShow = last.clone();
		if (marcada != null) {

			// calculo do histograma para o frame atual
			Mat toHist = last.clone();
			Imgproc.calcHist(Arrays.asList(toHist), channels, empty, histLast,
					histSize, ranges);
			// normalizando o histograma para comparar grandezas de mesmo range
			Core.normalize(histLast, histLast, 0, 1, Core.NORM_MINMAX, -1,
					empty);
			toHist.release();

			// sobrepondo as imagens (blending)
			Core.addWeighted(marcada, 0.35d, last, 0.65d, 0.0d, toShow);

			// comparando histogramas e sobrepondo uma pel�cula verde ou
			// vermelha
			if (Imgproc.compareHist(histMarcada, histLast, 1) < 20) {
				Core.addWeighted(green, 0.35d, toShow, 0.65d, 0.0d, toShow);
			} else {
				Core.addWeighted(red, 0.35d, toShow, 0.65d, 0.0d, toShow);
			}

		}

		return toShow;
	}

	public void catchImg() {
		releaseMat(marcada);
		releaseMat(red);
		releaseMat(green);

		marcada = last.clone();
		red = last.clone();
		green = last.clone();

		red.setTo(RED_SCALAR);
		green.setTo(GREEN_SCALAR);

		// calculando o guardando o histograma da imagem que foi marcada
		Imgproc.calcHist(Arrays.asList(marcada), channels, empty, histMarcada,
				histSize, ranges);
		// normalizando o histograma para comparar grandezas de mesmo range
		Core.normalize(histMarcada, histMarcada, 0, 1, Core.NORM_MINMAX, -1,
				empty);

		// borrando um pouco a imagem para ficar "menos vis�vel"
		Imgproc.blur(marcada, marcada, new Size(5, 5));
	}

	private void releaseMat(Mat mat) {
		if (mat != null)
			mat.release();
	}

}

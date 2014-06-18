package org.vvgaming.harmegido.test.vision.features;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;
import org.vvgaming.harmegido.vision.OCVUtil;

public class ObjDetectFeatTestCam implements CvCameraViewListener2 {

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

	// descs
	private Mat lastDescs;
	private Mat marcadaDescs;

	// fd
	private FeatureDetector fd;
	private DescriptorExtractor de;
	private DescriptorMatcher dm;

	@Override
	public void onCameraViewStarted(int width, int height) {

		empty = new Mat();

		last = new Mat();
		toShow = new Mat();

		lastDescs = new Mat();

		fd = FeatureDetector.create(FeatureDetector.ORB);
		de = DescriptorExtractor.create(DescriptorExtractor.ORB);
		dm = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

	}

	@Override
	public void onCameraViewStopped() {
		final OCVUtil ocvUtil = OCVUtil.getInstance();
		ocvUtil.releaseMat(empty, toShow, marcada, red, green, last);
	}

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

		OCVUtil.getInstance().releaseMat(last, lastDescs);

		last = inputFrame.gray();
		toShow = last.clone();

		MatOfKeyPoint kps = new MatOfKeyPoint();
		fd.detect(last, kps);
		de.compute(last, kps, lastDescs);

		if (marcada != null) {
			MatOfDMatch matches = new MatOfDMatch();

			dm.match(lastDescs, matches);

			float sum = 0;
			for (DMatch m : matches.toArray()) {
				if (m.distance < 50) {
					sum++;
				}
			}

			float result = sum / (float) matches.rows();

			if (matches.rows() != 0) {
				System.out.println(result);
			} else {
				System.out.println("no");
				result = 0;
			}

			if (result > .5) {
				Core.addWeighted(green, 0.35d, toShow, 0.65d, 0.0d, toShow);
			} else {
				Core.addWeighted(red, 0.35d, toShow, 0.65d, 0.0d, toShow);
			}

		}

		return toShow;
	}

	public void catchImg() {
		OCVUtil.getInstance().releaseMat(marcada, marcadaDescs, red, green,
				lastDescs);
		//
		marcada = last.clone();
		 red = last.clone();
		 green = last.clone();
		//

		marcadaDescs = new Mat();

		MatOfKeyPoint kps = new MatOfKeyPoint();
		fd.detect(marcada, kps);
		de.compute(marcada, kps, marcadaDescs);

		dm.clear();
		List<Mat> list = new ArrayList<>();
		list.add(marcadaDescs);
		dm.add(list);

		 red.setTo(RED_SCALAR);
		 green.setTo(GREEN_SCALAR);
		//
		// Mat marcadaCalc = marcada.clone();
		// // converte para hsv
		// Imgproc.cvtColor(marcadaCalc, marcadaCalc, Imgproc.COLOR_RGBA2RGB);
		// Imgproc.cvtColor(marcadaCalc, marcadaCalc, Imgproc.COLOR_RGB2HSV);
		//
		// // calculando o guardando o histograma da imagem que foi marcada
		// Imgproc.calcHist(Arrays.asList(marcadaCalc), channels, empty,
		// histMarcada, histSize, ranges);
		// marcadaCalc.release();
		//
		// // normalizando o histograma para comparar grandezas de mesmo range
		// Core.normalize(histMarcada, histMarcada, 0, 1, Core.NORM_MINMAX, -1,
		// empty);
		//
		// // borrando um pouco a imagem para ficar "menos visível"
		// Imgproc.blur(marcada, marcada, new Size(5, 5));
	}

}

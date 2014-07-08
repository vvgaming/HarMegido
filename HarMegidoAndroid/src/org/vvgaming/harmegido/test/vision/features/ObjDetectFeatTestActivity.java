package org.vvgaming.harmegido.test.vision.features;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.util.Constantes;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

public class ObjDetectFeatTestActivity extends Activity implements OnClickListener
{

	private CameraBridgeViewBase mOpenCvCameraView;
	private ObjDetectFeatTestCam cam = new ObjDetectFeatTestCam();

	@Override
	public void onClick(View v)
	{
		cam.catchImg();
	}

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this)
	{
		@Override
		public void onManagerConnected(int status)
		{
			switch (status)
			{
			case LoaderCallbackInterface.SUCCESS:
			{
				mOpenCvCameraView.enableView();
			}
				break;
			default:
			{
				super.onManagerConnected(status);
			}
				break;
			}
		}

	};

	public ObjDetectFeatTestActivity()
	{
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.obj_detect_test_view);

		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.java_camera);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setMaxFrameSize((int) Constantes.camNormalSize.width, (int) Constantes.camNormalSize.height);
		mOpenCvCameraView.setOnClickListener(this);
		mOpenCvCameraView.setCvCameraViewListener(cam);

	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	}

	public void onDestroy()
	{
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	public void onCameraViewStarted(int width, int height)
	{
	}

	public void onCameraViewStopped()
	{
	}

}

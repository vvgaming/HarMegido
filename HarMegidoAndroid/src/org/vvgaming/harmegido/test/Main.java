package org.vvgaming.harmegido.test;

import org.unbiquitous.uos.core.ClassLoaderUtils;
import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.test.bubbleTest.BubbleTestActivity;
import org.vvgaming.harmegido.test.vision.ObjDetectTestActivity;
import org.vvgaming.harmegido.test.vision.features.ObjDetectFeatTestActivity;
import org.vvgaming.harmegido.theGame.HarMegidoActivity;
import org.vvgaming.harmegido.uos.UOSFacade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);

		ClassLoaderUtils.builder = new ClassLoaderUtils.DefaultClassLoaderBuilder() {
			public ClassLoader getParentClassLoader() {
				return getClassLoader();
			};
		};

		((Button) findViewById(R.id.btnHarMegido))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(),
								HarMegidoActivity.class);
						startActivity(i);
					}
				});

		((Button) findViewById(R.id.btnBubblesTest))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(),
								BubbleTestActivity.class);
						startActivity(i);
					}
				});

		((Button) findViewById(R.id.btnCameraTest))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(),
								ObjDetectTestActivity.class);
						startActivity(i);
					}
				});

		((Button) findViewById(R.id.btnCameraTest2))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(),
								ObjDetectFeatTestActivity.class);
						startActivity(i);
					}
				});

		((Button) findViewById(R.id.btnMyTest))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getApplicationContext(),
								MainGame.class);
						startActivity(i);
					}
				});

		UOSFacade.startUos();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UOSFacade.stopUos();
	}

}

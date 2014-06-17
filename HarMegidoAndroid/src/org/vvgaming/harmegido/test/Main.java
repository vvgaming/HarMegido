package org.vvgaming.harmegido.test;

import java.util.List;

import org.unbiquitous.uos.core.ClassLoaderUtils;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.test.bubbleTest.BubbleTestActivity;
import org.vvgaming.harmegido.test.vision.ObjDetectTestActivity;
import org.vvgaming.harmegido.theGame.HarMegidoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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

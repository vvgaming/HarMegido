package org.vvgaming.harmegido.test;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.bubbleTest.BubbleTestActivity;
import org.vvgaming.harmegido.theGame.HarMegidoActivity;
import org.vvgaming.harmegido.vision.test.ObjDetectTestActivity;

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

	}

}

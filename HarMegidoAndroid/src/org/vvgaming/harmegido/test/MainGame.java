package org.vvgaming.harmegido.test;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.test.bubbleTest.BubbleTestActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainGame extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.maingame);

		((TextView) findViewById(R.id.lightchoice))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) 
					{
						v.getRootView().setBackgroundColor(Color.WHITE);
						((TextView) findViewById(R.id.lightchoice)).setVisibility(0);
						((TextView) findViewById(R.id.darkchoice)).setVisibility(1);
					}
				});

		((TextView) findViewById(R.id.darkchoice))
		.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				v.getRootView().setBackgroundColor(Color.BLACK);
				((TextView) findViewById(R.id.darkchoice)).setVisibility(0);
				((TextView) findViewById(R.id.lightchoice)).setVisibility(1);
			}
		});
		
		((Button) findViewById(R.id.btnNextPage))
		.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(getApplicationContext(),
						CharSelection.class);
				startActivity(i);
			}
		});
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}

package org.vvgaming.harmegido.test;

import org.vvgaming.harmegido.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CharSelection extends Activity 
{
	private int currentChar = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.charselection);
		
		final ImageView charPicture = (ImageView)findViewById(R.id.char_picture);
		charPicture.setImageResource(R.drawable.kayle);
		
		View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				if (currentChar == 0)
				{
					currentChar = 1;
					charPicture.setImageResource(R.drawable.morgana);
					
					 MediaPlayer mp = MediaPlayer.create(CharSelection.this, R.raw.morgana);
					 
		            mp.setOnCompletionListener(new OnCompletionListener() {

		            @Override
		            public void onCompletion(MediaPlayer mp) {
		                // TODO Auto-generated method stub
		                mp.release();
		            }

		            });   
		            mp.start();
				}
				else
				{
					currentChar = 0;
					charPicture.setImageResource(R.drawable.kayle);
					
					MediaPlayer mp = MediaPlayer.create(CharSelection.this, R.raw.kayle);
					 
		            mp.setOnCompletionListener(new OnCompletionListener() {

		            @Override
		            public void onCompletion(MediaPlayer mp) {
		                // TODO Auto-generated method stub
		                mp.release();
		            }

		            });   
		            mp.start();
				}
			}
		};
		((Button) findViewById(R.id.btn_prevChar))
				.setOnClickListener(onClickListener);
		
		((Button) findViewById(R.id.btnNextChar))
				.setOnClickListener(onClickListener);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}

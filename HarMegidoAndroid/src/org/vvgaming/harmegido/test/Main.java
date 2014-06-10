package org.vvgaming.harmegido.test;

import java.util.ListResourceBundle;

import org.unbiquitous.uos.core.ClassLoaderUtils;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.network.socket.connectionManager.TCPConnectionManager;
import org.unbiquitous.uos.network.socket.radar.MulticastRadar;
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
	
	private UOS uos = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);
		
		ClassLoaderUtils.builder = new ClassLoaderUtils.DefaultClassLoaderBuilder(){
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

		startUos();
	}
	
	private void startUos() {
	    if (uos != null)
	      return;
	    uos = new UOS();
	    new AsyncTask<Void, Void, Void> () {
	      protected Void doInBackground(Void... params) {
	        uos.start(new ListResourceBundle() {
	          protected Object[][] getContents() {
	            return new Object[][] {
	              {"ubiquitos.connectionManager", TCPConnectionManager.class.getName()},
	              {"ubiquitos.radar", MulticastRadar.class.getName()},
	              {"ubiquitos.eth.tcp.port", "14984"},
	              {"ubiquitos.eth.tcp.passivePortRange", "14985-15000"},
	              {"ubiquitos.eth.udp.port", "15001"},
	              {"ubiquitos.eth.udp.passivePortRange", "15002-15017"},
	            };
	          }
	        });
	        return null;
	      }
	    }.execute();
	  }
	  
	  private void stopUos() {
	    if (uos == null)
	      return;
	    uos.stop();
	    uos = null;
	  }
	  
	  @Override
	protected void onDestroy() {
		super.onDestroy();
		stopUos();
	}

}

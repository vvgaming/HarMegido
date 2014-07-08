package org.vvgaming.harmegido.gameEngine.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Vibrator;

/**
 * Gerenciador de recursos que d� acesso aos nativos do Android e mantem alguns
 * em seu dom�nio para evitar duplicidade em mem�ria
 * 
 * @author Vinicius Nogueira
 */
public class AssetManager {

	private final Activity act;
	private final Map<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();
	private final Map<Integer, MediaPlayer> medias = new HashMap<Integer, MediaPlayer>();

	public AssetManager(final Activity act) {
		this.act = act;
	}

	public Bitmap getBitmap(final int bmpId) {
		Bitmap retorno;
		if ((retorno = bitmaps.get(bmpId)) == null) {
			bitmaps.put(
					bmpId,
					retorno = BitmapFactory.decodeResource(act.getResources(),
							bmpId));
		}
		return retorno;
	}

	public void release() {
		for (final Entry<Integer, Bitmap> entry : bitmaps.entrySet()) {
			entry.getValue().recycle();
		}
		bitmaps.clear();
		for (final Entry<Integer, MediaPlayer> entry : medias.entrySet()) {
			entry.getValue().release();
		}
		medias.clear();
	}

	public void playSound(final int soundId) {
		MediaPlayer mp;
		if ((mp = medias.get(soundId)) == null) {
			medias.put(soundId, mp = MediaPlayer.create(getActivity(), soundId));
		}
		mp.start();
	}

	public void vibrate(final long timer) {
		Vibrator vb = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
		vb.vibrate(timer);
	}

	public String getRawTextFile(final int rawFileId) {
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;

		try {
			is = act.getResources().openRawResource(rawFileId);
			br = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} catch (Exception ignored) {
			return "";
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException ignored) {
				}
			}

		}

	}

	public android.content.res.AssetManager getAndroidAssets() {
		return act.getAssets();
	}

	public Activity getActivity() {
		return act;
	}

}

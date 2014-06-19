package org.vvgaming.harmegido.gameEngine.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

/**
 * Gerenciador de recursos que d� acesso aos nativos do Android e mantem alguns
 * em seu dom�nio para evitar duplicidade em mem�ria
 * 
 * @author Vinicius Nogueira
 */
public class AssetManager {

	private final Activity act;
	private final Map<Integer, Bitmap> bitmaps = new HashMap<>();
	private final Map<Integer, MediaPlayer> medias = new HashMap<>();

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
		for (Entry<Integer, Bitmap> entry : bitmaps.entrySet()) {
			entry.getValue().recycle();
		}
		bitmaps.clear();
		for (Entry<Integer, MediaPlayer> entry : medias.entrySet()) {
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

	public android.content.res.AssetManager getAndroidAssets() {
		return act.getAssets();
	}

	public Activity getActivity() {
		return act;
	}

}

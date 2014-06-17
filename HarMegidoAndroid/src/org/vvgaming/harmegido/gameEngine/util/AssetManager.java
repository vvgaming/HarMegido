package org.vvgaming.harmegido.gameEngine.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AssetManager {

	private final Activity act;
	private final Map<Integer, Bitmap> bitmaps = new HashMap<>();

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
	}

	public android.content.res.AssetManager getAndroidAssets() {
		return act.getAssets();
	}

	public Activity getActivity() {
		return act;
	}

}

package org.vvgaming.harmegido.gameEngine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.graphics.Canvas;
import android.view.MotionEvent;

public abstract class AbstractGame {

	private int width = 0;
	private int height = 0;
	private Map<Integer, List<GameObject>> objectsPerLayer = new TreeMap<>();

	public final void realUpdate(long delta) {
		for (Entry<Integer, List<GameObject>> entry : objectsPerLayer
				.entrySet()) {
			// criando uma nova para evitar problemas de acesso concorrente
			List<GameObject> curLayerList = new ArrayList<>(entry.getValue());
			for (Iterator<GameObject> iterator = curLayerList.iterator(); iterator
					.hasNext();) {
				GameObject go = (GameObject) iterator.next();
				if (go.isDead()) {
					iterator.remove();
				}
				go.update(delta);
			}
		}
		update(delta);
	}

	public final void realRender(Canvas canvas) {

		for (Entry<Integer, List<GameObject>> entry : objectsPerLayer
				.entrySet()) {
			// criando uma nova para evitar problemas de acesso concorrente
			List<GameObject> curLayerList = new ArrayList<>(entry.getValue());
			for (GameObject go : curLayerList) {
				go.render(canvas);
			}
		}
	}

	abstract public void update(long delta);

	abstract public void onTouch(MotionEvent event);

	public boolean addObject(GameObject object) {
		return addObject(object, 0);
	}

	public boolean addObject(GameObject object, int layerIndex) {
		List<GameObject> layer = getLayer(layerIndex);
		return layer.add(object);
	}

	private List<GameObject> getLayer(int layerIndex) {
		List<GameObject> retorno = objectsPerLayer.get(layerIndex);
		if (retorno == null) {
			retorno = new ArrayList<GameObject>();
			objectsPerLayer.put(layerIndex, retorno);
		}
		return retorno;
	}

	public void clearObjects() {
		for (Entry<Integer, List<GameObject>> entry : objectsPerLayer
				.entrySet()) {
			entry.getValue().clear();
		}
	}

	public boolean removeObject(GameObject object, int layerIndex) {
		return getLayer(layerIndex).remove(object);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;

	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}

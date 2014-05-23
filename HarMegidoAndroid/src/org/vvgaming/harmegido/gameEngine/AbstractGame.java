package org.vvgaming.harmegido.gameEngine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Implementação default de um Jogo (ou parte de um). Quando for implementar um
 * Jogo para ser rodado pelo {@link GameCanvas}, deve-se estender essa classe.
 * 
 * @author Vinicius Nogueira
 */
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
		// invoca o update do game
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

	/**
	 * Método padrão que deve ser implementado pelos filhos de
	 * {@link AbstractGame}. Este método é invocado a cada frame e dá a
	 * possibilidade de atualizar as informações necessárias do jogo.
	 * 
	 * @param delta
	 *            é a variação de tempo (em ms) do último frame, particularmente
	 *            útil quando se deseja fazer atualizações que dependam de tempo
	 */
	abstract public void update(long delta);

	abstract public void onTouch(MotionEvent event);

	/**
	 * Adiciona um objeto na camada 0. Veja {@link #addObject(GameObject, int)}
	 * para mais detalhes
	 * 
	 * @param object
	 * @return
	 */
	public boolean addObject(GameObject object) {
		return addObject(object, 0);
	}

	/**
	 * Adiciona um objeto da lista de objetos controlados por este jogo. Os
	 * objetos desta lista são gerenciados automaticamente sendo atualizados
	 * {@link GameObject#update(long)} a cada frame e renderizados
	 * {@link GameObject#render(Canvas)} também.
	 * 
	 * @param object
	 *            o objeto a ser adicionado
	 * @param layerIndex
	 *            o índice da camada de renderização (serve para sobrepor
	 *            objetos)
	 * @return retorna <code>true</code> se conseguiu adicionar
	 */
	public boolean addObject(GameObject object, int layerIndex) {
		List<GameObject> layer = getLayer(layerIndex);
		return layer.add(object);
	}

	/**
	 * Limpa todos os objetos que estão sendo controlado por este game. Veja
	 * {@link #addObject(GameObject, int)} para mais detalhes.
	 */
	public void clearObjects() {
		for (Entry<Integer, List<GameObject>> entry : objectsPerLayer
				.entrySet()) {
			entry.getValue().clear();
		}
	}

	/**
	 * Remove um objeto de uma camada. Veja {@link #addObject(GameObject, int)}
	 * para mais detalhes.
	 * 
	 * @param object
	 *            o objeto a ser removido
	 * @param layerIndex
	 * @return <code>true</code> se removeu
	 */
	public boolean removeObject(GameObject object, int layerIndex) {
		return getLayer(layerIndex).remove(object);
	}

	private List<GameObject> getLayer(int layerIndex) {
		List<GameObject> retorno = objectsPerLayer.get(layerIndex);
		if (retorno == null) {
			retorno = new ArrayList<GameObject>();
			objectsPerLayer.put(layerIndex, retorno);
		}
		return retorno;
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

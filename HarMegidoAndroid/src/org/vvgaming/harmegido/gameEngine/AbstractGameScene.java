package org.vvgaming.harmegido.gameEngine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.vvgaming.harmegido.gameEngine.util.AssetManager;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Implementa��o default de um Jogo (ou parte de um). Quando for implementar um
 * Jogo para ser rodado pelo {@link GameCanvas}, deve-se estender essa classe.
 * 
 * @author Vinicius Nogueira
 */
public abstract class AbstractGameScene {

	private final AssetManager assetManager;
	private int width = 0;
	private int height = 0;
	private Map<Integer, List<GameObject>> objectsPerLayer = new TreeMap<>();

	// indicador se essa cena foi inicializada
	private boolean inicializada = false;

	public AbstractGameScene(final Activity act) {
		super();
		assetManager = new AssetManager(act);
	}

	protected final void realInit() {
		inicializada = true;
		init();
	}

	protected final void realEnd() {
		clearObjects();
		assetManager.release();
		end();
	}

	public void init() {

	}

	public void end() {

	}

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
				if (go.isVisible()) {
					go.render(canvas);
				}
			}
		}
	}

	/**
	 * M�todo padr�o que deve ser implementado pelos filhos de
	 * {@link AbstractGameScene}. Este m�todo � invocado a cada frame e d� a
	 * possibilidade de atualizar as informa��es necess�rias do jogo.
	 * 
	 * @param delta
	 *            � a varia��o de tempo (em ms) do �ltimo frame, particularmente
	 *            �til quando se deseja fazer atualiza��es que dependam de tempo
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
	 * objetos desta lista s�o gerenciados automaticamente sendo atualizados
	 * {@link GameObject#update(long)} a cada frame e renderizados
	 * {@link GameObject#render(Canvas)} tamb�m.
	 * 
	 * @param object
	 *            o objeto a ser adicionado
	 * @param layerIndex
	 *            o �ndice da camada de renderiza��o (serve para sobrepor
	 *            objetos)
	 * @return retorna <code>true</code> se conseguiu adicionar
	 */
	public boolean addObject(final GameObject object, int layerIndex) {

		// TODO esse proxy � uma maneira de evitar aqueles INITS e ENDS
		// explicitos no objeto, para fazer um controle de estados autom�ticos.
		// vou reavaliar isso aqui depois para ver se vale a pena mudar e
		// implementar dessa maneira

		// GameObject realObject = (GameObject) Proxy.newProxyInstance(object
		// .getClass().getClassLoader(), new Class[] { GameObject.class },
		// new GameObjectProxy(object));

		if (!inicializada) {
			throw new IllegalStateException(
					"Essa cena não foi inicializada e portanto não pode receber objetos. "
							+ "Dica: nunca de addObject em construtores, para isso use o método init");
		}
		object.init();
		List<GameObject> layer = getLayer(layerIndex);
		return layer.add(object);
	}

	/**
	 * Limpa todos os objetos que est�o sendo controlado por este game. Veja
	 * {@link #addObject(GameObject, int)} para mais detalhes.
	 */
	public void clearObjects() {
		for (Entry<Integer, List<GameObject>> entry : objectsPerLayer
				.entrySet()) {
			for (GameObject go : entry.getValue()) {
				go.end();
			}
			entry.getValue().clear();
		}
	}

	// TODO esse proxy abaixo � uma maneira de evitar aqueles INITS e ENDS
	// explicitos no objeto, para fazer um controle de estados autom�ticos.
	// vou reavaliar isso aqui depois para ver se vale a pena mudar e
	// implementar dessa maneira

	// private class GameObjectProxy implements InvocationHandler {
	//
	// private GameObject delegate;
	//
	// public GameObjectProxy(GameObject delegate) {
	// this.delegate = delegate;
	// }
	//
	// @Override
	// public Object invoke(Object proxy, Method method, Object[] args)
	// throws Throwable {
	// System.out.println("Inside the invocation handler");
	// try {
	// return method.invoke(delegate, args);
	// } catch (InvocationTargetException e) {
	// throw e.getCause();
	// }
	// }
	// }

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
		object.end();
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

	public AssetManager getAssetManager() {
		return assetManager;
	}

}

package org.vvgaming.harmegido.gameEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.vvgaming.harmegido.gameEngine.util.AssetManager;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Implementação de um nó do jogo. Os objetos do jogo se comportam como uma
 * árvore, onde cada nó tem vários nós abaixo
 * 
 * @author Vinicius Nogueira
 */
public abstract class GameNode {

	private Map<Integer, List<GameNode>> nodesPerLayer = new TreeMap<>();

	// isso é uma morte forçada
	private boolean murdered = false;

	// indicador se esse nó foi inicializado
	private boolean inicializado = false;

	public GameNode() {
		super();
	}

	protected final void realInit() {
		inicializado = true;
		init();
	}

	protected final void realEnd() {
		end();
	}

	public void kill() {
		murdered = true;
		for (Entry<Integer, List<GameNode>> entry : nodesPerLayer.entrySet()) {
			for (GameNode node : entry.getValue()) {
				node.kill();
			}
		}
	}

	public void init() {

	}

	public void end() {

	}

	protected final void realUpdate(final long delta) {
		// invoca o update do nó
		update(delta);

		for (Entry<Integer, List<GameNode>> entry : nodesPerLayer.entrySet()) {
			// criando uma nova para evitar problemas de acesso concorrente
			// List<GameNode> curLayerList = new ArrayList<>(entry.getValue());
			for (Iterator<GameNode> iterator = entry.getValue().iterator(); iterator
					.hasNext();) {
				GameNode node = (GameNode) iterator.next();
				node.realUpdate(delta);
				if (node.murdered || node.isDead()) {
					if (node.isDead()) {
						// só morreu normal, entao mata os filhos
						node.kill();
					}
					node.realEnd();
					iterator.remove();
					continue;
				}
			}
		}
	}

	protected final void realRender(final Canvas canvas) {

		// invoca o render do nó
		render(canvas);

		for (Entry<Integer, List<GameNode>> entry : nodesPerLayer.entrySet()) {
			// criando uma nova para evitar problemas de acesso concorrente
			List<GameNode> curLayerList = new ArrayList<>(entry.getValue());
			for (GameNode node : curLayerList) {
				if (node.isVisible()) {
					node.realRender(canvas);
				}
			}
		}

	}

	/**
	 * Método padrão que deve ser implementado pelos filhos de {@link GameNode}.
	 * É invocado a cada frame para desenhar (renderizar) o objeto na tela. Deve
	 * se valer do canvas do parâmetro para realizar o desenho
	 * 
	 * @param canvas
	 */
	protected void render(final Canvas canvas) {

	}

	/**
	 * Método padrão que deve ser implementado pelos filhos de {@link GameNode}.
	 * Este método invocado a cada frame e dá a possibilidade de atualizar as
	 * informaçõees necessárias do jogo.
	 * 
	 * @param delta
	 *            é variação de tempo (em ms) do último frame, particularmente
	 *            útil quando se deseja fazer atualizações que dependam de tempo
	 */
	abstract protected void update(long delta);

	protected final boolean onRealTouch(MotionEvent event) {

		ArrayList<Entry<Integer, List<GameNode>>> entries = new ArrayList<>(
				nodesPerLayer.entrySet());
		Collections.reverse(entries);

		for (Entry<Integer, List<GameNode>> entry : entries) {
			// criando uma nova para evitar problemas de acesso concorrente
			List<GameNode> curLayerList = new ArrayList<>(entry.getValue());
			for (GameNode node : curLayerList) {
				if (node.onRealTouch(event))
					return true;
			}
		}

		return onTouch(event);

	}

	protected boolean onTouch(final MotionEvent event) {
		return false;
	}

	/**
	 * Adiciona um nó na camada 0. Veja {@link #addSubNode(GameNode, int)} para
	 * mais detalhes
	 * 
	 * @param node
	 * @return
	 */
	protected boolean addSubNode(GameNode node) {
		return addSubNode(node, 0);
	}

	/**
	 * Adiciona um nó da lista de objetos controlados por este nó (um sub-nó).
	 * Os nós desta lista são gerenciados automaticamente sendo atualizados
	 * {@link GameNode#update(long)} a cada frame e renderizados
	 * {@link GameNode#render(Canvas)} também.
	 * 
	 * @param node
	 *            o nó a ser adicionado
	 * @param layerIndex
	 *            o índice da camada de renderização (serve para sobrepor nós)
	 * @return retorna <code>true</code> se conseguiu adicionar
	 */
	protected boolean addSubNode(final GameNode node, int layerIndex) {

		if (!inicializado) {
			throw new IllegalStateException(
					"Este nó não foi inicializado e portanto não pode receber sub nós. "
							+ "Dica: nunca de addSubNode em construtores, para isso use o método init");
		}
		node.realInit();
		List<GameNode> layer = getLayer(layerIndex);
		return layer.add(node);
	}

	private List<GameNode> getLayer(int layerIndex) {
		List<GameNode> retorno = nodesPerLayer.get(layerIndex);
		if (retorno == null) {
			retorno = new ArrayList<GameNode>();
			nodesPerLayer.put(layerIndex, retorno);
		}
		return retorno;
	}

	public boolean isDead() {
		return false;
	}

	public boolean isVisible() {
		return true;
	}

	protected final int getGameWidth() {
		return RootNode.getInstance().getWidth();
	}

	protected final float getGameWidth(float percentage) {
		return getGameWidth() * percentage;
	}

	protected final int getGameHeight() {
		return RootNode.getInstance().getHeight();
	}

	protected final float getGameHeight(float percentage) {
		return getGameHeight() * percentage;
	}

	protected final AssetManager getGameAssetManager() {
		return RootNode.getInstance().getAssetManager();
	}

}

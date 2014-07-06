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

import com.github.detentor.codex.monads.Option;
import com.github.detentor.codex.product.Tuple2;

/**
 * Implementação de um nó do jogo. Os objetos do jogo se comportam como uma árvore, onde cada nó tem vários nós abaixo
 * 
 * @author Vinicius Nogueira
 */
public abstract class GameNode
{

	// sub-nós, por camadas
	private final Map<Integer, List<GameNode>> nodesPerLayer = new TreeMap<Integer, List<GameNode>>();

	// novos nós que devem ser adicionados no início da passada
	private final List<Tuple2<GameNode, Integer>> newNodes = new ArrayList<>();

	// isso é uma morte forçada
	private boolean murdered = false;

	// indicador se esse nó foi inicializado
	private boolean inicializado = false;

	protected final void realInit()
	{
		inicializado = true;
		init();
	}

	protected final void realEnd()
	{
		end();
	}

	public void kill()
	{
		murdered = true;
		for (final Entry<Integer, List<GameNode>> entry : nodesPerLayer.entrySet())
		{
			for (final GameNode node : entry.getValue())
			{
				node.kill();
				node.realEnd();
			}
		}
	}

	protected void init()
	{

	}

	protected void end()
	{

	}

	protected final void realUpdate(final long delta)
	{
		// adiciona os novos nós
		for (final Tuple2<GameNode, Integer> newNode : newNodes)
		{
			realAddSubNode(newNode.getVal1(), Option.from(newNode.getVal2()).getOrElse(0));
		}
		newNodes.clear();

		// invoca o update do nó
		update(delta);

		// percorre as camadas para atualizar cada nó
		for (final Entry<Integer, List<GameNode>> entry : nodesPerLayer.entrySet())
		{
			// cada nó da camada
			for (final Iterator<GameNode> iterator = entry.getValue().iterator(); iterator.hasNext();)
			{
				final GameNode node = iterator.next();
				if (node.murdered || node.isDead())
				{
					if (node.isDead() && !node.murdered)
					{
						// só morreu normal, entao mata os filhos
						node.kill();
						node.realEnd();
					}
					iterator.remove();
					continue;
				}
				node.realUpdate(delta);
			}
		}
	}

	protected final void realRender(final Canvas canvas)
	{

		// invoca o render do nó
		render(canvas);

		for (final Entry<Integer, List<GameNode>> entry : nodesPerLayer.entrySet())
		{
			// criando uma nova para evitar problemas de acesso concorrente
			final List<GameNode> curLayerList = new ArrayList<GameNode>(entry.getValue());
			for (final GameNode node : curLayerList)
			{
				if (node.isVisible())
				{
					node.realRender(canvas);
				}
			}
		}

	}

	/**
	 * Método padrão que deve ser implementado pelos filhos de {@link GameNode}. É invocado a cada frame para desenhar (renderizar) o objeto
	 * na tela. Deve se valer do canvas do parâmetro para realizar o desenho
	 * 
	 * @param canvas
	 */
	protected void render(final Canvas canvas)
	{

	}

	/**
	 * Método padrão que deve ser implementado pelos filhos de {@link GameNode}. Este método invocado a cada frame e dá a possibilidade de
	 * atualizar as informaçõees necessárias do jogo.
	 * 
	 * @param delta é variação de tempo (em ms) do último frame, particularmente útil quando se deseja fazer atualizações que dependam de
	 *            tempo
	 */
	abstract protected void update(long delta);

	protected final boolean onRealTouch(final MotionEvent event)
	{

		final ArrayList<Entry<Integer, List<GameNode>>> entries = new ArrayList<Entry<Integer, List<GameNode>>>(nodesPerLayer.entrySet());
		Collections.reverse(entries);

		for (final Entry<Integer, List<GameNode>> entry : entries)
		{
			// criando uma nova para evitar problemas de acesso concorrente
			final List<GameNode> curLayerList = new ArrayList<GameNode>(entry.getValue());
			for (final GameNode node : curLayerList)
			{
				if (node.onRealTouch(event))
				{
					return true;
				}
			}
		}

		return onTouch(event);

	}

	protected boolean onTouch(final MotionEvent event)
	{
		return false;
	}

	/**
	 * Adiciona um nó na camada 0. Veja {@link #addSubNode(GameNode, int)} para mais detalhes
	 * 
	 * @param node
	 * @return
	 */
	protected boolean addSubNode(final GameNode node)
	{
		return addSubNode(node, 0);
	}

	/**
	 * Adiciona um nó da lista de objetos controlados por este nó (um sub-nó). Os nós desta lista são gerenciados automaticamente sendo
	 * atualizados {@link GameNode#update(long)} a cada frame e renderizados {@link GameNode#render(Canvas)} também. <br/>
	 * Na verdade esse método não adiciona na hora. Ele marca para adicionar o nó, no início da próxima passada.
	 * 
	 * @param node o nó a ser adicionado
	 * @param layerIndex o índice da camada de renderização (serve para sobrepor nós)
	 * @return retorna <code>true</code> se conseguiu adicionar
	 */
	protected boolean addSubNode(final GameNode node, final int layerIndex)
	{
		return newNodes.add(Tuple2.from(node, layerIndex));
	}

	/**
	 * Método interno que faz o que o {@link GameNode#addSubNode(GameNode, int)} não faz. <br/>
	 * Esse aqui adiciona de verdade, é usado internamente para efetivar a adição
	 * 
	 * @param node o nó a ser adicionado
	 * @param layerIndex o índice da camada de renderização (serve para sobrepor nós)
	 * @return retorna <code>true</code> se conseguiu adicionar
	 */
	private boolean realAddSubNode(final GameNode node, final int layerIndex)
	{
		if (!inicializado)
		{
			throw new IllegalStateException("Este nó não foi inicializado e portanto não pode receber sub nós. "
					+ "Dica: nunca use addSubNode em construtores, para isso use o método init");
		}
		node.realInit();
		final List<GameNode> layer = getLayer(layerIndex);
		return layer.add(node);
	}

	/**
	 * Pega lista de nós de uma camada
	 * 
	 * @param layerIndex
	 * @return
	 */
	private List<GameNode> getLayer(final int layerIndex)
	{
		List<GameNode> retorno = nodesPerLayer.get(layerIndex);
		if (retorno == null)
		{
			retorno = new ArrayList<GameNode>();
			nodesPerLayer.put(layerIndex, retorno);
		}
		return retorno;
	}

	public boolean isDead()
	{
		return murdered;
	}

	public boolean isVisible()
	{
		return true;
	}

	public void setVisible(final boolean visible)
	{
		// implementação vazia para ser sobrecarregada, opcionalmente pelos
		// filhos de GameNode
	}

	protected final int getGameWidth()
	{
		return RootNode.getInstance().getWidth();
	}

	protected final float getGameWidth(final float percentage)
	{
		return getGameWidth() * percentage;
	}

	protected final int getGameHeight()
	{
		return RootNode.getInstance().getHeight();
	}

	protected final float getGameHeight(final float percentage)
	{
		return getGameHeight() * percentage;
	}

	protected final int getSmallFontSize()
	{
		// tamanho mágico
		// TODO fazer isso de outro jeito depois
		return adaptedFontSize(60);
	}

	protected final int getBigFontSize()
	{
		// tamanho mágico
		// TODO fazer isso de outro jeito depois
		return adaptedFontSize(140);
	}

	protected final int getMidFontSize()
	{
		// tamanho mágico
		// TODO fazer isso de outro jeito depois
		return adaptedFontSize(90);
	}

	protected final int adaptedFontSize(final int size)
	{
		// alguns números mágicos para gerar uma fonte de tamanho adaptado
		// TODO fazer isso de outro jeito depois
		return (int) ((float) size * getGameWidth() / 720.0f);
	}

	protected final AssetManager getGameAssetManager()
	{
		return RootNode.getInstance().getAssetManager();
	}

}

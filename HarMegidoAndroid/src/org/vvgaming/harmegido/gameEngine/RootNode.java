package org.vvgaming.harmegido.gameEngine;

import org.vvgaming.harmegido.gameEngine.util.AssetManager;

import android.app.Activity;

/**
 * Uma implementação de um {@link GameNode} raiz, que inicia o jogo, e tem
 * algumas particularidades
 * 
 * @author Vinicius Nogueira
 */
public class RootNode extends GameNode {

	private final AssetManager assetManager;
	private GameNode mainNode;
	private int width = 0;
	private int height = 0;

	private boolean visible = true;

	private RootNode(final Activity act, final GameNode mainNode) {
		if (act == null || mainNode == null) {
			throw new IllegalArgumentException(
					"activity ou mainNode nulos, isso não é permitido...");
		}
		assetManager = new AssetManager(act);
		this.mainNode = mainNode;
	}

	@Override
	public void init() {
		super.init();
		addSubNode(mainNode);
	}

	public void changeMainNode(final GameNode newMainNode) {
		if (newMainNode == null) {
			throw new IllegalArgumentException(
					"mainNode nulo? isso não é permitido...");
		}
		mainNode.kill();
		mainNode = newMainNode;
		addSubNode(mainNode);
	}

	@Override
	public void update(long delta) {
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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

	private static RootNode instance;

	public static RootNode getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					"RootNode não inicializado ainda. Utilize o método create().");
		}
		return instance;
	}

	public static RootNode create(final Activity act, final GameNode mainNode) {
		if (instance == null) {
			instance = new RootNode(act, mainNode);
		}
		return instance;
	}

}

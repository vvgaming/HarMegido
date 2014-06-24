package org.vvgaming.harmegido.theGame.objNodes;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.fx.NPaintFade;
import org.vvgaming.harmegido.gameEngine.nodes.util.NTimer;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.product.Tuple3;

/**
 * Implementação de um background padrão para o jogo Harmegido, com nuvens se
 * movendo, mudança de cores e etc
 * 
 * @author Vinicius Nogueira
 */
public class NHMBackground extends GameNode {

	private NSimpleBox box;
	private NImage fundo;
	private NImage asaAnjo;
	private NImage asaAnjoOposta;
	private NImage asaDemonio;
	private NImage asaDemonioOposta;
	private int movingSignal = 1;

	private float COLOR_CHANGE_SPEED = 255.0f / 2000;

	private Tuple3<Integer, Integer, Integer> destinyColor = Tuple3.from(100,
			100, 100);

	@Override
	protected void init() {
		super.init();

		box = new NSimpleBox(0, 0, getGameWidth(), getGameHeight(), 0, 0, 0);
		fundo = new NImage(new Ponto(getGameWidth(.5f), getGameHeight(.5f)),
				getGameAssetManager().getBitmap(R.drawable.fundo));
		fundo.setHeightKeepingRatio(getGameHeight());

		asaAnjo = new NImage(
				new Ponto(getGameWidth(.86f), getGameHeight(.25f)),
				getGameAssetManager().getBitmap(R.drawable.asa_anjo));
		asaAnjo.setHeightKeepingRatio(getGameHeight(.55f));

		asaAnjoOposta = new NImage(new Ponto(getGameWidth(.23f),
				getGameHeight(.25f)), getGameAssetManager().getBitmap(
				R.drawable.asa_anjo));
		asaAnjoOposta.setHeightKeepingRatio(getGameHeight(.55f));
		asaAnjoOposta.sethFlip(true);
		asaAnjoOposta.getPaint().setAlpha(0);

		asaDemonio = new NImage(new Ponto(getGameWidth(.17f),
				getGameHeight(.25f)), getGameAssetManager().getBitmap(
				R.drawable.asa_demonio));
		asaDemonio.setHeightKeepingRatio(getGameHeight(.55f));

		asaDemonioOposta = new NImage(new Ponto(getGameWidth(.92f),
				getGameHeight(.25f)), getGameAssetManager().getBitmap(
				R.drawable.asa_demonio));
		asaDemonioOposta.setHeightKeepingRatio(getGameHeight(.55f));
		asaDemonioOposta.sethFlip(true);
		asaDemonioOposta.getPaint().setAlpha(0);

		addSubNode(box);
		addSubNode(fundo, 1);
		addSubNode(asaDemonio, 2);
		addSubNode(asaDemonioOposta, 2);
		addSubNode(asaAnjo, 2);
		addSubNode(asaAnjoOposta, 2);
		addSubNode(new NTimer(15000, new Function0<Void>() {

			@Override
			public Void apply() {
				movingSignal = (movingSignal == 1 ? -1 : 1);
				return null;
			}
		}));

	}

	public void goToAngelColor() {
		destinyColor = Tuple3.from(43, 167, 203);

		if (asaAnjo.getPaint().getAlpha() != 255) {
			addSubNode(new NPaintFade(asaAnjo.getPaint(), true, 1000));
		}
		addSubNode(new NPaintFade(asaAnjoOposta.getPaint(), true, 1000));

		addSubNode(new NPaintFade(asaDemonio.getPaint(), false, 1000));
		if (asaDemonioOposta.getPaint().getAlpha() != 0) {
			addSubNode(new NPaintFade(asaDemonioOposta.getPaint(), false, 1000));
		}
	}

	public void goToDemonsColor() {
		destinyColor = Tuple3.from(255, 0, 0);

		if (asaDemonio.getPaint().getAlpha() != 255) {
			addSubNode(new NPaintFade(asaDemonio.getPaint(), true, 1000));
		}
		addSubNode(new NPaintFade(asaDemonioOposta.getPaint(), true, 1000));

		addSubNode(new NPaintFade(asaAnjo.getPaint(), false, 1000));

		if (asaAnjoOposta.getPaint().getAlpha() != 0) {
			addSubNode(new NPaintFade(asaAnjoOposta.getPaint(), false, 1000));
		}
	}

	@Override
	protected void update(long delta) {
		fundo.setCenter(fundo.getCenter().translate(
				movingSignal * ((20.0f / 1000) * delta), 0));

		Tuple3<Integer, Integer, Integer> atual = box.getColor();

		float step = delta * COLOR_CHANGE_SPEED;
		// R
		atual.setVal1(calculaNovoValor(atual.getVal1(), destinyColor.getVal1(),
				step));
		// G
		atual.setVal2(calculaNovoValor(atual.getVal2(), destinyColor.getVal2(),
				step));
		// B
		atual.setVal3(calculaNovoValor(atual.getVal3(), destinyColor.getVal3(),
				step));

		box.setColor(atual.getVal1(), atual.getVal2(), atual.getVal3());
	}

	private int calculaNovoValor(int atual, int destino, float step) {
		int retorno;

		int signal = destino - atual > 0 ? 1 : -1;
		retorno = (int) (atual + (signal * step));

		if ((signal > 0 && retorno > destino)
				|| (signal < 0 && retorno < destino)) {
			return destino;
		}

		return retorno;
	}
}

package org.vvgaming.harmegido.theGame.objNodes;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.NTimer;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.product.Tuple3;

/**
 * Implementação de um background padrão para o jogo Harmegido, com nuvens se movendo, mudança de cores e etc
 * 
 * @author Vinicius Nogueira
 */
public class NHMBackground extends GameNode
{

	private NSimpleBox box;
	private NImage fundo;
	private NImage asaAnjo;
	private NImage asaDemonio;
	private int movingSignal = 1;

	private float COLOR_CHANGE_SPEED = 255.0f / 2000;

	private Tuple3<Integer, Integer, Integer> destinyColor = Tuple3.from(100, 100, 100);

	@Override
	protected void init()
	{
		super.init();

		box = new NSimpleBox(0, 0, getGameWidth(), getGameHeight(), 0, 0, 0);
		fundo = new NImage(new Ponto(getGameWidth(.5f), getGameHeight(.5f)), getGameAssetManager().getBitmap(R.drawable.fundo));
		fundo.setHeightKeepingRatio(getGameHeight());

		asaAnjo = new NImage(new Ponto(getGameWidth(.86f), getGameHeight(.25f)), getGameAssetManager().getBitmap(R.drawable.asa_anjo));
		asaAnjo.setHeightKeepingRatio(getGameHeight(.55f));
		asaDemonio = new NImage(new Ponto(getGameWidth(.17f), getGameHeight(.25f)), getGameAssetManager().getBitmap(R.drawable.asa_demonio));
		asaDemonio.setHeightKeepingRatio(getGameHeight(.55f));

		addSubNode(box);
		addSubNode(fundo, 1);
		addSubNode(asaDemonio, 2);
		addSubNode(asaAnjo, 2);
		addSubNode(new NTimer(15000, new Function0<Void>()
		{

			@Override
			public Void apply()
			{
				movingSignal = (movingSignal == 1 ? -1 : 1);
				return null;
			}
		}));

	}

	public void goToAngelColor()
	{
		destinyColor = Tuple3.from(43, 167, 203);
	}

	public void goToDemonsColor()
	{
		destinyColor = Tuple3.from(255, 0, 0);
	}

	@Override
	protected void update(long delta)
	{
		fundo.setCenter(fundo.getCenter().translate(movingSignal * ((20.0f / 1000) * delta), 0));

		Tuple3<Integer, Integer, Integer> atual = box.getColor();

		float step = delta * COLOR_CHANGE_SPEED;
		// R
		int val1Signal = destinyColor.getVal1() - atual.getVal1() > 0 ? 1 : -1;
		atual.setVal1((int) (atual.getVal1() + (val1Signal * step)));
		if ((val1Signal > 0 && atual.getVal1() > destinyColor.getVal1()) || (val1Signal < 0 && atual.getVal1() < destinyColor.getVal1()))
			atual.setVal1(destinyColor.getVal1());
		// G
		int val2Signal = destinyColor.getVal2() - atual.getVal2() > 0 ? 1 : -1;
		atual.setVal2((int) (atual.getVal2() + (val2Signal * step)));
		if ((val2Signal > 0 && atual.getVal2() > destinyColor.getVal2()) || (val2Signal < 0 && atual.getVal2() < destinyColor.getVal2()))
			atual.setVal2(destinyColor.getVal2());
		// B
		int val3Signal = destinyColor.getVal3() - atual.getVal3() > 0 ? 1 : -1;
		atual.setVal3((int) (atual.getVal3() + (val3Signal * step)));
		if ((val3Signal > 0 && atual.getVal3() > destinyColor.getVal3()) || (val3Signal < 0 && atual.getVal3() < destinyColor.getVal3()))
			atual.setVal3(destinyColor.getVal3());

		box.setColor(atual.getVal1(), atual.getVal2(), atual.getVal3());
	}
}

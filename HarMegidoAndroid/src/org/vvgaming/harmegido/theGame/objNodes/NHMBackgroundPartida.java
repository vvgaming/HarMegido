package org.vvgaming.harmegido.theGame.objNodes;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.GameNode;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.util.NTimer;

import com.github.detentor.codex.function.Function0;

/**
 * 
 * @author Vinicius Nogueira
 */
public class NHMBackgroundPartida extends GameNode
{

	private NImage fundo;
	private NImage asa;
	private NImage asaOposta;
	private int movingSignal = 1;

	private final boolean anjo;

	public NHMBackgroundPartida(final boolean anjo)
	{
		super();
		this.anjo = anjo;
	}

	@Override
	protected void init()
	{
		super.init();

		fundo = new NImage(new Ponto(getGameWidth(.5f), getGameHeight(.5f)), getGameAssetManager().getBitmap(R.drawable.fundo_partida));
		fundo.setHeight(getGameHeight(), true);

		if (anjo)
		{
			asa = new NImage(new Ponto(getGameWidth(), getGameHeight(.43f)), getGameAssetManager().getBitmap(R.drawable.asa_anjo));
			asa.setHeight(getGameHeight(.65f), true);
			asa.setRotation(-50.f);

			asaOposta = new NImage(new Ponto(0, getGameHeight(.43f)), getGameAssetManager().getBitmap(R.drawable.asa_anjo));
			asaOposta.sethFlip(true);
			asaOposta.setHeight(getGameHeight(.65f), true);
			asaOposta.setRotation(50.f);

		}
		else
		{

			asa = new NImage(new Ponto(getGameWidth(.05f), getGameHeight(.4f)), getGameAssetManager().getBitmap(R.drawable.asa_demonio));
			asa.setHeight(getGameHeight(.7f), true);
			asa.setRotation(55.f);

			asaOposta = new NImage(new Ponto(getGameWidth(.95f), getGameHeight(.4f)), getGameAssetManager().getBitmap(
					R.drawable.asa_demonio));
			asaOposta.sethFlip(true);
			asaOposta.setHeight(getGameHeight(.7f), true);
			asaOposta.setRotation(-55.f);

		}

		addSubNode(fundo, 0);
		addSubNode(asa, 1);
		addSubNode(asaOposta, 1);
		addSubNode(new NTimer(15000, new Function0<Void>()
		{

			@Override
			public Void apply()
			{
				movingSignal = movingSignal == 1 ? -1 : 1;
				return null;
			}
		}));

	}

	@Override
	protected void update(final long delta)
	{
		fundo.setCenter(fundo.getCenter().translate(movingSignal * (20.0f / 1000 * delta), 0));
	}

}

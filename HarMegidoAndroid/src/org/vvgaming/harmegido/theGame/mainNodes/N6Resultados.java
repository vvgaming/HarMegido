package org.vvgaming.harmegido.theGame.mainNodes;

import org.vvgaming.harmegido.gameEngine.RootNode;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.fx.NBlinker;
import org.vvgaming.harmegido.gameEngine.nodes.util.NParallelWorker;
import org.vvgaming.harmegido.lib.model.Scoreboard;
import org.vvgaming.harmegido.lib.model.TeamType;
import org.vvgaming.harmegido.theGame.objNodes.NHMBackground;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;
import org.vvgaming.harmegido.uos.UOSFacade;

import android.graphics.Paint.Align;
import android.view.MotionEvent;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.monads.Either;

/**
 * Tela simples de introdução com uma pequena mensagem para o usuário clicar e
 * seguir em frente
 * 
 * @author Vinicius Nogueira
 */
public class N6Resultados extends NHMMainNode {

	private final String NOME_PARTIDA;
	private NText statsTxt;
	private NText tituloTxt;

	public N6Resultados(final String nomePartida) {
		NOME_PARTIDA = nomePartida;
	}

	@Override
	public void init() {
		super.init();

		final NParallelWorker worker;
		addSubNode(worker = new NParallelWorker());

		tituloTxt = new NText(getGameWidth(.5f), getGameHeight(.1f),
				NOME_PARTIDA);
		tituloTxt.face = getDefaultFace();
		tituloTxt.paint.setTextAlign(Align.CENTER);
		statsTxt = new NText(getGameWidth(.5f), getGameHeight(.5f),
				"Carregando resultados da partida...");
		statsTxt.face = getDefaultFace();
		statsTxt.paint.setTextAlign(Align.CENTER);

		worker.addToInit(new Function0<Void>() {
			@Override
			public Void apply() {
				worker.putTask(new Function0<Void>() {
					@Override
					public Void apply() {
						Either<Exception, Scoreboard> stats = UOSFacade
								.getDriverFacade().getPontuacao(NOME_PARTIDA);
						if (stats.isRight()) {
							String statsTexto = "Partida encerrada\n\n| ";
							for (final TeamType t : TeamType.values()) {
								statsTexto += t.toString() + ": "
										+ stats.getRight().getPontuacao(t)
										+ " |";
							}
							statsTxt.text = statsTexto;
						} else {
							statsTxt.text = "Falha ao carregar \ndados da partida";
						}
						return null;
					}
				});
				return null;
			}
		});

		String toqParaComecarStr = "Toque na tela para recomeçar...";
		NText toqueParaComecarText = new NText((int) getGameWidth(.5f),
				(int) getGameHeight(.9f), toqParaComecarStr);
		toqueParaComecarText.face = getDefaultFace();
		toqueParaComecarText.paint.setTextAlign(Align.CENTER);
		toqueParaComecarText.vAlign = NText.VerticalAlign.MIDDLE;
		NBlinker blinkingFxToqueParaComecar = new NBlinker(toqueParaComecarText);

		addSubNode(blinkingFxToqueParaComecar);

		addSubNode(new NHMBackground(), 0);
		addSubNode(tituloTxt, 1);
		addSubNode(statsTxt, 1);
		addSubNode(toqueParaComecarText, 1);

	}

	@Override
	public void update(long delta) {
	}

	@Override
	public boolean onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			RootNode.getInstance().changeMainNode(new N3SelecaoPartida());
			return true;
		}
		return false;
	}

}

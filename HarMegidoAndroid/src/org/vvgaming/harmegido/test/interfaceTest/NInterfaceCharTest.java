package org.vvgaming.harmegido.test.interfaceTest;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.NText.VerticalAlign;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonImage;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NGroupToggleButton;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NToggleButton;
import org.vvgaming.harmegido.theGame.objNodes.NHMBackground;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;

import android.graphics.Paint.Align;

/**
 * Menu de opções para criar seu char antes de entrar na partida. <br/>
 * - Escolhe o time <br/>
 * - Escolhe o nome do jogador (dentre 3 possíveis aleatórios) <br/>
 * 
 * @author Vinicius Nogueira
 */
public class NInterfaceCharTest extends NHMMainNode {

	protected static final int QTD_NOMES = 3;

	private NHMBackground background;

	private NToggleButton anjosTglBtn;
	private NToggleButton demoniosTglBtn;
	private NGroupToggleButton timeTglGroup;

	private NText orientacao;

	public NInterfaceCharTest() {
	}

	@Override
	public void init() {
		super.init();

		// botão de seleção dos demonios
		final NButtonImage demoniosBtn = new NButtonImage(new NImage(new Ponto(
				getGameWidth(.25f), getGameHeight(.4f)), getGameAssetManager()
				.getBitmap(R.drawable.morgana)));
		demoniosBtn.getImage().setWidthKeepingRatio(getGameWidth(.4f));
		demoniosTglBtn = new NToggleButton(demoniosBtn);

		// botão de seleção dos anjos
		final NButtonImage anjosBtn = new NButtonImage(new NImage(new Ponto(
				getGameWidth(.75f), getGameHeight(.4f)), getGameAssetManager()
				.getBitmap(R.drawable.kayle)));
		anjosBtn.getImage().setWidthKeepingRatio(getGameWidth(.4f));
		anjosTglBtn = new NToggleButton(anjosBtn);

		// agrupando os toogles de demonios e anjos
		timeTglGroup = new NGroupToggleButton(demoniosTglBtn, anjosTglBtn);

		orientacao = new NText(getGameWidth(.5f), getGameHeight(.90f), "");
		orientacao.face = getDefaultFace();
		orientacao.paint.setTextAlign(Align.CENTER);
		orientacao.vAlign = VerticalAlign.MIDDLE;

		background = new NHMBackground();
		addSubNode(background, 0);
		addSubNode(timeTglGroup, 1);
		addSubNode(orientacao, 1);
	}

	@Override
	public void update(final long delta) {
		if (timeTglGroup.getToggledIndex().isEmpty()) {
			orientacao.text = "Selecione seu time...";
		} else {
			orientacao.text = "Escolha seu nome\n e entrará na partida...";
		}
	}

}

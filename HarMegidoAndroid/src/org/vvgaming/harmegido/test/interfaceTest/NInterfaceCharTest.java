package org.vvgaming.harmegido.test.interfaceTest;

import java.util.Arrays;
import java.util.List;

import org.vvgaming.harmegido.R;
import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.NText.VerticalAlign;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonText;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NGroupToggleButton;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NToggleButton;
import org.vvgaming.harmegido.lib.model.TeamType;
import org.vvgaming.harmegido.theGame.objNodes.NHMBackground;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint.Align;

import com.github.detentor.codex.function.Function0;

/**
 * Menu de opções para criar seu char antes de entrar na partida. <br/>
 * - Escolhe o time <br/>
 * - Escolhe o nome do jogador (dentre 3 possíveis aleatórios) <br/>
 * 
 * @author Vinicius Nogueira
 */
public class NInterfaceCharTest extends NHMMainNode {

	private NHMBackground background;

	private NToggleButton anjosTglBtn;
	private NToggleButton demoniosTglBtn;
	private NGroupToggleButton timeTglGroup;
	
	private NImage charImg;

	private NText orientacao;
	private TeamType tipoTime;
	private int curPos = 0;
	
	private final List<Integer> angelImg = Arrays.asList(R.drawable.angel01, 
													     R.drawable.angel02, 
													     R.drawable.angel03, 
													     R.drawable.angel04, 
													     R.drawable.angel05,
													     R.drawable.angel06,
													     R.drawable.angel07,
													     R.drawable.angel08, 
													     R.drawable.angel09,
													     R.drawable.angel10);
	
	private final List<Integer> demonImg = Arrays.asList(R.drawable.demon01, 
													     R.drawable.demon02, 
													     R.drawable.demon03, 
													     R.drawable.demon04, 
													     R.drawable.demon05,
													     R.drawable.demon06,
													     R.drawable.demon07,
													     R.drawable.demon08, 
													     R.drawable.demon09,
													     R.drawable.demon10);
	

	public NInterfaceCharTest() {
	}

	@Override
	public void init() {
		
		super.init();
		
		tipoTime = TeamType.LIGHT;
		
		charImg = new NImage(new Ponto(getGameWidth(.5f), getGameHeight(.40f)), getImage());
		charImg.setWidth(getGameWidth(.70f), true);
		
//		// botão de seleção dos demonios
		final NButtonText proximoBtn = new NButtonText(new NText(getGameWidth(.10f), getGameHeight(.90f), "próximo"));
//
		proximoBtn.setOnClickFunction(new Function0<Void>() {
			@Override
			public Void apply() {
				
				if (++curPos > 9)
				{
					curPos = 0;
				}
				
				atualizarImagem();
				return null;
			}
		});
		
		final NButtonText trocarTimeBtn = new NButtonText(new NText(getGameWidth(.65f), getGameHeight(.90f), "trocar"));
		//
		trocarTimeBtn.setOnClickFunction(new Function0<Void>() {
					@Override
					public Void apply() {
						
						if (tipoTime == TeamType.DARK)
						{
							tipoTime = TeamType.LIGHT;
							curPos = 0;
						}
						else
						{
							tipoTime = TeamType.DARK;
							curPos = 0;
						}
						
						atualizarImagem();
						
						return null;
					}
				});
		
//		demoniosBtn.getImage().setWidthKeepingRatio(getGameWidth(.4f));
//		demoniosTglBtn = new NToggleButton(demoniosBtn);
//		
//		// botão de seleção dos anjos
//		final NButtonImage anjosBtn = new NButtonImage(new NImage(new Ponto(
//				getGameWidth(.75f), getGameHeight(.4f)), getGameAssetManager()
//				.getBitmap(R.drawable.kayle)));
//		anjosBtn.getImage().setWidthKeepingRatio(getGameWidth(.4f));
//		anjosTglBtn = new NToggleButton(anjosBtn);

		// agrupando os toogles de demonios e anjos
//		timeTglGroup = new NGroupToggleButton(demoniosTglBtn, anjosTglBtn);

		orientacao = new NText(getGameWidth(.5f), getGameHeight(.90f), "");
		orientacao.face = getDefaultFace();
		orientacao.paint.setTextAlign(Align.CENTER);
		orientacao.vAlign = VerticalAlign.MIDDLE;

		background = new NHMBackground();
		addSubNode(background, 0);
		addSubNode(charImg, 1);
		addSubNode(proximoBtn, 1);
		addSubNode(trocarTimeBtn, 1);
//		addSubNode(timeTglGroup, 1);
//		addSubNode(orientacao, 1);
	}
	
	private void atualizarImagem()
	{
		charImg.setBmp(getImage());
	}
	
	private Bitmap getImage()
	{
		int imgCode = 0;
		
		if (tipoTime == TeamType.DARK)
		{
			imgCode = demonImg.get(curPos);

			charImg.setBmp(getGameAssetManager().getBitmap(demonImg.get(curPos)));
		}
		else
		{
			imgCode = angelImg.get(curPos);
		}
		
		final Resources theResource = getGameAssetManager().getActivity().getResources();
		return BitmapFactory.decodeResource(theResource, imgCode);
	}
	
	/**
	 * Retorna o prefixo dos arquivos de imagem a partir do tipo passado como parâmetro
	 */
	private String getPrefix(final TeamType fromType)
	{
		switch(fromType)
		{
			case DARK:
				return "demon";
			case LIGHT: 
				return "angel";
			default:
				throw new IllegalArgumentException("Tipo não reconhecido: " + fromType);
		}
	}
	
	

	@Override
	public void update(final long delta) {
//		if (timeTglGroup.getToggledIndex().isEmpty()) {
//			orientacao.text = "Selecione seu time...";
//		} else if (timeTglGroup.getToggledIndex().get() == 0) {
//			orientacao.text = "Demonios...";
//		} else if (timeTglGroup.getToggledIndex().get() == 1) {
//			orientacao.text = "Anjos...";
//		} else {
//			orientacao.text = "Que isso?";
//		}
	}

}

package org.vvgaming.harmegido.test.interfaceTest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vvgaming.harmegido.gameEngine.geometry.Ponto;
import org.vvgaming.harmegido.gameEngine.nodes.NImage;
import org.vvgaming.harmegido.gameEngine.nodes.NText;
import org.vvgaming.harmegido.gameEngine.nodes.buttons.NButtonText;
import org.vvgaming.harmegido.lib.model.TeamType;
import org.vvgaming.harmegido.theGame.objNodes.NHMBackground;
import org.vvgaming.harmegido.theGame.objNodes.NHMMainNode;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.github.detentor.codex.function.Function0;
import com.github.detentor.codex.product.Tuple2;

/**
 * Menu de opções para criar seu char antes de entrar na partida. <br/>
 * - Escolhe o time <br/>
 * - Escolhe o nome do jogador (dentre 3 possíveis aleatórios) <br/>
 * 
 * @author Vinicius Nogueira
 */
public class NInterfaceCharTest extends NHMMainNode {

	private NHMBackground background;

	private TeamType tipoTime;
	private int curPos = 0;

	private List<GameChar> angels;
	private List<GameChar> demons;
	
	private NImage charImg;
	private NText nomeChar;
	private NText flavorText;
	

	public NInterfaceCharTest() {
	}

	@Override
	public void init() {
		
		super.init();
		
		angels = loadChars("angel");
		demons = loadChars("demon");
		
		tipoTime = TeamType.LIGHT;
		
		charImg = new NImage(new Ponto(getGameWidth(.5f), getGameHeight(.40f)), getImage());
		charImg.setWidth(getGameWidth(.65f), true);
		
		nomeChar = new NText(new Ponto(0, 0), getCurChar().getName());
		flavorText = new NText(new Ponto(0, 0), getCurChar().getFlavorText());

		atualizarChar(); //atualiza as posições dos botões

//		// botão de seleção dos demonios
		final NButtonText proximoBtn = new NButtonText(new NText(getGameWidth(.10f), getGameHeight(.95f), "próximo"));
//
		proximoBtn.setOnClickFunction(new Function0<Void>() {
			@Override
			public Void apply() {
				
				if (++curPos > 9)
				{
					curPos = 0;
				}
				
				atualizarChar();
				return null;
			}
		});
		
		final NButtonText trocarTimeBtn = new NButtonText(new NText(getGameWidth(.65f), getGameHeight(.10f), "trocar"));
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
						
						atualizarChar();
						
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

		background = new NHMBackground();
		addSubNode(background, 0);
		addSubNode(charImg, 1);
		addSubNode(proximoBtn, 1);
		addSubNode(trocarTimeBtn, 1);
		addSubNode(flavorText, 1);
		addSubNode(nomeChar, 1);
//		addSubNode(timeTglGroup, 1);
//		addSubNode(orientacao, 1);
	}
	
	private GameChar getCurChar()
	{
		return tipoTime == TeamType.LIGHT ? angels.get(curPos) :  demons.get(curPos);
	}
	
	private void atualizarChar()
	{
		final Ponto charPos = charImg.getPos();
		
		//Altera as informações do nome do char
		nomeChar.text = getCurChar().getName();
		nomeChar.setWidth(charImg.getWidth());
		final Rect nomeCharRect = nomeChar.getTextBounds();
		nomeChar.pos = new Ponto(charPos.x, charPos.y - (nomeCharRect.height() / 2));
		
		//Altera as informações do flavor text
		flavorText.text = getCurChar().getFlavorText();
		flavorText.setWidth(charImg.getWidth());
		final Rect flavorRect = flavorText.getTextBounds();
		flavorText.pos = new Ponto(charPos.x, charPos.y + charImg.getHeight() + flavorRect.height());
		
		//Troca o bmp do char
		charImg.setBmp(getImage());
	}
	
	private List<GameChar> loadChars(final String prefix)
	{
		final int fileId = idFromName(prefix + "s" , "raw");
		final List<Tuple2<String, String>> charNames = parseFile(getGameAssetManager().getRawTextFile(fileId));
		final List<GameChar> toReturn = new ArrayList<>();
		
		for (int i = 1; i < 11; i++)
		{
			final String fileName = prefix + (i < 10 ? "0" : "") + i;
			final Tuple2<String, String> curTuple = charNames.get(i-1);
			toReturn.add(new GameChar(curTuple.getVal1(), curTuple.getVal2(), idFromName(fileName, "drawable")));
		}
		
		return toReturn;
	}
	
	private List<Tuple2<String, String>> parseFile(final String theFile)
	{
		final List<Tuple2<String, String>> toReturn = new ArrayList<>();
		final Matcher mat = Pattern.compile("([^{]+)\\{([^}]+)\\}").matcher(theFile);

		while (mat.find())
		{
			toReturn.add(Tuple2.from(mat.group(1).trim(), mat.group(2).trim()));
		}
		return toReturn;
	}
	
	private int idFromName(final String theName, final String theFolder)
	{
		final Activity activity = getGameAssetManager().getActivity();
		final String packName = activity.getPackageName();
		return activity.getResources().getIdentifier(theName, theFolder, packName);
	}
	
	private Bitmap getImage()
	{
		int imgCode = 0;
		
		if (tipoTime == TeamType.DARK)
		{
			imgCode = demons.get(curPos).getResId();
		}
		else
		{
			imgCode = angels.get(curPos).getResId();
		}
		
		final Resources theResource = getGameAssetManager().getActivity().getResources();
		return BitmapFactory.decodeResource(theResource, imgCode);
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

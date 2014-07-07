package org.vvgaming.harmegido.test.interfaceTest;

public class GameChar 
{
	private final String name;
	private final int resId;
	private final String flavorText;
	
	public GameChar(String name, String flavorText, int resId) {
		super();
		this.name = name;
		this.resId = resId;
		this.flavorText = flavorText;
	}

	public String getName() {
		return name;
	}

	public int getResId() {
		return resId;
	}

	public String getFlavorText() {
		return flavorText;
	}

}

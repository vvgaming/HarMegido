package org.vvgaming.harmegido.lib.model;

/**
 * Wrapper da classe Mat da OpenCV. Essa classe além de encapsular a Mat, matém o conteúdo comprimido e em base64 para facilitar o
 * transporte na rede
 * 
 * @author Vinicius Nogueira
 * 
 */
public class OpenCVMatWrapper
{

	private final String content; // em base 64
	private final int width;
	private final int height;
	private final int cvType;

	public OpenCVMatWrapper(final String content, final int width, final int height, final int cvType)
	{
		super();
		this.content = content;
		this.width = width;
		this.height = height;
		this.cvType = cvType;
	}

	public static OpenCVMatWrapper from(final String content, final int width, final int height, final int cvType)
	{
		return new OpenCVMatWrapper(content, width, height, cvType);
	}

	public String getContent()
	{
		return content;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getCvType()
	{
		return cvType;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (content == null ? 0 : content.hashCode());
		result = prime * result + cvType;
		result = prime * result + height;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final OpenCVMatWrapper other = (OpenCVMatWrapper) obj;
		if (content == null)
		{
			if (other.content != null)
			{
				return false;
			}
		}
		else if (!content.equals(other.content))
		{
			return false;
		}
		if (cvType != other.cvType)
		{
			return false;
		}
		if (height != other.height)
		{
			return false;
		}
		if (width != other.width)
		{
			return false;
		}
		return true;
	}

}

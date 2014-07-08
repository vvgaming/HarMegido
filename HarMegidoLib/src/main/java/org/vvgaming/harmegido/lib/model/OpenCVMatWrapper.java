package org.vvgaming.harmegido.lib.model;

import java.util.Arrays;

public class OpenCVMatWrapper
{

	private final byte[] bytes;
	private final int width;
	private final int height;
	private final int cvType;

	public OpenCVMatWrapper(final byte[] bytes, final int width, final int height, final int cvType)
	{
		super();
		this.bytes = bytes;
		this.width = width;
		this.height = height;
		this.cvType = cvType;
	}

	public static OpenCVMatWrapper from(final byte[] bytes, final int width, final int height, final int cvType)
	{
		return new OpenCVMatWrapper(bytes, width, height, cvType);
	}

	public byte[] getBytes()
	{
		return bytes;
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
		result = prime * result + Arrays.hashCode(bytes);
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
		if (!Arrays.equals(bytes, other.bytes))
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

package org.vvgaming.harmegido.lib.model;

import java.util.Arrays;

public class OpenCVMatWrapper
{

	private final byte[] bytes;
	private final int weight;
	private final int height;
	private final int cvType;

	public OpenCVMatWrapper(final byte[] bytes, final int weight, final int height, final int cvType)
	{
		super();
		this.bytes = bytes;
		this.weight = weight;
		this.height = height;
		this.cvType = cvType;
	}

	public static OpenCVMatWrapper from(final byte[] bytes, final int weight, final int height, final int cvType)
	{
		return new OpenCVMatWrapper(bytes, weight, height, cvType);
	}

	public byte[] getBytes()
	{
		return bytes;
	}

	public int getWeight()
	{
		return weight;
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
		result = prime * result + weight;
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
		if (weight != other.weight)
		{
			return false;
		}
		return true;
	}

}

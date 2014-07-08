package org.vvgaming.harmegido.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Utilitário para compressão de arrays de bytes
 * 
 * @author Vinicius Nogueira
 */
public class CompressionUtils
{

	public static byte[] compress(final byte[] data) throws IOException
	{
		final Deflater deflater = new Deflater();
		deflater.setInput(data);

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

		deflater.finish();
		final byte[] buffer = new byte[1024];
		while (!deflater.finished())
		{
			final int count = deflater.deflate(buffer); // returns the generated code... index
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		final byte[] output = outputStream.toByteArray();

		deflater.end();
		return output;
	}

	public static byte[] decompress(final byte[] data) throws IOException, DataFormatException
	{
		final Inflater inflater = new Inflater();
		inflater.setInput(data);

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		final byte[] buffer = new byte[1024];
		while (!inflater.finished())
		{
			final int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		final byte[] output = outputStream.toByteArray();

		inflater.end();
		return output;
	}
}
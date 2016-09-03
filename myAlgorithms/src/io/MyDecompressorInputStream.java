package io;

import java.io.IOException;
import java.io.InputStream;

/**
 * MyDecompressorInputStream class that extends java's InputStream,</br>
 * Will read and decompress the data it gets from InputStream.
 * @author Elad Jarby
 * @version 1.0
 * @since 02.09.2016
 *
 */
public class MyDecompressorInputStream extends InputStream {
	private InputStream in;
	private int currentByte;
	private int count;
	
	/**
	 * Constructor using input stream source.
	 * @param input - Input stream source.
	 */
	public MyDecompressorInputStream(InputStream input) {
		super();
		this.in = input;
	}
	
	/**
	 * Override write method from InputStream.</br>
	 * Reading the compressed data from input stream source,</br>
	 * and decompress that data.
	 */
	@Override
	public int read() throws IOException {
		if (count <= 0 ) {
			if((currentByte = in.read()) == -1) {
				return -1;
			}
			
			if((count = in.read()) == -1) {
				throw (new IOException("Invalid byte array!"));
			}
			
			if (count <= 0) {
				throw (new IOException("Invalid counter"));
			}
		}
		count--;
		return currentByte;
	}

}

package io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * MyCompressorOutputStream class that extends java's OutputStream,</br>
 * Will write and save integer to output stream file.
 * @author Elad Jarby
 * @version 1.0
 * @since 02.09.2016
 *
 */
public class MyCompressorOutputStream extends OutputStream {

	private OutputStream out; // Out that  OutputStream. 
	private int count; // Counter for check how many "1" or "0".
	private int previousByte; // Holding the previous byes.
	
	/**
	 * Constructor using output stream source.
	 * @param output - Output stream source.
	 */
	public MyCompressorOutputStream(OutputStream output) {
		super();
		this.out = output;
		this.count = 0;
	}
	
	/**
	 * Override write method from OutputStream.</br>
	 * Compressing data and then writing to data source.
	 * @param b - Integer that need to write. 
	 */
	@Override
	public void write(int b) throws IOException {
		if(count == 0) {
			this.previousByte = b;
			this.count = 1;
			return;
		}
		
		if(this.previousByte == b) {
			count++;
			if(count == 256) {
				out.write(previousByte);
				out.write(255);
				count=1;
			}
		} else {
			out.write(previousByte);
			out.write(count);
			this.previousByte = b;
			this.count = 1;
		}
	}
	
	/**
	 * Override write method from OutputStream.</br>
	 * Writing byte array to data source.
	 * @param byteArr - Byte array that need to write.
	 */
	@Override
	public void write(byte[] byteArr) throws IOException {
		super.write(byteArr);
		if(count > 0) {
			out.write((byte)previousByte);
			out.write((byte)count);
		}
		
		count = 0;
		previousByte = 0;
	}

}

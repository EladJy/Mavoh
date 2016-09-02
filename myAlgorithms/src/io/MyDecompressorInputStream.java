package io;

import java.io.IOException;
import java.io.InputStream;

public class MyDecompressorInputStream extends InputStream {
	private InputStream in;
	private int currentByte;
	private int count;
	
	public MyDecompressorInputStream(InputStream input) {
		super();
		this.in = input;
	}
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

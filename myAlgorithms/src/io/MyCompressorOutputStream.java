package io;

import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {

	private OutputStream out;
	private int count;
	private int previousByte;
	
	public MyCompressorOutputStream(OutputStream output) {
		super();
		this.out = output;
		this.count = 0;
	}
	
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

package net.technowizardry.xmpp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class MemoryStream {
	private PipedInputStream toServerStream;
	private OutputStream outputStream;

	public MemoryStream() throws IOException {
		toServerStream = new PipedInputStream();
		outputStream = new PipedOutputStream(toServerStream);
	}

	public OutputStream getStream() {
		return outputStream;
	}

	public String readAll() throws IOException {
		int avail = toServerStream.available();
		byte[] buffer = new byte[avail];
		toServerStream.read(buffer);
		return new String(buffer);
	}
}

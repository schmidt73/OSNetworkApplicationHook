package com.schmidt73.networkapplicationhook.threads;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class StreamGobbler implements Runnable {

	/**
	 * Stream to be gobbled.
	 */

	private InputStream stream;

	/**
	 * Constructor that takes the stream to be gobbled.
	 * 
	 * @param stream
	 *            The stream to be gobbled.
	 */

	public StreamGobbler(InputStream stream) {
		this.stream = stream;
	}

	/*
	 * Run method takes the stream and reads it. Doing nothing with it.
	 * Necessary for ProcessBuilder
	 * http://docs.oracle.com/javase/7/docs/api/java/lang/ProcessBuilder.html
	 */

	public void run() {
		try {
			byte[] bytes = new byte[1024];
			while (stream.read(bytes) > 0)
				;
		} catch (IOException e) {
			// Ignoring exception because nothing to do.
		} finally {
			closeStream(stream);
		}
	}

	/**
	 * Closes the stream with some handling.
	 * 
	 * @param stream
	 *            The stream to close.
	 */

	private void closeStream(Closeable stream) {
		try {
			if (stream != null)
				stream.close();
		} catch (IOException e) {
			// Ignoring exception because nothing to do.
		}
	}
}

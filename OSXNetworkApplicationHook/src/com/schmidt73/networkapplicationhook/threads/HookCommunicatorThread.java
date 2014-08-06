package com.schmidt73.networkapplicationhook.threads;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.schmidt73.networkapplicationhook.parser.ParseRequest;

public class HookCommunicatorThread implements Runnable {
	private int port;

	/**
	 * Constructor that opens a port which reads from hooked functions.
	 * 
	 * @param port
	 *            Port to open.
	 */

	public HookCommunicatorThread(int port) {
		this.port = port;
	}

	/*
	 * Attempts to bind the socket until success. Then it reads info from the
	 * hooked function. It takes the read data, checks to see if it is able to
	 * be parsed, and then runs a thread that parses the data.
	 */

	public void run() {
		ServerSocket socket = null;

		while (true) {
			try {
				socket = new ServerSocket(port);
				break;
			} catch (IOException e) {
				continue;
			}
		}

		while (true) {
			try {
				StringBuilder bufferBuilder = new StringBuilder();
				Socket connectedSock = socket.accept();
				InputStream stream = connectedSock.getInputStream();
				byte[] bytearray = new byte[8192];

				while (stream.read(bytearray) > 0) {
					bufferBuilder.append(new String(bytearray));
				}

				if (ParseRequest.checkIfWebRequest(bufferBuilder.toString())) {
					new Thread(new ParseWebRequestThread(
							bufferBuilder.toString())).start();
				}
			} catch (IOException e) {
				continue;
			}
		}
	}
}
package com.schmidt73.networkapplicationhook;

import java.io.IOException;

import com.schmidt73.networkapplicationhook.threads.HookCommunicatorThread;

/**
 * This class contains the main method where we will start all of the threads
 * that will do all of the work. We will also use this class to test new
 * functionality. Everything starts here
 * 
 * @author schmidt73
 */

public class Main {

	/*
	 * Currently opens Google Chrome with the hooked library see (Hook Source
	 * Code/Hook.c). Then opens port 42333 and listens for messages from the
	 * hooked function. It then parses those messages and checks for certain
	 * forms being filled and prints out those forms and their values. You can
	 * do much more than just print out the forms and their values. Will add
	 * more later.
	 */

	public static void main(String[] args) throws IOException {
		// Make sure Chrome is open and hook a new Chrome.
		System.out
				.println(Hook
						.hookExecutable("/Users/schmidt73/Chrome Hook.dylib",
								"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"));
		new Thread(new HookCommunicatorThread(42333)).start();
	}
}

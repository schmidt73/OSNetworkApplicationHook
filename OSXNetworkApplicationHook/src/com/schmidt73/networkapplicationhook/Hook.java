package com.schmidt73.networkapplicationhook;

import java.io.IOException;
import java.util.Map;

import com.schmidt73.networkapplicationhook.threads.StreamGobbler;

public class Hook {

	/**
	 * These following strings are used to hook an application on OS X.
	 */

	private static final String INSERT_LIBRARIES = "DYLD_INSERT_LIBRARIES";
	private static final String FLAT_NAMESPACE = "DYLD_FORCE_FLAT_NAMESPACE";

	/**
	 * Function runs an executable with the following library as its hook.
	 * 
	 * @param library
	 *            The full path of the library with the functions that we want
	 *            to hook. The library should have a flat name-space.
	 * @param executable
	 *            The full path of the executable we want to run with the hook.
	 * @return Returns 0 on success and -1 on failure.
	 */

	public static int hookExecutable(String library, String executable) {
		try {
			ProcessBuilder pb = new ProcessBuilder(executable);
			pb.redirectErrorStream(true);

			Map<String, String> env = pb.environment();
			env.clear();
			env.put(INSERT_LIBRARIES, library);
			env.put(FLAT_NAMESPACE, "1");

			Process p = pb.start();
			new Thread(new StreamGobbler(p.getInputStream())).start();

			return 0;
		} catch (IOException e) {
			return -1;
		}
	}
}

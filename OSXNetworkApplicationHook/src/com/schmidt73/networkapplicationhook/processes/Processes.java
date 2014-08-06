package com.schmidt73.networkapplicationhook.processes;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * This class is going to be used to toy with processes. (Necessary later).
 * 
 * @author schmidt73
 */

public class Processes {
	public static interface ProcessesObjectiveC extends Library {
		public final static Processes.ProcessesObjectiveC INSTANCE = (Processes.ProcessesObjectiveC) Native
				.loadLibrary("OSXFormGrabberUtilitiesCocoa",
						Processes.ProcessesObjectiveC.class);

		public String getForegroundWindow();
	}

	/**
	 * List of signals that we can send to processes.
	 */

	private static final int SIG_KILL = 9;

	/**
	 * Registers any Native libraries we will be using functions from.
	 */

	static {
		Native.register("c");
	}

	/**
	 * Function kills process by its process ID.
	 * 
	 * @param pid
	 *            Process ID of process to kill.
	 * @return Returns -1 on failure (usually not privileged) and the ID of
	 *         killed process on success.
	 */

	public static int kill(int pid) {
		if (kill(pid, SIG_KILL) == -1)
			return -1;
		return pid;
	}

	private static native int kill(int pid, int sig);

	public static native String getForegroundWindow();
}

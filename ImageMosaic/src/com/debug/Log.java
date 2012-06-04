package com.debug;

import com.gui.LogGUI;

public class Log {
	private static final boolean DEBUG = true;

	private static LogGUI lGUI = null;

	private Log() {
	}

	public static void print(String s) {
		if (DEBUG)
			System.out.println(s);

		if (lGUI != null && !lGUI.isLogTextNull()) {
			lGUI.appendLog(s + "\n");
		}
	}

	public static void setLogGUI(LogGUI logGUI) {
		lGUI = logGUI;
	}

	public static void disposeLogGUI() {
		lGUI = null;
	}

}

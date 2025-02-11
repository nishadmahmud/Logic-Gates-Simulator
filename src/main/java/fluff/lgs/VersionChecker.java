package fluff.lgs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import fluff.lgs.utils.Utils;

public class VersionChecker {
	
	private static final int VERSION = 1;
	
	private static boolean outdated;
	
	public static void check() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/muscaa/logic-gates-sim/main/version").openStream()))) {
			int version = Integer.valueOf(in.readLine()).intValue();
			if (VERSION != version) {
				outdated = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void openGithub() {
		Utils.openURL("https://github.com/muscaa/logic-gates-sim");
	}
	
	public static boolean isOutdated() {
		return outdated;
	}
}

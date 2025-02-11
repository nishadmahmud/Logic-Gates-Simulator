package fluff.lgs.utils;

public class MathUtils {
	
	public static int clampInt(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static float clampFloat(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}
}

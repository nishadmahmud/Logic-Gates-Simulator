package fluff.lgs.utils;


public class Timer {
	
	private long last;
	
	public Timer() {
		reset();
	}
	
	public boolean passed(long millis, boolean reset) {
		if (last + millis < System.currentTimeMillis()) {
			if (reset) reset();
			return true;
		}
		return false;
	}
	
	public boolean passed(long millis) {
		return passed(millis, false);
	}
	
	public void reset(long offset) {
		last = System.currentTimeMillis() + offset;
	}
	
	public void reset() {
		reset(0);
	}
}

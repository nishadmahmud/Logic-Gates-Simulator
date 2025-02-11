package fluff.lgs.gui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fluff.lgs.gui.elements.Window;

public class WindowRegistry {
	
	private final Map<Long, Window> reg = new HashMap<>();
	
	public long winID = 0;
	
	public void put(long winID, Window gw) {
		reg.put(winID, gw);
	}
	
	public Window lookup(long winID) {
		return reg.get(winID);
	}
	
	public long genWinID(Window win) {
		long winID = this.winID++;
		put(winID, win);
		return winID;
	}
	
	public void reset(long winID) {
		reg.clear();
		this.winID = winID;
	}
	
	public Collection<Window> values() {
		return reg.values();
	}
}

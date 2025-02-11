package fluff.lgs.gate.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fluff.lgs.gui.elements.gate.ButtonConnection;

public class OverflowChecker {
	
	private static boolean check(List<IConnection> path, IConnection c) {
		if (c instanceof ButtonConnection bc) {
			return check(path, bc.from);
		}
		
		if (path.contains(c)) return true;
		
		if (c instanceof GateConnection gc) {
			path.add(gc);
			
			for (ButtonConnection bc : gc.gate.inputs) {
				if (check(path, bc)) return true;
			}
			
			path.remove(path.size() - 1);
		}
		
		return false;
	}
	
	public static boolean check(IConnection c) {
		if (c == null) return false;
		
		return check(new ArrayList<>(), c);
	}
}

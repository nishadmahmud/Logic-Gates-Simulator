package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class OrGate extends LogicGate {
	
	public OrGate() {
		super(NativeGateType.OR);
	}
	
	@Override
	public LogicalValue[] getOutputs() {
		// Check if all inputs are connected
		int connectedCount = 0;
		for (int i = 0; i < inputCount; i++) {
			ButtonConnection connection = this.inputs[i];
			if (connection != null && connection.from != null) {
				connectedCount++;
			}
		}
		
		// If not all inputs are connected, return UNDEFINED
		if (connectedCount < inputCount) {
			return new LogicalValue[] { LogicalValue.UNDEFINED };
		}
		
		// All inputs are connected, evaluate OR logic
		for (int i = 0; i < inputCount; i++) {
			if (input(i) == LogicalValue.TRUE) {
				return new LogicalValue[] { LogicalValue.TRUE };
			}
		}
		
		return new LogicalValue[] { LogicalValue.FALSE };
	}
}

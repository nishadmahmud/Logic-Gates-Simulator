package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class XnorGate extends LogicGate {
	
	public XnorGate() {
		super(NativeGateType.XNOR);
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
		
		// All inputs are connected, evaluate XNOR logic
		int trueCount = 0;
		for (int i = 0; i < inputCount; i++) {
			if (input(i) == LogicalValue.TRUE) {
				trueCount++;
			}
		}
		
		return new LogicalValue[] { (trueCount % 2) == 0 ? LogicalValue.TRUE : LogicalValue.FALSE };
	}
}

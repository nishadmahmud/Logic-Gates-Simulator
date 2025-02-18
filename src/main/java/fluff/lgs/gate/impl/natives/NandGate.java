package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.IGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class NandGate extends LogicGate {
	
	public NandGate() {
		super(NativeGateType.NAND);
	}
	
	@Override
	public LogicalValue[] getOutputs() {
		LogicalValue[] inputs = getInputs();
		
		// Count connected inputs and check if all are connected
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
		
		// All inputs are connected, evaluate NAND logic
		boolean allTrue = true;
		for (int i = 0; i < inputCount; i++) {
			if (inputs[i] != LogicalValue.TRUE) {
				allTrue = false;
				break;
			}
		}
		
		return new LogicalValue[] { allTrue ? LogicalValue.FALSE : LogicalValue.TRUE };
	}
}

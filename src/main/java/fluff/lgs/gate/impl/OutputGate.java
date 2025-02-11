package fluff.lgs.gate.impl;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.OutputValue;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class OutputGate extends LogicGate {
	
	public final OutputValue value;
	
	public OutputGate(OutputValue value) {
		super(NativeGateType.OUTPUT);
		this.value = value;
	}
	
	@Override
	public LogicalValue[] getOutputs() {
		LogicalValue b = input(0);
		value.setLogicalValue(b);
		return null;
	}
}

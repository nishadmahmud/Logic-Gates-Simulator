package fluff.lgs.gate.impl;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.InputValue;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class InputGate extends LogicGate {
	
	public final InputValue value;
	
	public InputGate(InputValue value) {
		super(NativeGateType.INPUT);
		this.value = value;
	}
	
	@Override
	public LogicalValue[] getOutputs() {
		return new LogicalValue[] {
				value.getLogicalValue()
		};
	}
}

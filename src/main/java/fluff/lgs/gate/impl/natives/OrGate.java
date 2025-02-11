package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class OrGate extends LogicGate {
	
	public OrGate() {
		super(NativeGateType.OR);
	}
	
	@Override
	public LogicalValue[] getOutputs() {
		return new LogicalValue[] {
				input(0).or(input(1))
		};
	}
}

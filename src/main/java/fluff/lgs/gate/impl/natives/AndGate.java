package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class AndGate extends LogicGate {
	
	public AndGate() {
		super(NativeGateType.AND);
	}
	
	@Override
	public LogicalValue[] getOutputs() {
		return new LogicalValue[] {
				input(0).and(input(1))
		};
	}
}

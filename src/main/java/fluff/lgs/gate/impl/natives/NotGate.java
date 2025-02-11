package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class NotGate extends LogicGate {
	
	public NotGate() {
		super(NativeGateType.NOT);
	}
	
	@Override
	public LogicalValue[] getOutputs() {
		return new LogicalValue[] {
				input(0).not()
		};
	}
}

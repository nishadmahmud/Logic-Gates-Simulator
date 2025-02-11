package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class XnorGate extends LogicGate {
	
	public XnorGate() {
		super(NativeGateType.XNOR);
	}
	
	@Override
	public LogicalValue[] getOutputs() {
		return new LogicalValue[] {
				input(0).xnor(input(1))
		};
	}
}

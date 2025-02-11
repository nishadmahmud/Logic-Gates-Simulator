package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.IGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.NativeGateType;

public class NandGate extends LogicGate {
	
	public NandGate() {
		super(NativeGateType.NAND);
	}
	
	@Override
	public LogicalValue[] getOutputs() {
		return new LogicalValue[] {
				input(0).nand(input(1))
		};
	}
}

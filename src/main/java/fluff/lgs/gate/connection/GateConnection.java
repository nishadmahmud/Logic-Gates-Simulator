package fluff.lgs.gate.connection;

import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class GateConnection implements IConnection {
	
	protected final LogicGate gate;
	protected final int i;
	
	public GateConnection(LogicGate gate, int i) {
		this.gate = gate;
		this.i = i;
	}
	
	@Override
	public LogicalValue getValue() {
		if (OverflowChecker.check(this)) return LogicalValue.UNDEFINED;
		return gate.getOutputs()[i];
	}
	
	@Override
	public ConnectionType getType() {
		return ConnectionType.GATE;
	}
}

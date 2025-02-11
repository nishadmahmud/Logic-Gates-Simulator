package fluff.lgs.gate;

import fluff.lgs.gate.connection.ConnectionType;
import fluff.lgs.gate.connection.GateConnection;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public abstract class LogicGate {
	
	public final IGateType type;
	public final ButtonConnection[] inputs;
	public final ButtonConnection[] outputs;
	
	public LogicGate(IGateType type) {
		this.type = type;
		this.inputs = new ButtonConnection[type.getInputs()];
		for (int i = 0; i < inputs.length; i++) {
			this.inputs[i] = new ButtonConnection(ConnectionType.INPUT, i);
		}
		this.outputs = new ButtonConnection[type.getOutputs()];
		for (int i = 0; i < outputs.length; i++) {
			this.outputs[i] = new ButtonConnection(ConnectionType.OUTPUT, i);
			GateConnection gc = new GateConnection(this, i);
			this.outputs[i].from = gc;
		}
	}
	
	public abstract LogicalValue[] getOutputs();
	
	protected LogicalValue input(int i) {
		return inputs[i].getValue();
	}
}

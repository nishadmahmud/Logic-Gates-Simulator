package fluff.lgs.gate;

import fluff.lgs.gate.connection.ConnectionType;
import fluff.lgs.gate.connection.GateConnection;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public abstract class LogicGate {
	
	public final IGateType type;
	public final ButtonConnection[] inputs;
	public final ButtonConnection[] outputs;
	
	protected int inputCount;
	protected LogicalValue[] inputValues;
	
	public LogicGate(IGateType type) {
		this.type = type;
		this.inputCount = type.getDefaultInputs();
		this.inputValues = new LogicalValue[type.getMaxInputs()];
		this.inputs = new ButtonConnection[type.getMaxInputs()];
		for (int i = 0; i < type.getMaxInputs(); i++) {
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
	
	public void updateInputCount(int newCount) {
		if (newCount >= type.getMinInputs() && newCount <= type.getMaxInputs()) {
			this.inputCount = newCount;
		}
	}
	
	public int getInputCount() {
		return inputCount;
	}
	
	protected LogicalValue[] getInputs() {
		LogicalValue[] result = new LogicalValue[inputCount];
		System.arraycopy(inputValues, 0, result, 0, inputCount);
		return result;
	}
	
	protected LogicalValue input(int i) {
		return inputs[i].getValue();
	}
}

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
			// Reset input values array to match new count
			this.inputValues = new LogicalValue[newCount];
			for (int i = 0; i < newCount; i++) {
				this.inputValues[i] = LogicalValue.FALSE;
			}
		}
	}
	
	public int getInputCount() {
		return inputCount;
	}
	
	protected LogicalValue input(int i) {
		if (i >= 0 && i < inputCount) {
			ButtonConnection connection = inputs[i];
			if (connection != null && connection.from != null) {
				return connection.getValue();
			}
		}
		return LogicalValue.FALSE;
	}
	
	protected LogicalValue[] getInputs() {
		LogicalValue[] result = new LogicalValue[inputCount];
		for (int i = 0; i < inputCount; i++) {
			result[i] = input(i);
			// Cache the input value
			inputValues[i] = result[i];
		}
		return result;
	}
}

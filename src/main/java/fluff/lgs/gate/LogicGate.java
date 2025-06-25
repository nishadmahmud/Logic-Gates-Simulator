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
	protected String[] inputLabels;
	protected String[] outputLabels;
	
	public LogicGate(IGateType type) {
		this.type = type;
		this.inputCount = type.getDefaultInputs();
		this.inputValues = new LogicalValue[type.getMaxInputs()];
		this.inputs = new ButtonConnection[type.getMaxInputs()];
		this.inputLabels = new String[inputCount];
		this.outputs = new ButtonConnection[type.getOutputs()];
		this.outputLabels = new String[type.getOutputs()];
		for (int i = 0; i < type.getMaxInputs(); i++) {
			this.inputs[i] = new ButtonConnection(ConnectionType.INPUT, i);
		}
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
	
	protected void setInputLabel(int index, String label) {
		if (index >= 0 && index < inputLabels.length) {
			inputLabels[index] = label;
		}
	}
	
	protected void setOutputLabel(int index, String label) {
		if (index >= 0 && index < outputLabels.length) {
			outputLabels[index] = label;
		}
	}
	
	public String getInputLabel(int index) {
		if (index >= 0 && index < inputLabels.length) {
			return inputLabels[index];
		}
		return null;
	}
	
	public String getOutputLabel(int index) {
		if (index >= 0 && index < outputLabels.length) {
			return outputLabels[index];
		}
		return null;
	}
	
	public LogicGate createCopy() {
		try {
			// Create new instance of the same gate type
			LogicGate copy = getClass().getDeclaredConstructor().newInstance();
			
			// Copy basic state
			copy.inputCount = this.inputCount;
			copy.inputValues = this.inputValues.clone();
			copy.inputLabels = this.inputLabels.clone();
			copy.outputLabels = this.outputLabels.clone();
			
			// The constructor already creates the input/output connections
			// We just need to set up the gate connections for outputs
			for (int i = 0; i < outputs.length; i++) {
				if (outputs[i] != null) {
					GateConnection gc = new GateConnection(copy, i);
					copy.outputs[i].from = gc;
				}
			}
			
			return copy;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

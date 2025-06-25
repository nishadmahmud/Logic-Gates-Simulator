package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class HalfAdderGate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"A", "B"};
    private static final String[] OUTPUT_LABELS = {"S", "C"};  // Sum and Carry
    
    public HalfAdderGate() {
        super(NativeGateType.HALF_ADDER);
        for (int i = 0; i < inputCount; i++) {
            setInputLabel(i, INPUT_LABELS[i]);
        }
        for (int i = 0; i < outputs.length; i++) {
            setOutputLabel(i, OUTPUT_LABELS[i]);
        }
    }
    
    @Override
    public LogicalValue[] getOutputs() {
        // Check if all inputs are connected
        int connectedCount = 0;
        for (int i = 0; i < inputCount; i++) {
            ButtonConnection connection = this.inputs[i];
            if (connection != null && connection.from != null) {
                connectedCount++;
            }
        }
        
        // If not all inputs are connected, return UNDEFINED
        if (connectedCount < inputCount) {
            return new LogicalValue[] { LogicalValue.UNDEFINED, LogicalValue.UNDEFINED };
        }
        
        // Get input values
        LogicalValue a = input(0);
        LogicalValue b = input(1);
        
        // Calculate Sum (XOR) and Carry (AND)
        LogicalValue sum = (a == LogicalValue.TRUE) ^ (b == LogicalValue.TRUE) ? 
            LogicalValue.TRUE : LogicalValue.FALSE;
        LogicalValue carry = (a == LogicalValue.TRUE) && (b == LogicalValue.TRUE) ? 
            LogicalValue.TRUE : LogicalValue.FALSE;
        
        return new LogicalValue[] { sum, carry };
    }
} 
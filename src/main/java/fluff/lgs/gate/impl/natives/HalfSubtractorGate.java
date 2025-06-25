package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class HalfSubtractorGate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"A", "B"};
    private static final String[] OUTPUT_LABELS = {"D", "Bout"};  // Difference and Borrow out
    
    public HalfSubtractorGate() {
        super(NativeGateType.HALF_SUBTRACTOR);
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
        LogicalValue a = input(0);  // Minuend
        LogicalValue b = input(1);  // Subtrahend
        
        // Calculate Difference (XOR) and Borrow (AND with NOT)
        LogicalValue difference = (a == LogicalValue.TRUE) ^ (b == LogicalValue.TRUE) ? 
            LogicalValue.TRUE : LogicalValue.FALSE;
        LogicalValue borrow = (a == LogicalValue.FALSE) && (b == LogicalValue.TRUE) ? 
            LogicalValue.TRUE : LogicalValue.FALSE;
        
        return new LogicalValue[] { difference, borrow };
    }
} 
package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class FullSubtractorGate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"A", "B", "Bin"};
    private static final String[] OUTPUT_LABELS = {"D", "Bout"};  // Difference and Borrow out
    
    public FullSubtractorGate() {
        super(NativeGateType.FULL_SUBTRACTOR);
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
        LogicalValue a = input(0);    // Minuend
        LogicalValue b = input(1);    // Subtrahend
        LogicalValue bin = input(2);  // Borrow In
        
        // Convert to boolean for easier calculation
        boolean aVal = a == LogicalValue.TRUE;
        boolean bVal = b == LogicalValue.TRUE;
        boolean binVal = bin == LogicalValue.TRUE;
        
        // Calculate Difference and Borrow Out
        boolean diff = aVal ^ bVal ^ binVal;
        boolean bout = (!aVal && bVal) || (!aVal && binVal) || (bVal && binVal);
        
        return new LogicalValue[] { 
            diff ? LogicalValue.TRUE : LogicalValue.FALSE,
            bout ? LogicalValue.TRUE : LogicalValue.FALSE 
        };
    }
} 
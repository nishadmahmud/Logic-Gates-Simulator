package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class FullAdderGate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"A", "B", "Cin"};
    private static final String[] OUTPUT_LABELS = {"S", "Cout"};  // Sum and Carry out
    
    public FullAdderGate() {
        super(NativeGateType.FULL_ADDER);
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
        
        // Get input values (A, B, Carry In)
        LogicalValue a = input(0);
        LogicalValue b = input(1);
        LogicalValue carryIn = input(2);
        
        // Count true inputs for XOR and carry calculations
        int trueCount = 0;
        if (a == LogicalValue.TRUE) trueCount++;
        if (b == LogicalValue.TRUE) trueCount++;
        if (carryIn == LogicalValue.TRUE) trueCount++;
        
        // Calculate Sum (XOR of all three inputs)
        LogicalValue sum = (trueCount % 2 == 1) ? LogicalValue.TRUE : LogicalValue.FALSE;
        
        // Calculate Carry Out (majority function - true if at least 2 inputs are true)
        LogicalValue carryOut = (trueCount >= 2) ? LogicalValue.TRUE : LogicalValue.FALSE;
        
        return new LogicalValue[] { sum, carryOut };
    }
} 
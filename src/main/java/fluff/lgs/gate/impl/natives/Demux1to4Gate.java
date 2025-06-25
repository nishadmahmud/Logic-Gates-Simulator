package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class Demux1to4Gate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"D", "S1", "S0"};
    private static final String[] OUTPUT_LABELS = {"Y0", "Y1", "Y2", "Y3"};
    
    public Demux1to4Gate() {
        super(NativeGateType.DEMUX_1TO4);
        // Set labels for inputs and outputs
        for (int i = 0; i < inputCount; i++) {
            setInputLabel(i, INPUT_LABELS[i]);
        }
        for (int i = 0; i < outputs.length; i++) {
            setOutputLabel(i, OUTPUT_LABELS[i]);
        }
    }
    
    @Override
    public LogicalValue[] getOutputs() {
        if (!areAllInputsConnected()) {
            return new LogicalValue[] { 
                LogicalValue.UNDEFINED,
                LogicalValue.UNDEFINED,
                LogicalValue.UNDEFINED,
                LogicalValue.UNDEFINED
            };
        }
        
        // Get input values
        boolean data = input(0) == LogicalValue.TRUE;   // Data input
        boolean s1 = input(1) == LogicalValue.TRUE;     // Select input 1
        boolean s0 = input(2) == LogicalValue.TRUE;     // Select input 0
        
        // Route data to one of four outputs based on S1S0:
        // 00 -> Y0
        // 01 -> Y1
        // 10 -> Y2
        // 11 -> Y3
        boolean y0 = data && !s1 && !s0;
        boolean y1 = data && !s1 && s0;
        boolean y2 = data && s1 && !s0;
        boolean y3 = data && s1 && s0;
        
        return new LogicalValue[] {
            y0 ? LogicalValue.TRUE : LogicalValue.FALSE,
            y1 ? LogicalValue.TRUE : LogicalValue.FALSE,
            y2 ? LogicalValue.TRUE : LogicalValue.FALSE,
            y3 ? LogicalValue.TRUE : LogicalValue.FALSE
        };
    }
    
    private boolean areAllInputsConnected() {
        for (int i = 0; i < inputCount; i++) {
            if (inputs[i] == null || inputs[i].from == null) {
                return false;
            }
        }
        return true;
    }
} 
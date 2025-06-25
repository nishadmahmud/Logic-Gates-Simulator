package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class Mux4to1Gate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"D0", "D1", "D2", "D3", "S1", "S0"};
    private static final String[] OUTPUT_LABELS = {"Y"};
    
    public Mux4to1Gate() {
        super(NativeGateType.MUX_4TO1);
        // Set labels for inputs and outputs
        for (int i = 0; i < inputCount; i++) {
            setInputLabel(i, INPUT_LABELS[i]);
        }
        setOutputLabel(0, OUTPUT_LABELS[0]);
    }
    
    @Override
    public LogicalValue[] getOutputs() {
        if (!areAllInputsConnected()) {
            return new LogicalValue[] { LogicalValue.UNDEFINED };
        }
        
        // Get input values
        boolean d0 = input(0) == LogicalValue.TRUE;  // Data input 0
        boolean d1 = input(1) == LogicalValue.TRUE;  // Data input 1
        boolean d2 = input(2) == LogicalValue.TRUE;  // Data input 2
        boolean d3 = input(3) == LogicalValue.TRUE;  // Data input 3
        boolean s1 = input(4) == LogicalValue.TRUE;  // Select input 1
        boolean s0 = input(5) == LogicalValue.TRUE;  // Select input 0
        
        // Select output based on S1S0:
        // 00 -> D0
        // 01 -> D1
        // 10 -> D2
        // 11 -> D3
        boolean output;
        if (!s1 && !s0) output = d0;
        else if (!s1 && s0) output = d1;
        else if (s1 && !s0) output = d2;
        else output = d3;
        
        return new LogicalValue[] { output ? LogicalValue.TRUE : LogicalValue.FALSE };
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
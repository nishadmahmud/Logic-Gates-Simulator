package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class Mux2to1Gate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"D0", "D1", "S"};
    private static final String[] OUTPUT_LABELS = {"Y"};
    
    public Mux2to1Gate() {
        super(NativeGateType.MUX_2TO1);
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
        boolean s = input(2) == LogicalValue.TRUE;   // Select input
        
        // Select between D0 and D1 based on S
        // If S=0, select D0; if S=1, select D1
        boolean output = s ? d1 : d0;
        
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
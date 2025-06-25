package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class Demux1to2Gate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"D", "S"};
    private static final String[] OUTPUT_LABELS = {"Y0", "Y1"};
    
    public Demux1to2Gate() {
        super(NativeGateType.DEMUX_1TO2);
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
                LogicalValue.UNDEFINED 
            };
        }
        
        // Get input values
        boolean data = input(0) == LogicalValue.TRUE;  // Data input
        boolean select = input(1) == LogicalValue.TRUE;  // Select input
        
        // Route data to Y0 when S=0, to Y1 when S=1
        boolean y0 = data && !select;
        boolean y1 = data && select;
        
        return new LogicalValue[] {
            y0 ? LogicalValue.TRUE : LogicalValue.FALSE,
            y1 ? LogicalValue.TRUE : LogicalValue.FALSE
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
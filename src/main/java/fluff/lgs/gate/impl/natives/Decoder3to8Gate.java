package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class Decoder3to8Gate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"A2", "A1", "A0"};
    private static final String[] OUTPUT_LABELS = {"Y0", "Y1", "Y2", "Y3", "Y4", "Y5", "Y6", "Y7"};

    public Decoder3to8Gate() {
        super(NativeGateType.DECODER_3TO8);
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
        
        if (connectedCount < inputCount) {
            return new LogicalValue[] { 
                LogicalValue.UNDEFINED, LogicalValue.UNDEFINED,
                LogicalValue.UNDEFINED, LogicalValue.UNDEFINED,
                LogicalValue.UNDEFINED, LogicalValue.UNDEFINED,
                LogicalValue.UNDEFINED, LogicalValue.UNDEFINED 
            };
        }
        
        // Get input values (A2 A1 A0)
        boolean a2 = input(0) == LogicalValue.TRUE;
        boolean a1 = input(1) == LogicalValue.TRUE;
        boolean a0 = input(2) == LogicalValue.TRUE;
        
        // Calculate outputs (Y0 to Y7)
        boolean[] outputs = new boolean[8];
        int index = (a2 ? 4 : 0) + (a1 ? 2 : 0) + (a0 ? 1 : 0);
        outputs[index] = true;
        
        return new LogicalValue[] { 
            outputs[0] ? LogicalValue.TRUE : LogicalValue.FALSE,
            outputs[1] ? LogicalValue.TRUE : LogicalValue.FALSE,
            outputs[2] ? LogicalValue.TRUE : LogicalValue.FALSE,
            outputs[3] ? LogicalValue.TRUE : LogicalValue.FALSE,
            outputs[4] ? LogicalValue.TRUE : LogicalValue.FALSE,
            outputs[5] ? LogicalValue.TRUE : LogicalValue.FALSE,
            outputs[6] ? LogicalValue.TRUE : LogicalValue.FALSE,
            outputs[7] ? LogicalValue.TRUE : LogicalValue.FALSE
        };
    }
} 
package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class Encoder8to3Gate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"I0", "I1", "I2", "I3", "I4", "I5", "I6", "I7"};
    private static final String[] OUTPUT_LABELS = {"Y2", "Y1", "Y0"};
    
    public Encoder8to3Gate() {
        super(NativeGateType.ENCODER_8TO3);
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
            return new LogicalValue[] { LogicalValue.UNDEFINED, LogicalValue.UNDEFINED, LogicalValue.UNDEFINED };
        }
        
        // Get input values (I0 to I7)
        boolean[] in = new boolean[8];
        for (int i = 0; i < 8; i++) {
            in[i] = input(i) == LogicalValue.TRUE;
        }
        
        // Calculate outputs
        // Y2 = I4 + I5 + I6 + I7
        // Y1 = I2 + I3 + I6 + I7
        // Y0 = I1 + I3 + I5 + I7
        boolean y2 = in[4] || in[5] || in[6] || in[7];
        boolean y1 = in[2] || in[3] || in[6] || in[7];
        boolean y0 = in[1] || in[3] || in[5] || in[7];
        
        return new LogicalValue[] { 
            y2 ? LogicalValue.TRUE : LogicalValue.FALSE,
            y1 ? LogicalValue.TRUE : LogicalValue.FALSE,
            y0 ? LogicalValue.TRUE : LogicalValue.FALSE 
        };
    }
} 
package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class Encoder4to2Gate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"I0", "I1", "I2", "I3"};
    private static final String[] OUTPUT_LABELS = {"Y1", "Y0"};
    
    public Encoder4to2Gate() {
        super(NativeGateType.ENCODER_4TO2);
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
            return new LogicalValue[] { LogicalValue.UNDEFINED, LogicalValue.UNDEFINED };
        }
        
        // Get input values (I0 to I3)
        boolean[] in = new boolean[4];
        for (int i = 0; i < 4; i++) {
            in[i] = input(i) == LogicalValue.TRUE;
        }
        
        // Calculate outputs
        // Y1 = I2 + I3
        // Y0 = I1 + I3
        boolean y1 = in[2] || in[3];
        boolean y0 = in[1] || in[3];
        
        return new LogicalValue[] { 
            y1 ? LogicalValue.TRUE : LogicalValue.FALSE,
            y0 ? LogicalValue.TRUE : LogicalValue.FALSE 
        };
    }
} 
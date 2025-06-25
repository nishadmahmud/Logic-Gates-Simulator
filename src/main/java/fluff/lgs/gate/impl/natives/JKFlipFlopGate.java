package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class JKFlipFlopGate extends LogicGate {
    private static final String[] INPUT_LABELS = {"J", "K", "CLK"};
    private static final String[] OUTPUT_LABELS = {"Q", "Q'"};
    
    private boolean lastState;
    private boolean lastClock;
    
    public JKFlipFlopGate() {
        super(NativeGateType.JK_FLIP_FLOP);
        for (int i = 0; i < inputCount; i++) {
            setInputLabel(i, INPUT_LABELS[i]);
        }
        for (int i = 0; i < outputs.length; i++) {
            setOutputLabel(i, OUTPUT_LABELS[i]);
        }
        lastState = false;
        lastClock = false;
    }
    
    @Override
    public LogicalValue[] getOutputs() {
        if (!areAllInputsConnected()) {
            return new LogicalValue[] { LogicalValue.UNDEFINED, LogicalValue.UNDEFINED };
        }
        
        boolean j = input(0) == LogicalValue.TRUE;    // J input
        boolean k = input(1) == LogicalValue.TRUE;    // K input
        boolean clk = input(2) == LogicalValue.TRUE;  // Clock input
        
        // Detect rising edge of clock
        if (clk && !lastClock) {
            if (j && k) {
                lastState = !lastState;  // Toggle
            } else if (j) {
                lastState = true;        // Set
            } else if (k) {
                lastState = false;       // Reset
            }
            // If both J and K are 0, maintain state
        }
        lastClock = clk;
        
        return new LogicalValue[] {
            lastState ? LogicalValue.TRUE : LogicalValue.FALSE,     // Q
            !lastState ? LogicalValue.TRUE : LogicalValue.FALSE     // Q'
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
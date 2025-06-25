package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class TFlipFlopGate extends LogicGate {
    private static final String[] INPUT_LABELS = {"T", "CLK"};
    private static final String[] OUTPUT_LABELS = {"Q", "Q'"};
    
    private boolean lastState;
    private boolean lastClock;
    
    public TFlipFlopGate() {
        super(NativeGateType.T_FLIP_FLOP);
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
        
        boolean t = input(0) == LogicalValue.TRUE;    // T input
        boolean clk = input(1) == LogicalValue.TRUE;  // Clock input
        
        // Detect rising edge of clock
        if (clk && !lastClock) {
            if (t) {
                lastState = !lastState;  // Toggle if T is 1
            }
            // If T is 0, maintain state
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
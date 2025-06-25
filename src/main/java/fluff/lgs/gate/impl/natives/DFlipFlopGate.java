package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class DFlipFlopGate extends LogicGate {
    private static final String[] INPUT_LABELS = {"D", "CLK"};
    private static final String[] OUTPUT_LABELS = {"Q", "Q'"};
    
    private boolean lastState;
    private boolean lastClock;
    
    public DFlipFlopGate() {
        super(NativeGateType.D_FLIP_FLOP);
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
        
        boolean d = input(0) == LogicalValue.TRUE;    // D input
        boolean clk = input(1) == LogicalValue.TRUE;  // Clock input
        
        // Detect rising edge of clock
        if (clk && !lastClock) {
            lastState = d;  // Update state on rising edge
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
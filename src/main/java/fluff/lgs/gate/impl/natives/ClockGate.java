package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;

public class ClockGate extends LogicGate {
    private static final long PULSE_INTERVAL = 500; // 500ms = 0.5 seconds
    private long lastToggleTime;
    private boolean currentState;
    
    public ClockGate() {
        super(NativeGateType.CLOCK);
        this.lastToggleTime = System.currentTimeMillis();
        this.currentState = false;
    }
    
    @Override
    public LogicalValue[] getOutputs() {
        long currentTime = System.currentTimeMillis();
        
        // Check if enough time has passed to toggle the state
        if (currentTime - lastToggleTime >= PULSE_INTERVAL) {
            currentState = !currentState;
            lastToggleTime = currentTime;
        }
        
        return new LogicalValue[] { 
            currentState ? LogicalValue.TRUE : LogicalValue.FALSE 
        };
    }
} 
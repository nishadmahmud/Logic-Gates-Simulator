package fluff.lgs.gate.impl.natives;

import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class Comparator1BitGate extends LogicGate {
    
    private static final String[] INPUT_LABELS = {"A", "B"};
    private static final String[] OUTPUT_LABELS = {"A>B", "A=B", "A<B"};
    
    public Comparator1BitGate() {
        super(NativeGateType.COMPARATOR_1BIT);
        // Set labels for inputs and outputs
        for (int i = 0; i < inputCount; i++) {
            setInputLabel(i, INPUT_LABELS[i]);
        }
        for (int i = 0; i < outputs.length; i++) {
            setOutputLabel(i, OUTPUT_LABELS[i]);
        }
    }
    
    private boolean areAllInputsConnected() {
        for (int i = 0; i < inputCount; i++) {
            ButtonConnection connection = this.inputs[i];
            if (connection == null || connection.from == null) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public LogicalValue[] getOutputs() {
        // Check if all inputs are connected
        if (!areAllInputsConnected()) {
            return new LogicalValue[] { 
                LogicalValue.UNDEFINED,
                LogicalValue.UNDEFINED,
                LogicalValue.UNDEFINED 
            };
        }
        
        // Get input values (A and B)
        boolean a = input(0) == LogicalValue.TRUE;
        boolean b = input(1) == LogicalValue.TRUE;
        
        // Compare A and B
        return new LogicalValue[] {
            a && !b ? LogicalValue.TRUE : LogicalValue.FALSE,  // A > B
            a == b ? LogicalValue.TRUE : LogicalValue.FALSE,   // A = B
            !a && b ? LogicalValue.TRUE : LogicalValue.FALSE   // A < B
        };
    }
} 
package fluff.lgs.clipboard;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.connection.Link;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class CircuitClipboard {
    private static CircuitClipboard instance;
    private List<GateWindow> copiedGates;
    private Map<ButtonConnection, ButtonConnection> connectionMap;
    
    private CircuitClipboard() {
        copiedGates = new ArrayList<>();
        connectionMap = new HashMap<>();
    }
    
    public static CircuitClipboard getInstance() {
        if (instance == null) {
            instance = new CircuitClipboard();
        }
        return instance;
    }
    
    public void copyGates(List<GateWindow> gates) {
        copiedGates.clear();
        connectionMap.clear();
        
        // First pass: Create copies of all gates
        Map<GateWindow, GateWindow> gateMap = new HashMap<>();
        for (GateWindow original : gates) {
            GateWindow copy = original.createCopy();
            copiedGates.add(copy);
            gateMap.put(original, copy);
        }
        
        // Second pass: Restore connections between copied gates
        for (GateWindow original : gates) {
            GateWindow copy = gateMap.get(original);
            if (copy != null && copy.gate != null) {
                // Copy input connections
                for (int i = 0; i < original.gate.inputs.length; i++) {
                    ButtonConnection origInput = original.gate.inputs[i];
                    if (origInput != null && origInput.from != null) {
                        // Find the source gate and output that connects to this input
                        for (GateWindow sourceGate : gates) {
                            if (sourceGate.gate != null) {
                                for (int j = 0; j < sourceGate.gate.outputs.length; j++) {
                                    ButtonConnection sourceOutput = sourceGate.gate.outputs[j];
                                    if (sourceOutput == origInput.from) {
                                        // Found the connection - connect the copies
                                        GateWindow sourceCopy = gateMap.get(sourceGate);
                                        if (sourceCopy != null && sourceCopy.gate != null) {
                                            copy.gate.inputs[i].from = sourceCopy.gate.outputs[j];
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public List<GateWindow> pasteGates(int offsetX, int offsetY) {
        List<GateWindow> pastedGates = new ArrayList<>();
        Map<GateWindow, GateWindow> gateMap = new HashMap<>();
        
        // First create all gates
        for (GateWindow copied : copiedGates) {
            GateWindow pasted = copied.createCopy();
            pasted.x += offsetX;
            pasted.y += offsetY;
            pastedGates.add(pasted);
            gateMap.put(copied, pasted);
        }
        
        // Then restore connections between pasted gates
        for (GateWindow copied : copiedGates) {
            GateWindow pasted = gateMap.get(copied);
            if (pasted != null && pasted.gate != null) {
                for (int i = 0; i < copied.gate.inputs.length; i++) {
                    ButtonConnection copiedInput = copied.gate.inputs[i];
                    if (copiedInput != null && copiedInput.from != null) {
                        // Find the source gate in our copied set
                        for (GateWindow sourceCopied : copiedGates) {
                            if (sourceCopied.gate != null) {
                                for (int j = 0; j < sourceCopied.gate.outputs.length; j++) {
                                    if (sourceCopied.gate.outputs[j] == copiedInput.from) {
                                        // Connect the pasted gates
                                        GateWindow sourcePasted = gateMap.get(sourceCopied);
                                        if (sourcePasted != null) {
                                            pasted.gate.inputs[i].from = sourcePasted.gate.outputs[j];
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return pastedGates;
    }
} 
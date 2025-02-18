package fluff.lgs.gui.screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import fluff.lgs.LGS;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.impl.InputGate;
import fluff.lgs.gate.impl.OutputGate;
import fluff.lgs.gate.impl.natives.AndGate;
import fluff.lgs.gate.impl.natives.OrGate;
import fluff.lgs.gate.impl.natives.NandGate;
import fluff.lgs.gate.impl.natives.NorGate;
import fluff.lgs.gate.impl.natives.XorGate;
import fluff.lgs.gate.impl.natives.XnorGate;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.gui.elements.gate.GateOutputLabel;
import fluff.lgs.gui.elements.ToggleButton;
import fluff.lgs.gui.Element;
import fluff.lgs.gate.IGateType;
import fluff.lgs.gui.elements.gate.ButtonConnection;
import fluff.lgs.gate.connection.ConnectionType;
import fluff.lgs.gate.connection.IConnection;

public class TimingDiagramScreen extends JFrame {
    private static TimingDiagramScreen instance;
    private Timer updateTimer;
    private JPanel diagramPanel;
    private final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private final Color SIGNAL_COLOR = new Color(0, 255, 0);
    private int TIME_STEPS;  // Will be set based on input count (2^n)
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 360;  // Reduced from 600
    private final int SIGNAL_HEIGHT = 40;  // Keep original value
    private final int SIGNAL_SPACING = 60;  // Keep original value
    
    private List<Boolean[]> inputHistory;
    private List<Boolean[]> outputHistory;
    private List<String> inputNames;
    private List<String> outputNames;

    public static void showWindow() {
        SwingUtilities.invokeLater(() -> {
            if (instance != null) {
                instance.dispose();
            }
            instance = new TimingDiagramScreen();
        });
    }

    public TimingDiagramScreen() {
        super("Timing Diagram");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Set icons to match main window
        List<Image> icons = new ArrayList<>();
        try {
            icons.add(new ImageIcon(getClass().getResource("/assets/icon_32.png")).getImage());
            icons.add(new ImageIcon(getClass().getResource("/assets/icon_64.png")).getImage());
            setIconImages(icons);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Position window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width - getWidth() - 50, 100);
        
        // Initialize data structures
        inputHistory = new ArrayList<>();
        outputHistory = new ArrayList<>();
        inputNames = new ArrayList<>();
        outputNames = new ArrayList<>();
        
        // Initialize with default TIME_STEPS
        TIME_STEPS = 0;  // Will be set in updateDiagram
        
        // Setup UI
        setupUI();
        
        // Start update timer
        updateTimer = new Timer(100, e -> updateDiagram());
        updateTimer.start();
        
        // Add window listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
            }
        });

        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setVisible(true);
    }

    private void setupUI() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        
        diagramPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTimingDiagram(g);
            }
        };
        diagramPanel.setBackground(BACKGROUND_COLOR);
        
        // Keep original padding
        diagramPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        setContentPane(diagramPanel);
        pack();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void drawTimingDiagram(Graphics g) {
        // Don't draw if TIME_STEPS is not initialized
        if (TIME_STEPS == 0) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw equations at top with less spacing
        String formula = buildFormula();
        g2d.setColor(Color.WHITE);
        String[] equations = formula.split("\n");
        int y = 15;
        for (String equation : equations) {
            g2d.drawString(equation, 10, y);
            y += 20;  // Keep original spacing
        }
        
        int labelWidth = 100;
        int timeStepWidth = (getWidth() - labelWidth - 50) / TIME_STEPS;
        
        // Draw vertical grid lines
        g2d.setColor(new Color(60, 60, 60));  // Darker color for grid
        for (int i = 0; i <= TIME_STEPS; i++) {
            int x = labelWidth + i * timeStepWidth;
            g2d.drawLine(x, 30, x, getHeight() - 30);
        }
        
        // Draw input signals
        g2d.setColor(SIGNAL_COLOR);
        for (int i = 0; i < inputNames.size(); i++) {
            // Draw signal name and value markers
            g2d.drawString(inputNames.get(i), 10, y + SIGNAL_HEIGHT/2);
            g2d.drawString("1", labelWidth - 20, y + 15);
            g2d.drawString("0", labelWidth - 20, y + SIGNAL_HEIGHT - 5);
            
            // Draw signal
            if (i < inputHistory.size()) {
                drawSignal(g2d, inputHistory.get(i), labelWidth, y, timeStepWidth);
            }
            
            y += SIGNAL_SPACING;
        }
        
        // Draw output signals
        g2d.setColor(Color.YELLOW);
        for (int i = 0; i < outputNames.size(); i++) {
            // Draw signal name and value markers
            g2d.drawString(outputNames.get(i), 10, y + SIGNAL_HEIGHT/2);
            g2d.drawString("1", labelWidth - 20, y + 15);
            g2d.drawString("0", labelWidth - 20, y + SIGNAL_HEIGHT - 5);
            
            // Draw signal
            if (i < outputHistory.size()) {
                drawSignal(g2d, outputHistory.get(i), labelWidth, y, timeStepWidth);
            }
            
            y += SIGNAL_SPACING;
        }
    }

    private String buildFormula() {
        List<GateWindow> outputGates = new ArrayList<>();
        
        // Collect output gates
        for (Element element : LGS.world().gates.list) {
            if (element instanceof GateWindow gw && gw.gate != null 
                && gw.gate.type == NativeGateType.OUTPUT) {
                outputGates.add(gw);
            }
        }
        
        if (outputGates.isEmpty()) return "";
        
        // Sort outputs by name for consistent display
        outputGates.sort((a, b) -> a.title.compareTo(b.title));
        
        // Build equations for all outputs
        StringBuilder equations = new StringBuilder();
        for (int i = 0; i < outputGates.size(); i++) {
            GateWindow outputGate = outputGates.get(i);
            String equation = EquationScreen.generateEquation(outputGate);
            // Format as "X = A + B"
            equations.append(outputGate.title.substring(0, 1))  // Just use first letter
                     .append(" = ")
                     .append(equation);
            
            // Add newline between equations
            if (i < outputGates.size() - 1) {
                equations.append("\n");
            }
        }
        
        return equations.toString();
    }

    private void drawSignal(Graphics2D g2d, Boolean[] values, int startX, int y, int timeStepWidth) {
        // Safety check for null values
        if (values == null) return;
        
        g2d.setStroke(new BasicStroke(2));
        
        int highY = y + 10;
        int lowY = y + SIGNAL_HEIGHT - 10;
        
        for (int i = 0; i < values.length; i++) {
            // Skip if value is null
            if (values[i] == null) continue;
            
            int x = startX + i * timeStepWidth;
            int nextX = startX + (i + 1) * timeStepWidth;
            
            if (i > 0 && values[i-1] != null) {
                int prevY = values[i-1] ? highY : lowY;
                int currentY = values[i] ? highY : lowY;
                g2d.drawLine(x, prevY, x, currentY);
            }
            
            int currentY = values[i] ? highY : lowY;
            g2d.drawLine(x, currentY, nextX, currentY);
        }
    }

    private void updateDiagram() {
        List<GateWindow> inputGates = new ArrayList<>();
        List<GateWindow> outputGates = new ArrayList<>();
        
        // Collect gates
        for (Element element : LGS.world().gates.list) {
            if (element instanceof GateWindow gw && gw.gate != null) {
                if (gw.gate.type == NativeGateType.INPUT) {
                    inputGates.add(gw);
                } else if (gw.gate.type == NativeGateType.OUTPUT) {
                    outputGates.add(gw);
                }
            }
        }
        
        if (!inputGates.isEmpty() && !outputGates.isEmpty()) {
            // Update TIME_STEPS based on input count (2^n)
            TIME_STEPS = 1 << inputGates.size();  // 2^n
            
            // Sort gates by name
            inputGates.sort((a, b) -> a.title.compareTo(b.title));
            outputGates.sort((a, b) -> a.title.compareTo(b.title));
            
            // Update names and reinitialize histories if input count changed
            if (inputNames.isEmpty() || inputNames.size() != inputGates.size()) {
                inputNames = inputGates.stream().map(gw -> gw.title).toList();
                outputNames = outputGates.stream().map(gw -> gw.title).toList();
                inputHistory.clear();  // Clear histories to force reinitialization
                outputHistory.clear();
            }
            
            // Update histories
            updateHistories(inputGates, outputGates);
            
            // Repaint
            diagramPanel.repaint();
        }
    }

    private void updateHistories(List<GateWindow> inputGates, List<GateWindow> outputGates) {
        // Initialize histories if needed
        if (inputHistory.isEmpty()) {
            // Initialize input patterns
            for (int i = 0; i < inputGates.size(); i++) {
                Boolean[] history = new Boolean[TIME_STEPS];
                for (int t = 0; t < TIME_STEPS; t++) {
                    history[t] = ((t >> (inputGates.size() - 1 - i)) & 1) == 1;
                }
                inputHistory.add(history);
            }

            // Initialize output histories
            outputHistory.clear();
            for (GateWindow outputGate : outputGates) {
                Boolean[] history = new Boolean[TIME_STEPS];
                
                // Get the gate that feeds into this output
                List<GateWindow> sourceGates = EquationScreen.getInputConnections(outputGate);
                if (!sourceGates.isEmpty()) {
                    GateWindow sourceGate = sourceGates.get(0);
                    
                    // For each time step
                    for (int t = 0; t < TIME_STEPS; t++) {
                        // Find which input gates are connected to this gate's inputs
                        Map<Integer, Integer> inputConnections = new HashMap<>();
                        for (int i = 0; i < sourceGate.gate.getInputCount(); i++) {
                            ButtonConnection input = sourceGate.gate.inputs[i];
                            for (int j = 0; j < inputGates.size(); j++) {
                                if (input != null && input.from != null && 
                                    input.from == inputGates.get(j).gate.outputs[0]) {
                                    inputConnections.put(i, j);
                                    break;
                                }
                            }
                        }

                        // Set up input values for this time step
                        LogicalValue[] gateInputs = new LogicalValue[sourceGate.gate.getInputCount()];
                        // Initialize all inputs to FALSE first
                        for (int i = 0; i < gateInputs.length; i++) {
                            gateInputs[i] = LogicalValue.FALSE;
                        }
                        // Then set the connected inputs
                        for (Map.Entry<Integer, Integer> conn : inputConnections.entrySet()) {
                            gateInputs[conn.getKey()] = inputHistory.get(conn.getValue())[t] ? 
                                LogicalValue.TRUE : LogicalValue.FALSE;
                        }

                        // Evaluate the gate based on its type
                        boolean output = false;
                        
                        // First check if all required inputs are connected
                        boolean allInputsConnected = true;
                        for (int i = 0; i < sourceGate.gate.getInputCount(); i++) {
                            ButtonConnection input = sourceGate.gate.inputs[i];
                            if (input == null || input.from == null) {
                                allInputsConnected = false;
                                break;
                            }
                        }

                        if (!allInputsConnected) {
                            history[t] = null;  // Use null to represent undefined
                            continue;
                        }

                        if (sourceGate.gate instanceof AndGate) {
                            output = true;
                            for (LogicalValue input : gateInputs) {
                                if (input != LogicalValue.TRUE) {
                                    output = false;
                                    break;
                                }
                            }
                        } else if (sourceGate.gate instanceof OrGate) {
                            output = false;
                            for (LogicalValue input : gateInputs) {
                                if (input == LogicalValue.TRUE) {
                                    output = true;
                                    break;
                                }
                            }
                        } else if (sourceGate.gate instanceof NandGate) {
                            output = true;
                            boolean allTrue = true;
                            for (LogicalValue input : gateInputs) {
                                if (input != LogicalValue.TRUE) {
                                    allTrue = false;
                                    break;
                                }
                            }
                            output = !allTrue;
                        } else if (sourceGate.gate instanceof NorGate) {
                            output = true;
                            for (LogicalValue input : gateInputs) {
                                if (input == LogicalValue.TRUE) {
                                    output = false;
                                    break;
                                }
                            }
                        } else if (sourceGate.gate instanceof XorGate) {
                            int trueCount = 0;
                            for (LogicalValue input : gateInputs) {
                                if (input == LogicalValue.TRUE) {
                                    trueCount++;
                                }
                            }
                            output = (trueCount % 2) == 1;
                        } else if (sourceGate.gate instanceof XnorGate) {
                            int trueCount = 0;
                            for (LogicalValue input : gateInputs) {
                                if (input == LogicalValue.TRUE) {
                                    trueCount++;
                                }
                            }
                            output = (trueCount % 2) == 0;
                        }

                        history[t] = output;
                    }
                }
                outputHistory.add(history);
            }
        }
    }

    private boolean evaluateEquation(String equation, List<GateWindow> inputGates, LogicalValue[] inputs) {
        Map<String, Boolean> assignments = new HashMap<>();
        for (int i = 0; i < inputGates.size(); i++) {
            assignments.put(inputGates.get(i).title, inputs[i] == LogicalValue.TRUE);
        }
        return EquationScreen.evaluateEquation(equation, assignments);
    }

    private void cleanup() {
        SwingUtilities.invokeLater(() -> {
            if (updateTimer != null) {
                updateTimer.stop();
            }
            instance = null;
        });
    }

    @Override
    public void dispose() {
        cleanup();
        super.dispose();
    }
} 
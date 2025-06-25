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
    private static final int TIME_STEPS = 20;  // Show last 20 states
    private static final int UPDATE_INTERVAL = 100;  // Update every 100ms
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 360;  // Reduced from 600
    private final int SIGNAL_HEIGHT = 40;  // Keep original value
    private final int SIGNAL_SPACING = 60;  // Keep original value
    
    private Map<GateWindow, List<LogicalValue>> inputHistory = new HashMap<>();
    private Map<GateWindow, List<LogicalValue>> outputHistory = new HashMap<>();
    private List<String> inputNames;
    private List<String> outputNames;
    private GateWindow gateWindow;

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
        inputNames = new ArrayList<>();
        outputNames = new ArrayList<>();
        
        // Setup UI
        setupUI();
        
        // Start update timer
        updateTimer = new Timer(UPDATE_INTERVAL, e -> updateDiagram());
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int labelWidth = 100;
        int timeStepWidth = (getWidth() - labelWidth - 50) / TIME_STEPS;
        int y = 50;  // Starting y position
        
        // Draw grid
        g2d.setColor(new Color(60, 60, 60));
        for (int i = 0; i <= TIME_STEPS; i++) {
            int x = labelWidth + i * timeStepWidth;
            g2d.drawLine(x, 30, x, getHeight() - 30);
        }
        
        // Check if we're dealing with an encoder
        boolean isEncoder = false;
        GateWindow sourceGate = null;
        for (GateWindow output : outputHistory.keySet()) {
            ButtonConnection input = output.gate.inputs[0];
            if (input != null && input.from instanceof ButtonConnection fromButton 
                && fromButton.parent instanceof GateWindow gw) {
                if (gw.gate.type == NativeGateType.ENCODER_4TO2 
                    || gw.gate.type == NativeGateType.ENCODER_8TO3) {
                    isEncoder = true;
                    sourceGate = gw;
                    break;
                }
            }
        }
        
        if (isEncoder && sourceGate != null) {
            // Draw encoder-specific signals
            drawEncoderSignals(g2d, sourceGate, labelWidth, timeStepWidth);
        } else {
            // Draw regular signals
            drawRegularSignals(g2d, labelWidth, timeStepWidth);
        }
    }

    private void drawEncoderSignals(Graphics2D g2d, GateWindow encoder, int labelWidth, int timeStepWidth) {
        int y = 50;
        g2d.setColor(SIGNAL_COLOR);
        
        // Draw input signals with "don't care" states
        int inputCount = encoder.gate.type == NativeGateType.ENCODER_4TO2 ? 4 : 8;
        for (int i = 0; i < inputCount; i++) {
            String name = "I" + i;
            g2d.drawString(name, 10, y + SIGNAL_HEIGHT/2);
            
            // Draw signal with "don't care" regions
            if (inputHistory.containsKey(encoder)) {
                List<LogicalValue> history = inputHistory.get(encoder);
                if (history != null) {
                    drawEncoderInputSignal(g2d, history, i, labelWidth, y, timeStepWidth);
                }
            }
            y += SIGNAL_SPACING;
        }
        
        // Draw output signals
        int outputCount = encoder.gate.type == NativeGateType.ENCODER_4TO2 ? 2 : 3;
        for (int i = 0; i < outputCount; i++) {
            String name = "Y" + i;
            g2d.drawString(name, 10, y + SIGNAL_HEIGHT/2);
            
            // Draw output signal
            if (outputHistory.containsKey(encoder)) {
                List<LogicalValue> history = outputHistory.get(encoder);
                if (history != null) {
                    drawSignal(g2d, history, labelWidth, y, timeStepWidth);
                }
            }
            y += SIGNAL_SPACING;
        }
    }

    private void drawEncoderInputSignal(Graphics2D g2d, List<LogicalValue> history, int inputIndex, 
                                      int labelWidth, int y, int timeStepWidth) {
        // Draw signal with "don't care" regions based on encoder truth table
        // Implementation depends on your specific requirements for visualizing "don't care" states
        // You might want to use a different color or pattern for "don't care" regions
    }

    private void drawRegularSignals(Graphics2D g2d, int labelWidth, int timeStepWidth) {
        int y = 50;  // Add this line to define y
        
        // Draw input signals
        g2d.setColor(SIGNAL_COLOR);
        for (int i = 0; i < inputNames.size(); i++) {
            String name = inputNames.get(i);
            GateWindow inputGate = findGateByName(name);
            if (inputGate != null) {
                // Draw signal name
                g2d.drawString(name, 10, y + SIGNAL_HEIGHT/2);
                
                // Get current state
                LogicalValue currentState = LogicalValue.UNDEFINED;
                List<LogicalValue> history = inputHistory.get(inputGate);
                if (history != null && !history.isEmpty()) {
                    currentState = history.get(history.size() - 1);
                }
                
                // Draw state label
                String stateLabel = getStateLabel(currentState);
                g2d.drawString(stateLabel, labelWidth - 25, y + SIGNAL_HEIGHT/2);
            
                // Draw signal
                if (history != null) {
                    drawSignal(g2d, history, labelWidth, y, timeStepWidth);
                }
                
                y += SIGNAL_SPACING;
            }
        }
        
        // Draw output signals
        g2d.setColor(Color.YELLOW);
        for (int i = 0; i < outputNames.size(); i++) {
            String name = outputNames.get(i);
            GateWindow outputGate = findGateByName(name);
            if (outputGate != null) {
                // Draw signal name
                g2d.drawString(name, 10, y + SIGNAL_HEIGHT/2);
                
                // Get current state
                LogicalValue currentState = LogicalValue.UNDEFINED;
                List<LogicalValue> history = outputHistory.get(outputGate);
                if (history != null && !history.isEmpty()) {
                    currentState = history.get(history.size() - 1);
                }
                
                // Draw state label
                String stateLabel = getStateLabel(currentState);
                g2d.drawString(stateLabel, labelWidth - 25, y + SIGNAL_HEIGHT/2);
            
                // Draw signal
                if (history != null) {
                    drawSignal(g2d, history, labelWidth, y, timeStepWidth);
                }
                
                y += SIGNAL_SPACING;
            }
        }
    }

    private String getStateLabel(LogicalValue state) {
        switch (state) {
            case TRUE: return "1";
            case FALSE: return "0";
            default: return "?";  // for UNDEFINED
        }
    }

    private void drawSignal(Graphics2D g2d, List<LogicalValue> values, int startX, int y, int timeStepWidth) {
        if (values == null || values.isEmpty()) return;
        
        g2d.setStroke(new BasicStroke(2));
        
        int highY = y + 10;
        int lowY = y + SIGNAL_HEIGHT - 10;
        
        for (int i = 0; i < values.size(); i++) {
            LogicalValue value = values.get(i);
            if (value == null) continue;
            
            int x = startX + i * timeStepWidth;
            int nextX = startX + (i + 1) * timeStepWidth;
            
            // Draw vertical line if value changed
            if (i > 0 && values.get(i-1) != null) {
                int prevY = values.get(i-1) == LogicalValue.TRUE ? highY : lowY;
                int currentY = value == LogicalValue.TRUE ? highY : lowY;
                if (prevY != currentY) {
                g2d.drawLine(x, prevY, x, currentY);
                }
            }
            
            // Draw horizontal line
            int currentY = value == LogicalValue.TRUE ? highY : lowY;
            g2d.drawLine(x, currentY, nextX, currentY);
            
            // Draw undefined state if value is UNDEFINED
            if (value == LogicalValue.UNDEFINED) {
                int midY = (highY + lowY) / 2;
                g2d.setColor(Color.RED);
                g2d.drawLine(x, midY - 5, nextX, midY + 5);
                g2d.drawLine(x, midY + 5, nextX, midY - 5);
                g2d.setColor(SIGNAL_COLOR);
            }
        }
    }

    private void updateDiagram() {
        List<GateWindow> inputGates = new ArrayList<>();
        List<GateWindow> outputGates = new ArrayList<>();
        
        // Collect gates
        for (Element element : LGS.world().gates.list) {
            if (element instanceof GateWindow gw && gw.gate != null) {
                // Include both INPUT and CLOCK types as inputs
                if (gw.gate.type == NativeGateType.INPUT || gw.gate.type == NativeGateType.CLOCK) {
                    inputGates.add(gw);
                } else if (gw.gate.type == NativeGateType.OUTPUT) {
                    outputGates.add(gw);
                }
            }
        }
        
        if (!inputGates.isEmpty() && !outputGates.isEmpty()) {
            // Sort gates by name for consistent display
            inputGates.sort((a, b) -> a.title.compareTo(b.title));
            outputGates.sort((a, b) -> a.title.compareTo(b.title));
            
            // Update input histories
            for (GateWindow inputGate : inputGates) {
                try {
                    List<LogicalValue> history = inputHistory.computeIfAbsent(inputGate, k -> new ArrayList<>());
                    
                    // Get current input value with null check
                    LogicalValue[] outputs = inputGate.gate.getOutputs();
                    LogicalValue currentValue = (outputs != null && outputs.length > 0) ? 
                        outputs[0] : LogicalValue.UNDEFINED;
                    
                    if (history.size() >= TIME_STEPS) {
                        history.remove(history.size() - 1);  // Remove rightmost value
                    }
                    history.add(0, currentValue);  // Add new value at the start (left)
                } catch (Exception e) {
                    // Skip this gate if there's an error
                    continue;
                }
            }
            
            // Update output histories
            for (GateWindow outputGate : outputGates) {
                try {
                    List<LogicalValue> history = outputHistory.computeIfAbsent(outputGate, k -> new ArrayList<>());
                    
                    // Get current output value by checking the input connection
                    LogicalValue currentValue = LogicalValue.UNDEFINED;
                    
                    // Get the input connection to this output gate
                    if (outputGate.gate.inputs != null && outputGate.gate.inputs.length > 0) {
                        ButtonConnection input = outputGate.gate.inputs[0];
                        if (input != null && input.from != null) {
                            // Get the gate that's connected to this output
                            GateWindow sourceGate = null;
                            if (input.from instanceof ButtonConnection) {
                                ButtonConnection fromButton = (ButtonConnection) input.from;
                                if (fromButton.parent instanceof GateWindow) {
                                    sourceGate = (GateWindow) fromButton.parent;
                                }
                            }
                            
                            if (sourceGate != null && sourceGate.gate != null) {
                                // Get the output index that's connected to this input
                                int outputIndex = -1;
                                for (int i = 0; i < sourceGate.gate.outputs.length; i++) {
                                    if (sourceGate.gate.outputs[i] == input.from) {
                                        outputIndex = i;
                                break;
                            }
                        }

                                if (outputIndex >= 0) {
                                    LogicalValue[] sourceOutputs = sourceGate.gate.getOutputs();
                                    if (sourceOutputs != null && outputIndex < sourceOutputs.length) {
                                        currentValue = sourceOutputs[outputIndex];
                                    }
                                }
                            }
                        }
                    }
                    
                    if (history.size() >= TIME_STEPS) {
                        history.remove(history.size() - 1);  // Remove rightmost value
                    }
                    history.add(0, currentValue);  // Add new value at the start (left)
                } catch (Exception e) {
                    // Skip this gate if there's an error
                    continue;
                }
            }
            
            // Update names if needed
            inputNames = inputGates.stream().map(gw -> gw.title).toList();
            outputNames = outputGates.stream().map(gw -> gw.title).toList();
            
            // Repaint
            diagramPanel.repaint();
        }
    }

    private GateWindow findGateByName(String name) {
        for (Element element : LGS.world().gates.list) {
            if (element instanceof GateWindow gw && gw.title.equals(name)) {
                return gw;
            }
        }
        return null;
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
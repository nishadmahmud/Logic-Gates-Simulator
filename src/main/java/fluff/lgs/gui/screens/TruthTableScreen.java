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
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.gui.elements.gate.GateOutputLabel;
import fluff.lgs.gui.elements.ToggleButton;
import fluff.lgs.gui.Element;
import org.newdawn.slick.util.ResourceLoader;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.DefaultStyledDocument;

public class TruthTableScreen extends JFrame {
    private JTextPane truthTableArea;
    private static TruthTableScreen instance;
    private Timer updateTimer;
    private final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private final Color TEXT_COLOR = new Color(200, 200, 200);
    private final Color HIGHLIGHT_COLOR = new Color(60, 60, 80);

    public static void showWindow() {
        SwingUtilities.invokeLater(() -> {
            if (instance != null) {
                instance.dispose();
            }
            instance = new TruthTableScreen();
        });
    }

    private TruthTableScreen() {
        setTitle("Truth Table");
        setSize(500, 400);
        setMinimumSize(new Dimension(300, 200));
        
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
        setLocation(screenSize.width - getWidth() - 50, 50);
        
        setupUI();
        
        // Add window listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
            }
        });

        setAlwaysOnTop(true);
        setFocusableWindowState(false);

        // Start update timer - use a longer interval to reduce CPU usage
        updateTimer = new Timer(500, e -> {
            SwingUtilities.invokeLater(this::updateTruthTable);
        });
        updateTimer.start();

        // Generate initial table on EDT
        SwingUtilities.invokeLater(() -> {
            generateInitialTable();
            setVisible(true);
        });
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Use monospace font for truth table
        truthTableArea = new JTextPane();
        truthTableArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        
        truthTableArea.setEditable(false);
        truthTableArea.setBackground(new Color(40, 40, 40));
        truthTableArea.setForeground(TEXT_COLOR);
        truthTableArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(truthTableArea);
        scrollPane.setBackground(new Color(40, 40, 40));
        scrollPane.getViewport().setBackground(new Color(40, 40, 40));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void generateInitialTable() {
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

        if (inputGates.isEmpty() || outputGates.isEmpty()) {
            truthTableArea.setText("Add input and output gates");
            return;
        }

        // Sort gates by name
        inputGates.sort((a, b) -> a.title.compareTo(b.title));
        outputGates.sort((a, b) -> a.title.compareTo(b.title));

        updateTruthTable();
        setVisible(true);
    }

    private void updateTruthTable() {
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
            // Sort gates by name
            inputGates.sort((a, b) -> a.title.compareTo(b.title));
            outputGates.sort((a, b) -> a.title.compareTo(b.title));
            
            // Get current input states
            int currentState = 0;
            for (int i = 0; i < inputGates.size(); i++) {
                GateWindow gw = inputGates.get(i);
                if (gw.gate instanceof InputGate inputGate) {
                    ToggleButton toggle = (ToggleButton)inputGate.value;
                    if (toggle.value.get()) {
                        currentState |= (1 << (inputGates.size() - 1 - i));
                    }
                }
            }
            
            // Generate table with highlighting
            generateTableWithHighlight(inputGates, outputGates, currentState);
        }
    }

    private void generateTableWithHighlight(List<GateWindow> inputGates, List<GateWindow> outputGates, int highlightRow) {
        // Save original input states
        Map<GateWindow, Boolean> originalStates = new HashMap<>();
        for (GateWindow gw : inputGates) {
            if (gw.gate instanceof InputGate inputGate) {
                ToggleButton toggle = (ToggleButton)inputGate.value;
                originalStates.put(gw, toggle.value.get());
            }
        }

        StyledDocument doc = new DefaultStyledDocument();
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style highlightStyle = truthTableArea.addStyle("highlight", defaultStyle);
        StyleConstants.setBackground(highlightStyle, HIGHLIGHT_COLOR);
        
        try {
            // Generate header
            StringBuilder header = new StringBuilder();
            for (GateWindow gw : inputGates) {
                header.append(String.format("%-6s", gw.title));
            }
            header.append("| ");
            for (GateWindow gw : outputGates) {
                header.append(String.format("%-6s", gw.title));
            }
            header.append("\n");
            doc.insertString(doc.getLength(), header.toString(), defaultStyle);
            
            // Generate separator
            String separator = "-".repeat(inputGates.size() * 6 + outputGates.size() * 6 + 2) + "\n";
            doc.insertString(doc.getLength(), separator, defaultStyle);
            
            // Generate combinations
            int combinations = 1 << inputGates.size();
            for (int i = 0; i < combinations; i++) {
                StringBuilder row = new StringBuilder();
                
                // Set inputs for this combination
                for (int j = 0; j < inputGates.size(); j++) {
                    boolean value = ((i >> (inputGates.size() - 1 - j)) & 1) == 1;
                    GateWindow gw = inputGates.get(j);
                    if (gw.gate instanceof InputGate inputGate) {
                        ToggleButton toggle = (ToggleButton)inputGate.value;
                        toggle.value.set(value);
                        inputGate.getOutputs();
                    }
                    row.append(String.format("%-6s", value ? "1" : "0"));
                }
                
                row.append("| ");
                
                // Force circuit update
                for (GateWindow gw : outputGates) {
                    if (gw.gate != null) {
                        gw.gate.getOutputs();
                    }
                }
                
                // Get output values
                for (GateWindow gw : outputGates) {
                    if (gw.gate instanceof OutputGate outputGate) {
                        gw.gate.getOutputs();
                        LogicalValue displayValue = ((GateOutputLabel)outputGate.value).value;
                        row.append(String.format("%-6s", displayValue == LogicalValue.TRUE ? "1" : "0"));
                    }
                }
                row.append("\n");
                
                // Apply highlight style to current state row
                doc.insertString(doc.getLength(), row.toString(), 
                    i == highlightRow ? highlightStyle : defaultStyle);
            }
            
            // Restore original input states
            for (GateWindow gw : inputGates) {
                if (gw.gate instanceof InputGate inputGate) {
                    ToggleButton toggle = (ToggleButton)inputGate.value;
                    toggle.value.set(originalStates.get(gw));
                    inputGate.getOutputs();
                }
            }
            
            // Update outputs one final time
            for (GateWindow gw : outputGates) {
                if (gw.gate != null) {
                    gw.gate.getOutputs();
                }
            }
            
            truthTableArea.setStyledDocument(doc);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
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
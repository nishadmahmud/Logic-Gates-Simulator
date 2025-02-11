package fluff.lgs.gui.screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fluff.lgs.LGS;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.impl.InputGate;
import fluff.lgs.gate.impl.OutputGate;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.gui.Element;
import fluff.lgs.gate.IGateType;
import fluff.lgs.gate.connection.Link;

public class EquationScreen extends JFrame {
    private JTextPane equationArea;
    private static EquationScreen instance;
    private Timer updateTimer;
    private final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private final Color TEXT_COLOR = new Color(200, 200, 200);

    public static void showWindow() {
        SwingUtilities.invokeLater(() -> {
            if (instance != null) {
                instance.dispose();
            }
            instance = new EquationScreen();
        });
    }

    private EquationScreen() {
        setTitle("Circuit Equation");
        setSize(400, 300);
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
        setLocation(screenSize.width - getWidth() - 50, 100);
        
        setupUI();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
            }
        });

        setAlwaysOnTop(true);
        setFocusableWindowState(false);

        // Update equation periodically
        updateTimer = new Timer(500, e -> {
            SwingUtilities.invokeLater(this::updateEquation);
        });
        updateTimer.start();

        // Generate initial equation
        SwingUtilities.invokeLater(() -> {
            updateEquation();
            setVisible(true);
        });
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        equationArea = new JTextPane();
        equationArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        equationArea.setEditable(false);
        equationArea.setBackground(new Color(40, 40, 40));
        equationArea.setForeground(TEXT_COLOR);
        equationArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(equationArea);
        scrollPane.setBackground(new Color(40, 40, 40));
        scrollPane.getViewport().setBackground(new Color(40, 40, 40));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private String generateEquation(GateWindow outputGate) {
        if (outputGate.gate instanceof OutputGate) {
            // Get the input connection to this output gate
            List<GateWindow> inputs = getInputConnections(outputGate);
            if (inputs.isEmpty()) {
                return "undefined";
            }
            // For output gates, we just need to get the equation of its input
            return generateEquation(inputs.get(0));
        }
        
        // Handle different gate types
        IGateType type = outputGate.gate.type;
        
        if (type == NativeGateType.NOT) {
            List<GateWindow> notInputs = getInputConnections(outputGate);
            if (notInputs.isEmpty()) return "undefined";
            return "!" + addParenthesesIfNeeded(generateEquation(notInputs.get(0)));
        }
        else if (type == NativeGateType.AND) {
            List<GateWindow> andInputs = getInputConnections(outputGate);
            if (andInputs.isEmpty()) return "undefined";
            return andInputs.stream()
                .map(in -> addParenthesesIfNeeded(generateEquation(in)))
                .collect(Collectors.joining(" • "));
        }
        else if (type == NativeGateType.OR) {
            List<GateWindow> orInputs = getInputConnections(outputGate);
            if (orInputs.isEmpty()) return "undefined";
            return orInputs.stream()
                .map(in -> addParenthesesIfNeeded(generateEquation(in)))
                .collect(Collectors.joining(" + "));
        }
        else if (type == NativeGateType.NAND) {
            List<GateWindow> nandInputs = getInputConnections(outputGate);
            if (nandInputs.isEmpty()) return "undefined";
            return "!(" + nandInputs.stream()
                .map(in -> addParenthesesIfNeeded(generateEquation(in)))
                .collect(Collectors.joining(" • ")) + ")";
        }
        else if (type == NativeGateType.NOR) {
            List<GateWindow> norInputs = getInputConnections(outputGate);
            if (norInputs.isEmpty()) return "undefined";
            return "!(" + norInputs.stream()
                .map(in -> addParenthesesIfNeeded(generateEquation(in)))
                .collect(Collectors.joining(" + ")) + ")";
        }
        else if (type == NativeGateType.XOR) {
            List<GateWindow> xorInputs = getInputConnections(outputGate);
            if (xorInputs.isEmpty()) return "undefined";
            return xorInputs.stream()
                .map(in -> addParenthesesIfNeeded(generateEquation(in)))
                .collect(Collectors.joining(" ⊕ "));
        }
        else if (type == NativeGateType.XNOR) {
            List<GateWindow> xnorInputs = getInputConnections(outputGate);
            if (xnorInputs.isEmpty()) return "undefined";
            return "!(" + xnorInputs.stream()
                .map(in -> addParenthesesIfNeeded(generateEquation(in)))
                .collect(Collectors.joining(" ⊕ ")) + ")";
        }
        else if (type == NativeGateType.INPUT) {
            return outputGate.title;
        }
        
        return "undefined";
    }
    
    private List<GateWindow> getInputConnections(GateWindow gate) {
        List<GateWindow> inputs = new ArrayList<>();
        
        // Get all connections from the world
        for (Link link : LGS.world().connections.list) {
            // If this connection ends at our gate
            if (link.to.parent instanceof GateWindow && link.to.parent == gate) {
                // Add the source gate if it's a GateWindow
                if (link.from.parent instanceof GateWindow sourceGate) {
                    inputs.add(sourceGate);
                }
            }
        }
        
        return inputs;
    }
    
    private String addParenthesesIfNeeded(String expr) {
        // Add parentheses if expression contains operators
        if (expr.contains(" + ") || expr.contains(" • ") || expr.contains(" ⊕ ")) {
            return "(" + expr + ")";
        }
        return expr;
    }

    private void updateEquation() {
        List<GateWindow> outputGates = new ArrayList<>();
        
        // Collect output gates
        for (Element element : LGS.world().gates.list) {
            if (element instanceof GateWindow gw && gw.gate != null) {
                if (gw.gate.type == NativeGateType.OUTPUT) {
                    outputGates.add(gw);
                }
            }
        }

        if (outputGates.isEmpty()) {
            equationArea.setText("Add output gates");
            return;
        }

        // Generate equations
        StringBuilder equations = new StringBuilder();
        equations.append("Boolean Equations:\n\n");

        for (GateWindow output : outputGates) {
            String originalEq = generateEquation(output);
            String simplifiedEq = simplifyEquation(originalEq);
            
            equations.append(output.title).append(" = ");
            equations.append(originalEq);
            if (!originalEq.equals(simplifiedEq)) {
                equations.append("\n  = ").append(simplifiedEq).append(" (simplified)");
            }
            equations.append("\n\n");
        }

        equationArea.setText(equations.toString());
    }

    private String simplifyEquation(String equation) {
        String simplified = equation;
        boolean changed;
        
        do {
            changed = false;
            String previous = simplified;
            
            // Identity laws
            simplified = simplified.replaceAll("A • 1", "A");
            simplified = simplified.replaceAll("1 • A", "A");
            simplified = simplified.replaceAll("A \\+ 0", "A");
            simplified = simplified.replaceAll("0 \\+ A", "A");
            
            // Null laws
            simplified = simplified.replaceAll("A • 0", "0");
            simplified = simplified.replaceAll("0 • A", "0");
            simplified = simplified.replaceAll("A \\+ 1", "1");
            simplified = simplified.replaceAll("1 \\+ A", "1");
            
            // Idempotent law
            simplified = simplified.replaceAll("A • A", "A");
            simplified = simplified.replaceAll("A \\+ A", "A");
            
            // Inverse law
            simplified = simplified.replaceAll("A • !A", "0");
            simplified = simplified.replaceAll("!A • A", "0");
            simplified = simplified.replaceAll("A \\+ !A", "1");
            simplified = simplified.replaceAll("!A \\+ A", "1");
            
            // Double negation
            simplified = simplified.replaceAll("!!A", "A");
            
            // Absorption law
            simplified = simplified.replaceAll("A • \\(A \\+ B\\)", "A");
            simplified = simplified.replaceAll("A \\+ \\(A • B\\)", "A");
            
            // De Morgan's laws
            simplified = simplified.replaceAll("!\\(A • B\\)", "!A \\+ !B");
            simplified = simplified.replaceAll("!\\(A \\+ B\\)", "!A • !B");
            
            // Check if any simplification was applied
            if (!simplified.equals(previous)) {
                changed = true;
            }
            
        } while (changed); // Keep simplifying until no more changes can be made
        
        return simplified;
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
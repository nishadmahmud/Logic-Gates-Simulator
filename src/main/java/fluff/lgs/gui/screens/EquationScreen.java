package fluff.lgs.gui.screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Arrays;

import fluff.lgs.LGS;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.impl.InputGate;
import fluff.lgs.gate.impl.OutputGate;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.gui.Element;
import fluff.lgs.gate.IGateType;
import fluff.lgs.gate.connection.Link;
import fluff.lgs.gate.KMap;
import fluff.lgs.gate.QuineMcCluskey;

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
            String simplifiedEq = simplifyUsingKMap(originalEq, getVariables(output));
            
            equations.append(output.title).append(" = ");
            
            // Sort variables in simplified equation if it's different from original
            if (!originalEq.equals(simplifiedEq) && !isXORExpansion(originalEq, simplifiedEq)) {
                simplifiedEq = sortVariablesAlphabetically(simplifiedEq);
                if (!originalEq.equals(simplifiedEq)) {  // Check again after sorting
                    equations.append(originalEq);
                    equations.append("\n  = ").append(simplifiedEq).append(" (simplified)");
                } else {
                    equations.append(originalEq);
                }
            } else {
                equations.append(originalEq);
            }
            equations.append("\n\n");
        }

        equationArea.setText(equations.toString());
    }

    private String sortVariablesAlphabetically(String equation) {
        // Don't sort if it's a XOR/XNOR expression
        if (equation.contains("⊕")) return equation;
        
        // Split into terms (by +)
        String[] terms = equation.split("\\s*\\+\\s*");
        List<String> sortedTerms = new ArrayList<>();
        
        for (String term : terms) {
            // Split into variables (by •)
            String[] vars = term.trim().split("\\s*•\\s*");
            Arrays.sort(vars);
            sortedTerms.add(String.join(" • ", vars));
        }
        
        // Sort the terms themselves
        Collections.sort(sortedTerms);
        return String.join(" + ", sortedTerms);
    }

    private boolean isXORExpansion(String original, String simplified) {
        // Check if original contains XOR/XNOR and simplified is its expansion
        return (original.contains("⊕") || original.contains("!(") && original.contains("⊕)")) && 
               (simplified.contains("•") && simplified.contains("+"));
    }

    private String simplifyUsingKMap(String equation, List<String> variables) {
        int numVars = variables.size();
        if (numVars == 0) {
            return equation;
        }
        
        int numTerms = 1 << numVars;
        
        // Create truth table from equation
        boolean[] truthTable = new boolean[numTerms];
        for (int i = 0; i < numTerms; i++) {
            Map<String, Boolean> assignments = new HashMap<>();
            // Fix variable ordering to match circuit
            for (int j = 0; j < numVars; j++) {
                // Reverse bit order to match circuit's input ordering
                boolean value = ((i >> (numVars - 1 - j)) & 1) == 1;
                assignments.put(variables.get(j), value);
            }
            truthTable[i] = evaluateEquation(equation, assignments);
        }
        
        // Debug output
        System.out.println("Truth table for: " + equation);
        for (int i = 0; i < numTerms; i++) {
            StringBuilder debug = new StringBuilder();
            for (int j = 0; j < numVars; j++) {
                debug.append(((i >> (numVars - 1 - j)) & 1) == 1 ? "1" : "0").append(" ");
            }
            debug.append("| ").append(truthTable[i] ? "1" : "0");
            System.out.println(debug.toString());
        }
        
        return QuineMcCluskey.simplify(truthTable, variables);
    }

    private List<String> getVariables(GateWindow outputGate) {
        Set<String> variableSet = new LinkedHashSet<>();  // Use LinkedHashSet to maintain insertion order
        collectVariables(outputGate, variableSet, new HashSet<>());
        return new ArrayList<>(variableSet);  // No need to sort, maintain circuit order
    }

    private void collectVariables(GateWindow gate, Set<String> variables, Set<GateWindow> visited) {
        if (visited.contains(gate)) {
            return;  // Avoid cycles
        }
        visited.add(gate);

        // Get inputs in order
        List<GateWindow> inputs = getInputConnections(gate);
        
        // Process inputs first (reverse order to maintain correct precedence)
        for (int i = inputs.size() - 1; i >= 0; i--) {
            GateWindow input = inputs.get(i);
            collectVariables(input, variables, visited);
        }

        // Add this gate's variable if it's an input
        if (gate.gate instanceof InputGate) {
            variables.add(gate.title);
        }
    }

    private boolean evaluateEquation(String equation, Map<String, Boolean> assignments) {
        // Remove spaces
        equation = equation.replaceAll("\\s+", "");
        
        // Handle parentheses recursively
        while (equation.contains("(")) {
            int start = equation.lastIndexOf("(");
            int end = equation.indexOf(")", start);
            String subExpr = equation.substring(start + 1, end);
            boolean value = evaluateEquation(subExpr, assignments);
            equation = equation.substring(0, start) + (value ? "1" : "0") + equation.substring(end + 1);
        }
        
        // Evaluate NOT operations
        while (equation.contains("!")) {
            int pos = equation.indexOf("!");
            if (pos + 1 < equation.length()) {
                char next = equation.charAt(pos + 1);
                boolean value;
                if (next >= 'A' && next <= 'Z') {
                    value = !assignments.getOrDefault(String.valueOf(next), false);
                } else {
                    value = !(next == '1');
                }
                equation = equation.substring(0, pos) + (value ? "1" : "0") + equation.substring(pos + 2);
            }
        }
        
        // Replace variables with their values
        for (Map.Entry<String, Boolean> entry : assignments.entrySet()) {
            equation = equation.replace(entry.getKey(), entry.getValue() ? "1" : "0");
        }

        // Evaluate XOR operations (⊕)
        while (equation.contains("⊕")) {
            int pos = equation.indexOf("⊕");
            if (pos > 0 && pos + 1 < equation.length()) {
                boolean left = equation.charAt(pos - 1) == '1';
                boolean right = equation.charAt(pos + 1) == '1';
                equation = equation.substring(0, pos - 1) + ((left != right) ? "1" : "0") + equation.substring(pos + 2);
            }
        }
        
        // Evaluate AND operations (•)
        while (equation.contains("•")) {
            int pos = equation.indexOf("•");
            if (pos > 0 && pos + 1 < equation.length()) {
                boolean left = equation.charAt(pos - 1) == '1';
                boolean right = equation.charAt(pos + 1) == '1';
                equation = equation.substring(0, pos - 1) + ((left && right) ? "1" : "0") + equation.substring(pos + 2);
            }
        }
        
        // Evaluate OR operations (+)
        while (equation.contains("+")) {
            int pos = equation.indexOf("+");
            if (pos > 0 && pos + 1 < equation.length()) {
                boolean left = equation.charAt(pos - 1) == '1';
                boolean right = equation.charAt(pos + 1) == '1';
                equation = equation.substring(0, pos - 1) + ((left || right) ? "1" : "0") + equation.substring(pos + 2);
            }
        }
        
        // Final result should be just "0" or "1"
        return equation.equals("1");
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
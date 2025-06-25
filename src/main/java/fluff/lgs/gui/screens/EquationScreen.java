package fluff.lgs.gui.screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import fluff.lgs.LGS;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.impl.InputGate;
import fluff.lgs.gate.impl.OutputGate;
import fluff.lgs.gui.elements.gate.ButtonConnection;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.gui.Element;
import fluff.lgs.gate.IGateType;
import fluff.lgs.gate.connection.Link;
import fluff.lgs.gate.QuineMcCluskey;

public class EquationScreen extends JFrame {
    private JTextPane equationArea;
    private static EquationScreen instance;
    private javax.swing.Timer updateTimer;
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
        updateTimer = new javax.swing.Timer(500, e -> {
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
            equationArea.setText("Add output gates to see equations");
            return;
        }

        // Generate equations
        StringBuilder equations = new StringBuilder();
        equations.append("Boolean Equations:\n\n");

        for (GateWindow output : outputGates) {
            String equation = generateEquation(output);
            equations.append(output.title).append(" = ").append(equation).append("\n\n");
            
            // Try to simplify the equation for all types
            if (!equation.equals("undefined")) {
                List<String> variables = getVariablesForOutput(output);
                if (!variables.isEmpty()) {
                    String simplified = simplifyEquation(equation, variables);
                    if (!equation.equals(simplified) && !simplified.equals("undefined")) {
                        equations.append("  = ").append(simplified).append(" (simplified)\n\n");
                    }
                }
            }
        }

        equationArea.setText(equations.toString());
    }

    private String generateEquation(GateWindow gate) {
        if (gate.gate instanceof InputGate) {
            return gate.title;
        }
        
        if (gate.gate instanceof OutputGate) {
            ButtonConnection input = gate.gate.inputs[0];
            if (input != null && input.from instanceof ButtonConnection fromButton) {
                if (fromButton.parent instanceof GateWindow sourceGate) {
                    // Find which output we're connected to
                    int outputIndex = -1;
                    for (int i = 0; i < sourceGate.gate.outputs.length; i++) {
                        if (sourceGate.gate.outputs[i] == fromButton) {
                            outputIndex = i;
                            break;
                        }
                    }
                    
                    if (outputIndex != -1) {
                        return generateGateEquation(sourceGate, outputIndex);
                    }
                }
            }
            return "undefined";
        }

        // For non-output gates, generate equation for their first output
        return generateGateEquation(gate, 0);
    }

    private String generateGateEquation(GateWindow gate, int outputIndex) {
        IGateType type = gate.gate.type;
        
        if (type == NativeGateType.INPUT) {
            return gate.title;
        }
        
        // Get input connections
        List<GateWindow> inputs = getInputConnections(gate);
        
        // Handle basic gates
        if (isBasicGate(type)) {
            return generateBasicGateEquation(type, inputs, outputIndex);
        }
        
        // Handle composite gates
        if (isCompositeGate(type)) {
            return generateCompositeGateEquation(type, inputs, outputIndex);
        }
        
        return "undefined";
    }

    private boolean isBasicGate(IGateType type) {
        return type == NativeGateType.NOT || 
               type == NativeGateType.AND || 
               type == NativeGateType.OR || 
               type == NativeGateType.NAND || 
               type == NativeGateType.NOR || 
               type == NativeGateType.XOR || 
               type == NativeGateType.XNOR;
        }

    private boolean isCompositeGate(IGateType type) {
        return type == NativeGateType.HALF_ADDER ||
               type == NativeGateType.FULL_ADDER ||
               type == NativeGateType.HALF_SUBTRACTOR ||
               type == NativeGateType.FULL_SUBTRACTOR ||
               type == NativeGateType.ENCODER_4TO2 ||
               type == NativeGateType.ENCODER_8TO3 ||
               type == NativeGateType.DECODER_2TO4 ||
               type == NativeGateType.DECODER_3TO8 ||
               type == NativeGateType.COMPARATOR_1BIT ||
               type == NativeGateType.COMPARATOR_2BIT ||
               type == NativeGateType.MUX_2TO1 ||
               type == NativeGateType.MUX_4TO1 ||
               type == NativeGateType.DEMUX_1TO2 ||
               type == NativeGateType.DEMUX_1TO4;
    }

    private String generateBasicGateEquation(IGateType type, List<GateWindow> inputs, int outputIndex) {
        if (inputs.isEmpty()) return "undefined";
        
        List<String> inputExpressions = inputs.stream()
            .map(this::generateEquation)
            .collect(Collectors.toList());
        
        if (type == NativeGateType.NOT) {
            return "!" + addParenthesesIfNeeded(inputExpressions.get(0));
        } else if (type == NativeGateType.AND) {
            return inputExpressions.stream()
                .map(this::addParenthesesIfNeeded)
                .collect(Collectors.joining(" • "));
        } else if (type == NativeGateType.OR) {
            return inputExpressions.stream()
                .map(this::addParenthesesIfNeeded)
                .collect(Collectors.joining(" + "));
        } else if (type == NativeGateType.NAND) {
            return "!(" + inputExpressions.stream()
                .map(this::addParenthesesIfNeeded)
                .collect(Collectors.joining(" • ")) + ")";
        } else if (type == NativeGateType.NOR) {
            return "!(" + inputExpressions.stream()
                .map(this::addParenthesesIfNeeded)
                .collect(Collectors.joining(" + ")) + ")";
        } else if (type == NativeGateType.XOR) {
            return inputExpressions.stream()
                .map(this::addParenthesesIfNeeded)
                .collect(Collectors.joining(" ⊕ "));
        } else if (type == NativeGateType.XNOR) {
            return "!(" + inputExpressions.stream()
                .map(this::addParenthesesIfNeeded)
                .collect(Collectors.joining(" ⊕ ")) + ")";
        }
            
                return "undefined";
            }
            
    private String generateCompositeGateEquation(IGateType type, List<GateWindow> inputs, int outputIndex) {
        if (inputs.isEmpty()) return "undefined";
        
        List<String> inputExpressions = inputs.stream()
            .map(this::generateEquation)
            .collect(Collectors.toList());
        
        if (type == NativeGateType.HALF_ADDER) {
            if (outputIndex == 0) {
                return inputExpressions.get(0) + " ⊕ " + inputExpressions.get(1); // Sum
            } else {
                return inputExpressions.get(0) + " • " + inputExpressions.get(1); // Carry
            }
        } else if (type == NativeGateType.FULL_ADDER) {
            if (outputIndex == 0) {
                return inputExpressions.get(0) + " ⊕ " + inputExpressions.get(1) + " ⊕ " + inputExpressions.get(2); // Sum
            } else {
                // Carry = (A•B) + (A•Cin) + (B•Cin)
                return "(" + inputExpressions.get(0) + " • " + inputExpressions.get(1) + ") + " +
                       "(" + inputExpressions.get(0) + " • " + inputExpressions.get(2) + ") + " +
                       "(" + inputExpressions.get(1) + " • " + inputExpressions.get(2) + ")";
            }
        } else if (type == NativeGateType.HALF_SUBTRACTOR) {
            if (outputIndex == 0) {
                return inputExpressions.get(0) + " ⊕ " + inputExpressions.get(1); // Difference
            } else {
                return "!" + inputExpressions.get(0) + " • " + inputExpressions.get(1); // Borrow
                                }
        } else if (type == NativeGateType.FULL_SUBTRACTOR) {
            if (outputIndex == 0) {
                return inputExpressions.get(0) + " ⊕ " + inputExpressions.get(1) + " ⊕ " + inputExpressions.get(2); // Difference
            } else {
                // Borrow = (!A•B) + (!A•Bin) + (B•Bin)
                return "(!" + inputExpressions.get(0) + " • " + inputExpressions.get(1) + ") + " +
                       "(!" + inputExpressions.get(0) + " • " + inputExpressions.get(2) + ") + " +
                       "(" + inputExpressions.get(1) + " • " + inputExpressions.get(2) + ")";
            }
        } else if (type == NativeGateType.ENCODER_4TO2) {
                                        if (outputIndex == 0) {
                return inputExpressions.get(2) + " + " + inputExpressions.get(3); // Y1 = I2 + I3
                                        } else {
                return inputExpressions.get(1) + " + " + inputExpressions.get(3); // Y0 = I1 + I3
            }
        } else if (type == NativeGateType.ENCODER_8TO3) {
            if (outputIndex == 0) {
                return inputExpressions.get(4) + " + " + inputExpressions.get(5) + " + " + 
                       inputExpressions.get(6) + " + " + inputExpressions.get(7); // Y2
            } else if (outputIndex == 1) {
                return inputExpressions.get(2) + " + " + inputExpressions.get(3) + " + " + 
                       inputExpressions.get(6) + " + " + inputExpressions.get(7); // Y1
            } else {
                return inputExpressions.get(1) + " + " + inputExpressions.get(3) + " + " + 
                       inputExpressions.get(5) + " + " + inputExpressions.get(7); // Y0
            }
        } else if (type == NativeGateType.DECODER_2TO4) {
            String a1 = inputExpressions.get(0);
            String a0 = inputExpressions.get(1);
            if (outputIndex == 3) {
                return a1 + " • " + a0;         // Y3
            } else if (outputIndex == 2) {
                return a1 + " • !" + a0;        // Y2
            } else if (outputIndex == 1) {
                return "!" + a1 + " • " + a0;   // Y1
            } else if (outputIndex == 0) {
                return "!" + a1 + " • !" + a0;  // Y0
            }
        } else if (type == NativeGateType.DECODER_3TO8) {
            String a2 = inputExpressions.get(0);
            String b1 = inputExpressions.get(1);
            String b0 = inputExpressions.get(2);
            if (outputIndex == 7) {
                return a2 + " • " + b1 + " • " + b0;
            } else if (outputIndex == 6) {
                return a2 + " • " + b1 + " • !" + b0;
            } else if (outputIndex == 5) {
                return a2 + " • !" + b1 + " • " + b0;
            } else if (outputIndex == 4) {
                return a2 + " • !" + b1 + " • !" + b0;
            } else if (outputIndex == 3) {
                return "!" + a2 + " • " + b1 + " • " + b0;
            } else if (outputIndex == 2) {
                return "!" + a2 + " • " + b1 + " • !" + b0;
            } else if (outputIndex == 1) {
                return "!" + a2 + " • !" + b1 + " • " + b0;
            } else if (outputIndex == 0) {
                return "!" + a2 + " • !" + b1 + " • !" + b0;
            }
        } else if (type == NativeGateType.COMPARATOR_1BIT) {
            String a = inputExpressions.get(0);
            String b = inputExpressions.get(1);
            if (outputIndex == 0) {
                return a + " • !" + b;        // A > B
            } else if (outputIndex == 1) {
                return "(" + a + " • " + b + ") + (!" + a + " • !" + b + ")";  // A = B
            } else if (outputIndex == 2) {
                return "!" + a + " • " + b;   // A < B
            }
        } else if (type == NativeGateType.COMPARATOR_2BIT) {
            String a1_comp = inputExpressions.get(0);
            String a0_comp = inputExpressions.get(1);
            String b1_comp = inputExpressions.get(2);
            String b0_comp = inputExpressions.get(3);
            if (outputIndex == 0) { // A > B
                return a1_comp + " • !" + b1_comp + " + (" + a1_comp + " • " + b1_comp + " • " + a0_comp + " • !" + b0_comp + ")";
            } else if (outputIndex == 1) { // A = B
                return "(" + a1_comp + " • " + b1_comp + " + !" + a1_comp + " • !" + b1_comp + ") • (" + 
                       a0_comp + " • " + b0_comp + " + !" + a0_comp + " • !" + b0_comp + ")";
            } else if (outputIndex == 2) { // A < B
                return "!" + a1_comp + " • " + b1_comp + " + (" + a1_comp + " • " + b1_comp + " • !" + a0_comp + " • " + b0_comp + ")";
                                }
        } else if (type == NativeGateType.MUX_2TO1) {
            String d0 = inputExpressions.get(0);
            String d1 = inputExpressions.get(1);
            String sel = inputExpressions.get(2);
            return "(" + d0 + " • !" + sel + ") + (" + d1 + " • " + sel + ")";
        } else if (type == NativeGateType.MUX_4TO1) {
            String d0_4 = inputExpressions.get(0);
            String d1_4 = inputExpressions.get(1);
            String d2_4 = inputExpressions.get(2);
            String d3_4 = inputExpressions.get(3);
            String s1 = inputExpressions.get(4);
            String s0 = inputExpressions.get(5);
            return "(" + d0_4 + " • !" + s1 + " • !" + s0 + ") + (" + d1_4 + " • !" + s1 + " • " + s0 + ") + " +
                   "(" + d2_4 + " • " + s1 + " • !" + s0 + ") + (" + d3_4 + " • " + s1 + " • " + s0 + ")";
        } else if (type == NativeGateType.DEMUX_1TO2) {
            String data = inputExpressions.get(0);
            String sel_demux = inputExpressions.get(1);
            if (outputIndex == 0) {
                return data + " • !" + sel_demux;
            } else {
                return data + " • " + sel_demux;
            }
        } else if (type == NativeGateType.DEMUX_1TO4) {
            String data_4 = inputExpressions.get(0);
            String s1_demux = inputExpressions.get(1);
            String s0_demux = inputExpressions.get(2);
            if (outputIndex == 0) {
                return data_4 + " • !" + s1_demux + " • !" + s0_demux;
            } else if (outputIndex == 1) {
                return data_4 + " • !" + s1_demux + " • " + s0_demux;
            } else if (outputIndex == 2) {
                return data_4 + " • " + s1_demux + " • !" + s0_demux;
            } else if (outputIndex == 3) {
                return data_4 + " • " + s1_demux + " • " + s0_demux;
            }
        }
        
        return "undefined";
    }

    private boolean isCompositeGateEquation(String equation) {
        return equation.contains("⊕") || equation.contains("ENCODER") || equation.contains("DECODER") ||
               equation.contains("COMPARATOR") || equation.contains("MUX") || equation.contains("DEMUX");
    }

    private String addParenthesesIfNeeded(String expr) {
        if (expr.contains(" ") && !expr.startsWith("(") && !expr.equals("undefined")) {
            return "(" + expr + ")";
        }
        return expr;
    }

    private List<GateWindow> getInputConnections(GateWindow gate) {
        List<GateWindow> inputs = new ArrayList<>();
        for (Link link : LGS.world().connections.list) {
            if (link.to.parent instanceof GateWindow && link.to.parent == gate) {
                if (link.from.parent instanceof GateWindow sourceGate) {
                    inputs.add(sourceGate);
                }
            }
        }
        return inputs;
    }

    private List<String> getVariablesForOutput(GateWindow outputGate) {
        Set<String> variables = new LinkedHashSet<>();
        collectVariables(outputGate, variables, new HashSet<>());
        return new ArrayList<>(variables);
    }

    private void collectVariables(GateWindow gate, Set<String> variables, Set<GateWindow> visited) {
        if (visited.contains(gate)) return;
        visited.add(gate);

        // If this is an input gate, add its name to variables
        if (gate.gate instanceof InputGate) {
            variables.add(gate.title);
            return;
        }

        // For all other gates, collect variables from their inputs
        List<GateWindow> inputs = getInputConnections(gate);
        for (GateWindow input : inputs) {
            collectVariables(input, variables, visited);
        }
    }

    private String simplifyEquation(String equation, List<String> variables) {
        if (variables.isEmpty() || equation.equals("undefined")) {
            return equation;
        }
        
        try {
            // Generate truth table
            int numVars = variables.size();
        int numTerms = 1 << numVars;
        boolean[] truthTable = new boolean[numTerms];
            
            System.out.println("Simplifying equation: " + equation);
            System.out.println("Variables: " + variables);
            
        for (int i = 0; i < numTerms; i++) {
            Map<String, Boolean> assignments = new HashMap<>();
            for (int j = 0; j < numVars; j++) {
                boolean value = ((i >> (numVars - 1 - j)) & 1) == 1;
                assignments.put(variables.get(j), value);
            }
            truthTable[i] = evaluateEquation(equation, assignments);
        }
        
            // Use Quine-McCluskey to simplify
            String simplified = QuineMcCluskey.simplify(truthTable, variables);
            System.out.println("Simplified to: " + simplified);
            return simplified;
        } catch (Exception e) {
            System.out.println("Error simplifying equation: " + e.getMessage());
            e.printStackTrace();
            return equation; // Return original if simplification fails
        }
    }

    private boolean evaluateEquation(String equation, Map<String, Boolean> assignments) {
        try {
            // Replace variables with their values
            String evalEquation = equation;
        for (Map.Entry<String, Boolean> entry : assignments.entrySet()) {
                evalEquation = evalEquation.replace(entry.getKey(), entry.getValue() ? "1" : "0");
        }

            // Replace operators
            evalEquation = evalEquation.replace("•", "&").replace("⊕", "^").replace("!", "~");
            
            // Evaluate the expression
            return evaluateBooleanExpression(evalEquation);
        } catch (Exception e) {
            System.out.println("Error evaluating equation: " + equation + " with assignments: " + assignments);
            return false;
        }
    }

    // Helper method to find an operator outside of any parentheses
    private int findOperatorOutsideParens(String expr, char op) {
        int parenCount = 0;
        // Iterate from right to left for left-to-right associativity
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')') {
                parenCount++;
            } else if (c == '(') {
                parenCount--;
            } else if (c == op && parenCount == 0) {
                return i;
            }
        }
        return -1; // Not found
    }

    private boolean evaluateBooleanExpression(String expr) {
        expr = expr.trim();
        
        // Handle parentheses that wrap the whole expression
        if (expr.startsWith("(") && expr.endsWith(")")) {
            int parenCount = 1;
            boolean allWrapped = true;
            for (int i = 1; i < expr.length() - 1; i++) {
                if (expr.charAt(i) == '(') parenCount++;
                else if (expr.charAt(i) == ')') parenCount--;
                if (parenCount == 0) {
                    allWrapped = false;
                    break;
                }
            }
            if (allWrapped) {
                return evaluateBooleanExpression(expr.substring(1, expr.length() - 1));
            }
        }
        
        // Handle operators with precedence (lowest first)
        int orIndex = findOperatorOutsideParens(expr, '+');
        if (orIndex != -1) {
            String left = expr.substring(0, orIndex);
            String right = expr.substring(orIndex + 1);
            return evaluateBooleanExpression(left) | evaluateBooleanExpression(right);
        }
        
        int xorIndex = findOperatorOutsideParens(expr, '^');
        if (xorIndex != -1) {
            String left = expr.substring(0, xorIndex);
            String right = expr.substring(xorIndex + 1);
            return evaluateBooleanExpression(left) ^ evaluateBooleanExpression(right);
        }
        
        int andIndex = findOperatorOutsideParens(expr, '&');
        if (andIndex != -1) {
            String left = expr.substring(0, andIndex);
            String right = expr.substring(andIndex + 1);
            return evaluateBooleanExpression(left) & evaluateBooleanExpression(right);
        }
        
        // Handle NOT
        if (expr.startsWith("~")) {
            return !evaluateBooleanExpression(expr.substring(1));
        }
        
        // Base cases (should be all that's left after recursion)
        if (expr.equals("1")) return true;
        if (expr.equals("0")) return false;
        
        // If we reach here, something is wrong
        throw new IllegalArgumentException("Invalid boolean expression fragment: " + expr);
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

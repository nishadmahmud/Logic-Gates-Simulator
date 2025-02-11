package fluff.lgs.gui.screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import fluff.lgs.LGS;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.impl.InputGate;
import fluff.lgs.gate.impl.OutputGate;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.gui.Element;

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

    private void updateEquation() {
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
            equationArea.setText("Add input and output gates");
            return;
        }

        // Generate equations
        StringBuilder equations = new StringBuilder();
        equations.append("Boolean Equations:\n\n");

        for (GateWindow output : outputGates) {
            equations.append(output.title).append(" = ");
            // TODO: Generate actual equation by traversing circuit
            equations.append("f(");
            for (int i = 0; i < inputGates.size(); i++) {
                if (i > 0) equations.append(", ");
                equations.append(inputGates.get(i).title);
            }
            equations.append(")");
            equations.append("\n");
        }

        equationArea.setText(equations.toString());
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
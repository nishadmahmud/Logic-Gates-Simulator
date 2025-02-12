package fluff.lgs.gui.elements.gate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import org.lwjgl.Sys;
import org.newdawn.slick.Image;

import fluff.lgs.LGS;
import fluff.lgs.gate.IGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.impl.InputGate;
import fluff.lgs.gate.impl.OutputGate;
import fluff.lgs.gate.connection.Link;
import fluff.lgs.gui.WindowRegistry;
import fluff.lgs.gui.elements.Icon;
import fluff.lgs.gui.elements.ToggleButton;
import fluff.lgs.gui.elements.Window;
import fluff.lgs.gui.screens.GateSettingsScreen;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.values.BooleanValue;
import fluff.lgs.gate.connection.ConnectionType;
import fluff.lgs.gui.Element;
import java.util.Iterator;

public class GateWindow extends Window {
	
	private static final int CONNECTION_HEIGHT = 50;
	public static final int CONNECTION_BUTTON_HEIGHT = 20;
	
	public LogicGate gate;
	private int currentInputs;
	private JSpinner inputCountSpinner;  // For configuration UI
	
	private List<Point> inputs = new ArrayList<>();
	private Map<Integer, Link> inputConnections = new HashMap<>();

	private boolean hasInputConnection(int index) {
		return inputConnections.containsKey(index);
	}

	private Link getInputConnection(int index) {
		return inputConnections.get(index);
	}

	private void setInputConnection(int index, Link link) {
		inputConnections.put(index, link);
	}
	
	public GateWindow(WindowRegistry reg) {
		super(reg, null, 0, 0, 200, TITLE_SIZE);
	}
	
	@Override
	public void mousePress(int button, int mouseX, int mouseY) {
		super.mousePress(button, mouseX, mouseY);
		
		if (!hovered || found) return;
		
		if (button == 1 && canDrag(mouseX, mouseY)) LGS.setScreen(new GateSettingsScreen(this));
	}
	
	public void init(IGateType type, IDataInput data) throws IOException {
		currentInputs = type.getDefaultInputs();
		height = TITLE_SIZE + Math.max(currentInputs, type.getOutputs()) * CONNECTION_HEIGHT;
		
		gate = type.create(this, data);
		
		setupInputConfiguration();
		
		int yIn = TITLE_SIZE;
		yIn += (height - yIn) / 2 - currentInputs * CONNECTION_HEIGHT / 2;
		yIn += (CONNECTION_HEIGHT - CONNECTION_BUTTON_HEIGHT) / 2;
		for (int in = 0; in < currentInputs; in++) {
			ButtonConnection b = gate.inputs[in];
			b.x = -CONNECTION_BUTTON_HEIGHT / 2;
			b.y = yIn + in * CONNECTION_HEIGHT;
			elements.add(b);
		}
		
		int yOut = TITLE_SIZE;
		yOut += (height - yOut) / 2 - type.getOutputs() * CONNECTION_HEIGHT / 2;
		yOut += (CONNECTION_HEIGHT - CONNECTION_BUTTON_HEIGHT) / 2;
		for (int out = 0; out < type.getOutputs(); out++) {
			ButtonConnection b = gate.outputs[out];
			b.x = width - CONNECTION_BUTTON_HEIGHT / 2;
			b.y = yOut + out * CONNECTION_HEIGHT;
			elements.add(b);
		}
	}
	
	public static LogicGate nativeGate(GateWindow gw, LogicGate gate) {
		Image icon = gate.type.getIcon();
		gw.elements.add(new Icon(icon, gw.width / 2 - icon.getWidth() / 2, TITLE_SIZE + (gw.height - TITLE_SIZE) / 2 - icon.getHeight() / 2, icon.getWidth(), icon.getHeight()));
		return gate;
	}
	
	public static LogicGate inputGate(GateWindow gw, LogicalValue value) {
		ToggleButton button = new ToggleButton(0, 0, 20, BooleanValue.of(value == LogicalValue.TRUE));
		button.x = gw.width / 2 - button.width / 2;
		button.y = TITLE_SIZE + (gw.height - TITLE_SIZE) / 2 - button.height / 2;
		gw.elements.add(button);
		return new InputGate(button);
	}
	
 	public static LogicGate outputGate(GateWindow gw) {
 		GateOutputLabel label = new GateOutputLabel(gw, gw.width / 2 - CONNECTION_HEIGHT / 2, TITLE_SIZE + (gw.height - TITLE_SIZE) / 2 - CONNECTION_HEIGHT / 2, CONNECTION_HEIGHT, CONNECTION_HEIGHT);
		gw.elements.add(label);
		return new OutputGate(label);
 	}

	private void setupInputConfiguration() {
		if (gate != null && gate.type.getMaxInputs() > gate.type.getMinInputs()) {
			SpinnerNumberModel model = new SpinnerNumberModel(
				currentInputs,
				gate.type.getMinInputs(),
				gate.type.getMaxInputs(),
				1
			);
			inputCountSpinner = new JSpinner(model);
			inputCountSpinner.addChangeListener(e -> {
				int newCount = (Integer)inputCountSpinner.getValue();
				updateInputCount(newCount);
			});
			// Add spinner to gate configuration panel
		}
	}

	public int getInputCount() {
		return currentInputs;
	}

	public void updateInputCount(int newCount) {
		if (gate != null && newCount >= gate.type.getMinInputs() && newCount <= gate.type.getMaxInputs()) {
			// Store old connections
			Map<Integer, Link> oldConnections = new HashMap<>();
			for (int i = 0; i < currentInputs; i++) {
				if (hasInputConnection(i)) {
					oldConnections.put(i, getInputConnection(i));
				}
			}

			// Update input count
			currentInputs = newCount;
			
			// Update height based on new input count
			height = TITLE_SIZE + Math.max(currentInputs, gate.type.getOutputs()) * CONNECTION_HEIGHT;
			
			// Remove old input buttons
			Iterator<Element> it = elements.list.iterator();
			while (it.hasNext()) {
				Element e = it.next();
				if (e instanceof ButtonConnection bc && bc.getType() == ConnectionType.INPUT) {
					it.remove();
				}
			}
			
			// Add new input buttons
			int yIn = TITLE_SIZE;
			yIn += (height - yIn) / 2 - currentInputs * CONNECTION_HEIGHT / 2;
			yIn += (CONNECTION_HEIGHT - CONNECTION_BUTTON_HEIGHT) / 2;
			for (int in = 0; in < currentInputs; in++) {
				ButtonConnection b = gate.inputs[in];
				b.x = -CONNECTION_BUTTON_HEIGHT / 2;
				b.y = yIn + in * CONNECTION_HEIGHT;
				elements.add(b);
			}

			// Restore connections that are still valid
			oldConnections.forEach((index, link) -> {
				if (index < currentInputs) {
					setInputConnection(index, link);
				}
			});

			// Update gate logic
			gate.updateInputCount(currentInputs);
			
			// Recreate input slots
			recreateInputSlots();
		}
	}

	private void recreateInputSlots() {
		// Clear existing input slots
		inputs.clear();
		
		// Create new input slots based on currentInputs
		int spacing = getHeight() / (currentInputs + 1);
		for (int i = 0; i < currentInputs; i++) {
			inputs.add(new Point(0, (i + 1) * spacing));
		}
	}
}

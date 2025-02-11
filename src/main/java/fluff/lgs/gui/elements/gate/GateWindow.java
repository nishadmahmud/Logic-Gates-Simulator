package fluff.lgs.gui.elements.gate;

import java.io.IOException;

import org.lwjgl.Sys;
import org.newdawn.slick.Image;

import fluff.lgs.LGS;
import fluff.lgs.gate.IGateType;
import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.impl.InputGate;
import fluff.lgs.gate.impl.OutputGate;
import fluff.lgs.gui.WindowRegistry;
import fluff.lgs.gui.elements.Icon;
import fluff.lgs.gui.elements.ToggleButton;
import fluff.lgs.gui.elements.Window;
import fluff.lgs.gui.screens.GateSettingsScreen;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.values.BooleanValue;

public class GateWindow extends Window {
	
	private static final int CONNECTION_HEIGHT = 50;
	public static final int CONNECTION_BUTTON_HEIGHT = 20;
	
	public LogicGate gate;
	
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
		height = TITLE_SIZE + Math.max(type.getInputs(), type.getOutputs()) * CONNECTION_HEIGHT;
		
		gate = type.create(this, data);
		
		int yIn = TITLE_SIZE;
		yIn += (height - yIn) / 2 - type.getInputs() * CONNECTION_HEIGHT / 2;
		yIn += (CONNECTION_HEIGHT - CONNECTION_BUTTON_HEIGHT) / 2;
		for (int in = 0; in < type.getInputs(); in++) {
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
}

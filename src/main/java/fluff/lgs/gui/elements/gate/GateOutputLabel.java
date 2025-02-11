package fluff.lgs.gui.elements.gate;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fluff.lgs.gate.LogicGate;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.OutputValue;
import fluff.lgs.gui.Element;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.utils.Colors;

public class GateOutputLabel extends Element implements OutputValue {
	
	private final GateWindow gw;
	public LogicalValue value;
	
	public GateOutputLabel(GateWindow gw, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.gw = gw;
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		gw.gate.getOutputs();
		
		String text = "undefined";
		Color color = Colors.connection_undefined_hover;
		if (value == LogicalValue.TRUE) {
			text = "true";
			color = Colors.connection_true_hover;
		} else if (value == LogicalValue.FALSE) {
			text = "false";
			color = Colors.connection_false_hover;
		}
		
		Fonts.draw(Align.CENTER, text, width / 2, height / 2, color);
	}
	
	@Override
	public void setLogicalValue(LogicalValue value) {
		this.value = value;
	}
}

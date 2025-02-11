package fluff.lgs.gui.elements.gate;

import java.io.IOException;

import org.lwjgl.Sys;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import fluff.lgs.LGS;
import fluff.lgs.gate.IGateType;
import fluff.lgs.gui.elements.Icon;
import fluff.lgs.gui.elements.Label;
import fluff.lgs.gui.elements.NicePanel;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.utils.Colors;

public class GateButton extends NicePanel {
	
	private final IGateType type;
	private final Icon icon;
	
	public GateButton(IGateType type) {
		super(0, 0, 0, 70, true);
		this.type = type;
		
		elements.add(icon = new Icon(type.getIcon(), 0, 0, height, height));
		
		int xOff = 10;
		int yOff = 10;
		if (icon.image != null) {
			xOff = icon.width;
		}
		
		elements.add(new Label(type.getID(), xOff, yOff, 0, 0, Fonts.NORMAL, Align.START, Align.START));
		yOff += Fonts.getHeight();
		
		if (type.getLine1() != null) {
			Label l = new Label(type.getLine1(), xOff, yOff, 0, 0, Fonts.SMALL, Align.START, Align.START);
			l.color = Colors.text_dark;
			elements.add(l);
			yOff += Fonts.getHeight();
		}
		if (type.getLine2() != null) {
			Label l = new Label(type.getLine2(), xOff, yOff, 0, 0, Fonts.SMALL, Align.START, Align.START);
			l.color = Colors.text_dark;
			elements.add(l);
			yOff += Fonts.getHeight();
		}
		if (type.getLine1() == null && type.getLine2() == null) {
			Label l = new Label("No description set.", xOff, yOff, 0, 0, Fonts.SMALL, Align.START, Align.START);
			l.color = Colors.text_dark;
			elements.add(l);
			yOff += Fonts.getHeight();
		}
	}
	
	protected void onAction() {
		GateWindow gw = new GateWindow(LGS.world().windowReg);
		gw.x = LGS.world().getCenterX() - gw.width / 2;
		gw.y = LGS.world().getCenterY() - gw.height / 2;
		
		gw.title = type.getID();
		
		try {
			gw.init(type, null);
		} catch (IOException e) {
			Sys.alert("Error", e.getMessage());
			return;
		}
		
		LGS.world().gates.add(gw);
		
		LGS.setScreen(null);
	}
	
	@Override
	public void mousePress(int button, int mouseX, int mouseY) {
		super.mousePress(button, mouseX, mouseY);
		
		if (!hovered) return;
		
		onAction();
	}
}

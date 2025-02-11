package fluff.lgs.gui.elements;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fluff.lgs.utils.Colors;

public class NicePanel extends Panel {
	
	public static final int BORDER_OFF = 4;
	public static final int BORDER_SIZE = 2;
	public static final int BORDER_TOTAL = BORDER_OFF + BORDER_SIZE;
	
	protected final boolean allowHover;
	
	public NicePanel(int x, int y, int width, int height, boolean allowHover) {
		super(x, y, width, height);
		this.allowHover = allowHover;
	}
	
	@Override
	protected void renderBackground(Graphics g, int mouseX, int mouseY) {
		g.setColor(Colors.nice_panel_border);
		g.fillRect(BORDER_OFF, BORDER_OFF, width - BORDER_OFF * 2, height - BORDER_OFF * 2);
		
		g.setColor(getNiceColor());
		g.fillRect(BORDER_TOTAL, BORDER_TOTAL, width - BORDER_TOTAL * 2, height - BORDER_TOTAL * 2);
	}
	
	protected Color getNiceColor() {
		return allowHover && hovered ? Colors.nice_panel_hovered : Colors.nice_panel;
	}
}

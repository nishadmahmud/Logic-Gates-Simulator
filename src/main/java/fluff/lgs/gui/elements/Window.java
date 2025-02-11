package fluff.lgs.gui.elements;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fluff.lgs.gui.Element;
import fluff.lgs.gui.Elements;
import fluff.lgs.gui.IParent;
import fluff.lgs.gui.WindowRegistry;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.utils.Colors;

public class Window extends Panel {
	
	public static final int TITLE_SIZE = 20;
	
	private boolean dragging;
	
	public WindowRegistry reg;
	public long winID;
	
	public String title;
	
	public Window(WindowRegistry reg, String title, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.reg = reg;
		if (reg != null) this.winID = reg.genWinID(this);
		this.title = title;
	}
	
	@Override
	protected void renderBackground(Graphics g, int mouseX, int mouseY) {
		g.setColor(Colors.window_border);
		g.fillRect(-1, -1, width + 2, height + 2);
		
		super.renderBackground(g, mouseX, mouseY);
		
		g.setColor(Colors.window_title);
		g.fillRect(0, 0, width, TITLE_SIZE);
		
		Fonts.draw(Align.CENTER, title, width / 2, 10, Colors.text);
	}
	
	@Override
	public void mousePress(int button, int mouseX, int mouseY) {
		super.mousePress(button, mouseX, mouseY);
		
		if (!hovered) return;
		
		parentElements.remove(this);
		parentElements.add(this);
		
		if (button == 0 && !found && canDrag(mouseX, mouseY)) dragging = true;
	}
	
	protected boolean canDrag(int mouseX, int mouseY) {
		return mouseY < 20;
	}
	
	@Override
	public void mouseRelease(int button, int mouseX, int mouseY) {
		super.mouseRelease(button, mouseX, mouseY);
		
		if (button == 0 && dragging) {
			dragging = false;
		}
	}
	
	@Override
	public void mouseDrag(int oldX, int oldY, int mouseX, int mouseY) {
		super.mouseDrag(oldX, oldY, mouseX, mouseY);
		
		if (dragging) {
			x += mouseX - oldX;
			y += mouseY - oldY;
		}
	}
}

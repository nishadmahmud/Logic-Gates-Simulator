package fluff.lgs.gui.elements;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fluff.lgs.gui.Element;
import fluff.lgs.gui.Elements;
import fluff.lgs.gui.IParent;
import fluff.lgs.utils.Colors;

public class Panel extends Element implements IParent {
	
	public final Elements elements = new Elements(this);
	public boolean found;
	
	public Panel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		renderBackground(g, mouseX, mouseY);
		
		elements.render(g, mouseX, mouseY);
	}
	
	protected void renderBackground(Graphics g, int mouseX, int mouseY) {
		g.setColor(Colors.panel);
		g.fillRect(0, 0, width, height);
	}
	
	@Override
	public boolean hover(int mouseX, int mouseY, boolean found) {
		this.found = elements.hover(mouseX, mouseY, found);
		
		return this.found ? true : super.hover(mouseX, mouseY, found);
	}
	
	@Override
	public void update(int delta) {
		elements.update(delta);
	}
	
	@Override
	public void mousePress(int button, int mouseX, int mouseY) {
		elements.mousePress(button, mouseX, mouseY);
	}
	
	@Override
	public void mouseRelease(int button, int mouseX, int mouseY) {
		elements.mouseRelease(button, mouseX, mouseY);
	}
	
	@Override
	public void mouseDrag(int oldX, int oldY, int mouseX, int mouseY) {
		elements.mouseDrag(oldX, oldY, mouseX, mouseY);
	}
	
	@Override
	public boolean mouseScroll(int delta) {
		return elements.mouseScroll(delta);
	}
	
	@Override
	public boolean keyPress(int key, char c) {
		return elements.keyPress(key, c);
	}
	
	@Override
	public boolean keyRelease(int key, char c) {
		return elements.keyRelease(key, c);
	}
	
	@Override
	public int getTotalX() {
		return parent.getTotalX() + getX();
	}
	
	@Override
	public int getTotalY() {
		return parent.getTotalY() + getY();
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
}

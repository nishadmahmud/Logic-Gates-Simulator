package fluff.lgs.gui;

import java.io.IOException;

import org.newdawn.slick.Graphics;

import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.data.IDataOutput;
import fluff.lgs.storage.data.IDataParser;

public abstract class Element {
	
	public static Element focusElement;
	
	public Elements parentElements;
	public IParent parent;
	
	public int x;
	public int y;
	public int width;
	public int height;
	public boolean hovered;
	
	public Element(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public abstract void render(Graphics g, int mouseX, int mouseY);
	
	public boolean hover(int mouseX, int mouseY, boolean found) {
		return within(mouseX, mouseY);
	}
	
	public void update(int delta) {}
	
	public void mousePress(int button, int mouseX, int mouseY) {}
	
	public void mouseRelease(int button, int mouseX, int mouseY) {}
	
	public void mouseDrag(int oldX, int oldY, int mouseX, int mouseY) {}
	
	public boolean mouseScroll(int delta) {
		return false;
	}
	
	public boolean keyPress(int key, char c) {
		return false;
	}
	
	public boolean keyRelease(int key, char c) {
		return false;
	}
	
	public boolean hasFocus() {
		return this.equals(focusElement);
	}
	
	public void setFocus(boolean focus) {
		if (focus) {
			if (focusElement != null) {
				focusElement.setFocus(false);
			}
			focusElement = this;
		} else {
			if (hasFocus()) {
				focusElement = null;
			}
		}
	}
	
	public boolean within(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}

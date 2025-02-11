package fluff.lgs.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;

public class Elements {
	
	public final List<Element> list = new ArrayList<>();
	public final IParent parent;
	
	public Elements(IParent parent) {
		this.parent = parent;
	}
	
	public void render(Graphics g, int mouseX, int mouseY) {
		for (Element e : list) {
			g.pushTransform();
			g.translate(e.getX(), e.getY());
			e.render(g, mouseX - e.getX(), mouseY - e.getY());
			g.popTransform();
		}
	}
	
	public boolean hover(int mouseX, int mouseY, boolean found) {
		for (int i = list.size() - 1; i >= 0; i--) {
			final Element e = list.get(i);
			boolean hovered = e.hover(mouseX - e.getX(), mouseY - e.getY(), found);
			if (e.hovered && (!hovered || found)) {
				e.hovered = false;
			}
			if (!found && hovered) {
				e.hovered = found = true;
			}
		}
		return found;
	}
	
	public void update(int delta) {
		for (Element e : list) {
			e.update(delta);
		}
	}
	
	public void mousePress(int button, int mouseX, int mouseY) {
		for (int i = list.size() - 1; i >= 0; i--) {
			final Element e = list.get(i);
			
			if (e.hovered) {
				e.setFocus(true);
			}
			
			e.mousePress(button, mouseX - e.getX(), mouseY - e.getY());
		}
	}
	
	public void mouseRelease(int button, int mouseX, int mouseY) {
		for (int i = list.size() - 1; i >= 0; i--) {
			final Element e = list.get(i);
			e.mouseRelease(button, mouseX - e.getX(), mouseY - e.getY());
		}
	}
	
	public void mouseDrag(int oldX, int oldY, int mouseX, int mouseY) {
		for (int i = list.size() - 1; i >= 0; i--) {
			final Element e = list.get(i);
			e.mouseDrag(oldX - e.getX(), oldY - e.getY(), mouseX - e.getX(), mouseY - e.getY());
		}
	}
	
	public boolean mouseScroll(int delta) {
		for (int i = list.size() - 1; i >= 0; i--) {
			final Element e = list.get(i);
			if (e.mouseScroll(delta)) return true;
		}
		return false;
	}
	
	public boolean keyPress(int key, char c) {
		for (int i = list.size() - 1; i >= 0; i--) {
			final Element e = list.get(i);
			if (e.keyPress(key, c)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean keyRelease(int key, char c) {
		for (int i = list.size() - 1; i >= 0; i--) {
			final Element e = list.get(i);
			if (e.keyRelease(key, c)) {
				return true;
			}
		}
		return false;
	}
	
	public void add(Element e) {
		e.parentElements = this;
		e.parent = parent;
		
		list.add(e);
	}
	
	public void remove(Element e) {
		list.remove(e);
	}
}

package fluff.lgs.gui.elements;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fluff.lgs.gui.Element;
import fluff.lgs.utils.Colors;
import fluff.lgs.utils.MathUtils;

public class ScrollPanel extends Panel {
	
	public static final int SCROLLBAR_WIDTH = 10;
	
	protected final List<Element> list = new ArrayList<>();
	
	private boolean dragging;
	
	protected float scroll = 0;
	protected int totalHeight = 0;
	
	public ScrollPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	// if we want this to work properly, we need to add/remove the elements with these methods
	public void add(Element e) {
		e.x = 0;
		e.y = getLastY();
		e.width = width - SCROLLBAR_WIDTH;
		
		totalHeight += e.height;
		
		list.add(e);
		elements.add(e);
	}
	
	public void remove(Element e) {
		int found = -1;
		for (int i = list.size() - 1; i >= 0; i--) {
			if (list.get(i).equals(e)) {
				found = i;
				list.remove(i);
				break;
			}
		}
		
		if (found == -1) return;
		
		for (int i = found; i < list.size(); i++) {
			list.get(i).y -= e.height;
		}
		
		totalHeight -= e.height;
		
		final int diff = height - totalHeight;
		if (diff >= 0) {
			scroll = 0;
		} else {
			if (getLastY() + scroll < height) {
				scroll = diff;
			}
		}
		
		elements.remove(e);
	}
	
	public void clear() {
		for (int i = elements.list.size() - 1; i >= 0; i--) {
			remove(elements.list.get(i));
		}
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		renderBackground(g, mouseX, mouseY);
		
		g.pushTransform();
		g.setWorldClip(0, 0, width, height);
		g.translate(0, scroll);
		elements.render(g, mouseX, mouseY - (int) scroll);
		g.clearWorldClip();
		g.popTransform();
		
		renderScrollbar(g, mouseX, mouseY);
	}
	
	@Override
	public boolean hover(int mouseX, int mouseY, boolean found) {
		if (found) {
			this.found = elements.hover(mouseX, mouseY - (int) scroll, true);
			return true;
		}
		
		if (within(mouseX, mouseY)) {
			this.found = elements.hover(mouseX, mouseY - (int) scroll, false);
			return true;
		}
		
		elements.hover(mouseX, mouseY - (int) scroll, true);
		this.found = false;
		
		return false;
	}
	
	protected void renderScrollbar(Graphics g, int mouseX, int mouseY) {
		final float diff = height - totalHeight;
		int scrollbarHeight = height;
		int scrollbarY = 0;
		if (totalHeight > height) {
			scrollbarHeight = height * height / totalHeight;
			scrollbarY = (int) (scroll / diff * (height - scrollbarHeight));
		}
		
		g.setColor(Colors.scrollbar_path);
		g.fillRect(width - SCROLLBAR_WIDTH, 0, SCROLLBAR_WIDTH, height);
		
		g.setColor(Colors.scrollbar);
		g.fillRect(width - SCROLLBAR_WIDTH, scrollbarY, SCROLLBAR_WIDTH, scrollbarHeight);
	}
	
	@Override
	public boolean mouseScroll(int delta) {
		if (elements.mouseScroll(delta)) return true;
		
		if (hovered) {
			final int diff = height - totalHeight;
			
			if (diff >= 0) return false;
			
			int off = 40;
			if (delta < 0) off = -off;
			
			scroll = MathUtils.clampFloat(scroll + off, diff, 0);
			
			return true;
		}
		
		return false;
	}
	
	protected int getLastY() {
		if (list.isEmpty()) return 0;
		
		Element last = list.get(list.size() - 1);
		
		return last.y + last.height;
	}
	
	@Override
	public void mousePress(int button, int mouseX, int mouseY) {
		super.mousePress(button, mouseX, mouseY - (int) scroll);
		
		if (!hovered || found || button != 0 || mouseX < width - SCROLLBAR_WIDTH || totalHeight <= height) return;
		
		final float diff = height - totalHeight;
		final int scrollbarHeight = height * height / totalHeight;
		final int scrollbarY = (int) (scroll / diff * (height - scrollbarHeight));
		
		if (mouseY >= scrollbarY && mouseY < scrollbarY + scrollbarHeight) {
			dragging = true;
		}
	}
	
	@Override
	public void mouseRelease(int button, int mouseX, int mouseY) {
		super.mouseRelease(button, mouseX, mouseY - (int) scroll);
		
		dragging = false;
	}
	
	@Override
	public void mouseDrag(int oldX, int oldY, int mouseX, int mouseY) {
		super.mouseDrag(oldX, oldY - (int) scroll, mouseX, mouseY - (int) scroll);
		
		if (dragging) {
			final float diff = height - totalHeight;
			final int scrollbarHeight = height * height / totalHeight;
			float scrollbarY = scroll / diff * (height - scrollbarHeight);
			scrollbarY += mouseY - oldY;
			
			scroll = MathUtils.clampFloat(scrollbarY / (height - scrollbarHeight) * diff, diff, 0);
		}
	}
	
	public int getContentWidth() {
		return width - SCROLLBAR_WIDTH;
	}
}

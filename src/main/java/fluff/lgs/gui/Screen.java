package fluff.lgs.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import fluff.lgs.ILayer;
import fluff.lgs.LGS;
import fluff.lgs.utils.Colors;

public abstract class Screen implements ILayer, IParent {
	
	public final Elements elements = new Elements(this);
	public Screen parentScreen;
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		renderBackground(g, mouseX, mouseY);
		
		elements.render(g, mouseX, mouseY);
	}
	
	protected void renderBackground(Graphics g, int mouseX, int mouseY) {
		g.setColor(Colors.screen);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	@Override
	public boolean hover(int mouseX, int mouseY, boolean found) {
		return elements.hover(mouseX, mouseY, found);
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
	public boolean captureMouse(int mouseX, int mouseY) {
		return true;
	}
	
	@Override
	public boolean keyPress(int key, char c) {
		if (preKey(key, c)) return true;
		
		return elements.keyPress(key, c);
	}
	
	protected boolean preKey(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			LGS.setScreen(parentScreen);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean keyRelease(int key, char c) {
		return elements.keyRelease(key, c);
	}
	
	@Override
	public int getTotalX() {
		return 0;
	}
	
	@Override
	public int getTotalY() {
		return 0;
	}
	
	@Override
	public int getWidth() {
		return LGS.container().getWidth();
	}
	
	@Override
	public int getHeight() {
		return LGS.container().getHeight();
	}
}

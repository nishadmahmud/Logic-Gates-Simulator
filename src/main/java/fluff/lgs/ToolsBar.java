package fluff.lgs;

import org.newdawn.slick.Graphics;

import fluff.lgs.gui.ActionListener;
import fluff.lgs.gui.Element;
import fluff.lgs.gui.Elements;
import fluff.lgs.gui.IParent;
import fluff.lgs.gui.elements.Button;
import fluff.lgs.gui.elements.CenterPanel;
import fluff.lgs.gui.screens.AboutScreen;
import fluff.lgs.gui.screens.AddScreen;
import fluff.lgs.gui.screens.SaveLoadScreen;
import fluff.lgs.gui.screens.SettingsScreen;
import fluff.lgs.gui.screens.TruthTableScreen;
import fluff.lgs.gui.screens.EquationScreen;
import fluff.lgs.utils.Colors;

public class ToolsBar implements ILayer, IParent {
	
	public final Elements elements = new Elements(this);
	
	private CenterPanel center = new CenterPanel(0, getHeight());
	private int buttons = 0;
	
	public ToolsBar() {
		addButton("Add", e -> {
			LGS.setScreen(new AddScreen());
		});
		addButton("Save/Load", e -> {
			LGS.setScreen(new SaveLoadScreen());
		});
		addButton("Truth Table", e -> {
			TruthTableScreen.showWindow();
		});
		addButton("Equation", e -> {
			EquationScreen.showWindow();
		});
		addButton("Settings", e -> {
			LGS.setScreen(new SettingsScreen());
		});
		
		for (Element e : center.elements.list) {
			center.width += e.width;
		}
		elements.add(center);
	}
	
	public void addButton(String text, ActionListener<? extends Button> action) {
		center.elements.add(new Button(text, 200 * buttons++, 0, 200, getHeight(), action));
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		g.setColor(Colors.toolsBar);
		g.fillRect(0, getY(), getWidth(), getHeight());
		
		g.pushTransform();
		g.translate(0, getY());
		elements.render(g, mouseX, mouseY - getY());
		g.popTransform();
	}
	
	@Override
	public boolean hover(int mouseX, int mouseY, boolean found) {
		return elements.hover(mouseX, mouseY - getY(), found);
	}
	
	@Override
	public void update(int delta) {
		elements.update(delta);
	}
	
	@Override
	public void mousePress(int button, int mouseX, int mouseY) {
		elements.mousePress(button, mouseX, mouseY - getY());
	}
	
	@Override
	public void mouseRelease(int button, int mouseX, int mouseY) {
		elements.mouseRelease(button, mouseX, mouseY - getY());
	}
	
	@Override
	public void mouseDrag(int oldX, int oldY, int mouseX, int mouseY) {
		elements.mouseDrag(oldX, oldY - getY(), mouseX, mouseY - getY());
	}
	
	@Override
	public boolean mouseScroll(int delta) {
		return elements.mouseScroll(delta);
	}
	
	@Override
	public boolean captureMouse(int mouseX, int mouseY) {
		return mouseY >= getY();
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
		return 50;
	}
	
	public int getY() {
		return LGS.container().getHeight() - getHeight();
	}
}

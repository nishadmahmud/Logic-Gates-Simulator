package fluff.lgs.gui.elements;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fluff.lgs.gui.ActionListener;
import fluff.lgs.gui.Element;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.utils.Colors;

public class Button extends Element {
	
	public String text;
	public ActionListener action;
	
	public Button(String text, int x, int y, int width, int height, ActionListener<? extends Button> action) {
		super(x, y, width, height);
		this.text = text;
		this.action = action;
	}
	
	public Button(String text, int x, int y, int width, int height) {
		this(text, x, y, width, height, null);
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		renderBackground(g, mouseX, mouseY);
		
		Fonts.draw(Align.CENTER, text, width / 2, height / 2, Colors.text);
	}
	
	protected void renderBackground(Graphics g, int mouseX, int mouseY) {
		g.setColor(getBorderColor());
		g.fillRect(0, 0, width, height);
		g.setColor(Colors.button);
		g.fillRect(2, 2, width - 4, height - 4);
	}
	
	@Override
	public void mousePress(int button, int mouseX, int mouseY) {
		if (!hovered) return;
		
		onAction();
	}
	
	protected void onAction() {
		if (action != null) action.onAction(this);
	}
	
	protected Color getColor() {
		return Colors.button;
	}
	
	protected Color getBorderColor() {
		return hovered ? Colors.button_border_hover : Colors.button;
	}
	
	protected String getText() {
		return text;
	}
}

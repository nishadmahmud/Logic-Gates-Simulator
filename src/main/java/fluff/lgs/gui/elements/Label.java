package fluff.lgs.gui.elements;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import fluff.lgs.gui.Element;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.utils.Colors;

public class Label extends Element {
	
	private final Font font;
	private final Align alignX;
	private final Align alignY;
	private int alX = 0;
	private int alY = 0;
	
	public String text;
	public Color color;
	
	public Label(String text, int x, int y, int width, int height, Font font, Align alignX, Align alignY) {
		super(x, y, width, height);
		this.text = text;
		this.font = font;
		this.alignX = alignX;
		this.alignY = alignY;
		
		if (alignX == Align.CENTER) {
			alX = width / 2;
		} else if (alignX == Align.END) {
			alX = width;
		}
		if (alignY == Align.CENTER) {
			alY = height / 2;
		} else if (alignY == Align.END) {
			alY = height;
		}
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		Fonts.use(font);
		Fonts.draw(alignX, alignY, text, alX, alY, Colors.text);
		Fonts.reset();
	}
}

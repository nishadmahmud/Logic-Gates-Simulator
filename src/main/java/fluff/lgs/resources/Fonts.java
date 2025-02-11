package fluff.lgs.resources;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import fluff.lgs.LGS;
import fluff.lgs.Main;
import fluff.lgs.utils.Utils;

public class Fonts {
	
	private static final java.awt.Font DEFAULT_AWT_FONT = Utils.lazy(() -> java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("/assets/Consolas.ttf")));
	public static final Font NORMAL = new TrueTypeFont(DEFAULT_AWT_FONT.deriveFont(java.awt.Font.PLAIN, 18), true);
	public static final Font HEADER = new TrueTypeFont(DEFAULT_AWT_FONT.deriveFont(java.awt.Font.BOLD, 40), true);
	public static final Font SMALL = new TrueTypeFont(DEFAULT_AWT_FONT.deriveFont(java.awt.Font.PLAIN, 14), true);
	public static final Font MONOSPACE = new TrueTypeFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 14), true);
	
	private static Font current;
	private static int height;
	
	public static void draw(Align alignX, Align alignY, String text, int x, int y, Color color) {
		if (text == null || text.isEmpty()) return;
		
		current.drawString(alignX.getX(text, x), alignY.getY(text, y), text, color);
	}
	
	public static void draw(Align align, String text, int x, int y, Color color) {
		draw(align, align, text, x, y, color);
	}
	
	public static void draw(String text, int x, int y, Color color) {
		draw(Align.START, text, x, y, color);
	}
	
	public static void use(Font font) {
		current = font;
		height = current.getLineHeight() - Math.round(current.getLineHeight() * 15.0F / 100.0F);
	}
	
	public static void reset() {
		use(NORMAL);
		
		LGS.graphics().setFont(current);
	}
	
	public static int getWidth(String text) {
		return current.getWidth(text);
	}
	
	public static int getHeight() {
		return height;
	}
}

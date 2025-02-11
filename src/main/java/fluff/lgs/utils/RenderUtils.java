package fluff.lgs.utils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fluff.lgs.LGS;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.connection.ConnectionType;
import fluff.lgs.gui.elements.gate.ButtonConnection;

public class RenderUtils {
	
	public static void drawConnection(Graphics g, ButtonConnection b1, ButtonConnection b2, boolean input, boolean output, boolean middle) {
		drawConnection(g, b1, b2.parent.getTotalX() + b2.getX() + 10, b2.parent.getTotalY() + b2.getY() + 10, b2.getValue(), input, output, middle);
	}
	
	public static void drawConnection(Graphics g, ButtonConnection b, float x, float y, LogicalValue value, boolean input, boolean output, boolean middle) {
		final float x1;
		final float y1;
		final float x2;
		final float y2;
		
		if (b.getType() == ConnectionType.INPUT) {
			x1 = x;
			y1 = y;
			x2 = b.parent.getTotalX() + b.getX() + 10;
			y2 = b.parent.getTotalY() + b.getY() + 10;
		} else {
			x1 = b.parent.getTotalX() + b.getX() + 10;
			y1 = b.parent.getTotalY() + b.getY() + 10;
			x2 = x;
			y2 = y;
		}
		
		float w = (x2 - x1) / 2.0F;
		float h = (y2 - y1) / 2.0F;
		if (w < 20.0F) w = 20.0F;
		
		Color color = switch (value) {
			case UNDEFINED -> Colors.connection_undefined_dark;
			case TRUE -> Colors.connection_true_dark;
			case FALSE -> Colors.connection_false_dark;
		};
		
		g.setColor(color);
		g.setLineWidth(LGS.world().scale * 2);
		
		if (input) g.drawLine(x1, y1, x1 + w, y1);
		
		if (middle) {
			g.drawLine(x1 + w, y1, x1 + w, y1 + h);
			g.drawLine(x1 + w, y1 + h, x2 - w, y2 - h);
			g.drawLine(x2 - w, y2 - h, x2 - w, y2);
		}
		
		if (output) g.drawLine(x2 - w, y2, x2, y2);
	}
}

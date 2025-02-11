package fluff.lgs.gui.elements.gate;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fluff.lgs.LGS;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gate.connection.ConnectionType;
import fluff.lgs.gate.connection.IConnection;
import fluff.lgs.gui.elements.Button;
import fluff.lgs.utils.Colors;

public class ButtonConnection extends Button implements IConnection {
	
	private final ConnectionType type;
	public final int index;
	
	public IConnection from;
	
	public boolean render;
	
	public ButtonConnection(ConnectionType type, int index) {
		super(null, 0, 0, GateWindow.CONNECTION_BUTTON_HEIGHT, GateWindow.CONNECTION_BUTTON_HEIGHT);
		this.type = type;
		this.index = index;
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		render(g, getValue());
	}
	
	@Override
	protected void onAction() {
		LGS.world().connect(this);
	}
	
	@Override
	public LogicalValue getValue() {
		return from != null ? from.getValue() : LogicalValue.UNDEFINED;
	}
	
	@Override
	public ConnectionType getType() {
		return type;
	}
	
	protected void render(Graphics g, LogicalValue value) {
		g.setColor(getColor(value));
		g.fillOval(0, 0, width, height);
	}
	
	public void renderTranslated(Graphics g, LogicalValue value) {
		g.pushTransform();
		g.translate(parent.getTotalX() + getX(), parent.getTotalY() + getY());
		render(g, value);
		g.popTransform();
	}
	
	protected Color getColor(LogicalValue value) {
		return hovered ? switch (value) {
					case UNDEFINED -> Colors.connection_undefined_hover;
					case TRUE -> Colors.connection_true_hover;
					case FALSE -> Colors.connection_false_hover;
				} : switch (value) {
					case UNDEFINED -> Colors.connection_undefined;
					case TRUE -> Colors.connection_true;
					case FALSE -> Colors.connection_false;
				};
	}
}

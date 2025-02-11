package fluff.lgs.gui.elements;

import org.newdawn.slick.Graphics;

import fluff.lgs.gate.InputValue;
import fluff.lgs.gate.LogicalValue;
import fluff.lgs.gui.ActionListener;
import fluff.lgs.storage.values.BooleanValue;
import fluff.lgs.utils.Colors;

public class ToggleButton extends Button implements InputValue {
	
	public BooleanValue value;
	
	public ToggleButton(int x, int y, int size, BooleanValue value, ActionListener<? extends ToggleButton> action) {
		super(null, x, y, size * 2, size, action);
		this.value = value;
	}
	
	public ToggleButton(int x, int y, int size, BooleanValue value) {
		this(x, y, size, value, null);
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		int pHeight = height * 3 / 4;
		if (pHeight % 2 == 1) pHeight -= 1;
		int pOff = (height - pHeight) / 2;
		int pWidth = width - pOff * 2;
		
		if (value.get()) {
			g.setColor(Colors.toggle_button_on_dark);
			g.fillRoundRect(pOff, pOff, pWidth, pHeight, pHeight);
			g.setColor(hovered ? Colors.toggle_button_on_hover : Colors.toggle_button_on);
			g.fillOval(width - height, 0, height, height);
		} else {
			g.setColor(Colors.toggle_button_dark);
			g.fillRoundRect(pOff, pOff, pWidth, pHeight, pHeight);
			g.setColor(hovered ? Colors.toggle_button_hover : Colors.toggle_button);
			g.fillOval(0, 0, height, height);
		}
	}
	
	@Override
	protected void onAction() {
		value.set(!value.get());;
		
		super.onAction();
	}
	
	@Override
	public LogicalValue getLogicalValue() {
		return value.get() ? LogicalValue.TRUE : LogicalValue.FALSE;
	}
}

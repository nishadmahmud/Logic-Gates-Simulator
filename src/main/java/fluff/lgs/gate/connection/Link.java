package fluff.lgs.gate.connection;

import fluff.lgs.gui.elements.gate.ButtonConnection;

public class Link {
	
	public final ButtonConnection from;
	public final ButtonConnection to;
	
	Link(ButtonConnection from, ButtonConnection to) {
		this.from = from;
		this.to = to;
	}
}

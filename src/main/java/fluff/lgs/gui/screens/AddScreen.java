package fluff.lgs.gui.screens;

import java.util.function.Predicate;

import fluff.lgs.gate.IGateType;
import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gui.Element;
import fluff.lgs.gui.elements.Button;
import fluff.lgs.gui.elements.Panel;
import fluff.lgs.gui.elements.TextBox;
import fluff.lgs.gui.elements.gate.GateButton;
import fluff.lgs.storage.Worlds;

public class AddScreen extends ScrollMenuScreen {
	
	private final TextBox searchBox;
	
	public AddScreen() {
		super("Add");
		
		Panel p = new Panel(scroll.x, scroll.y, scroll.width, 50);
		scroll.y += p.height;
		scroll.height -= p.height;
		p.elements.add(searchBox = new TextBox(10, 10, p.width - 130, 30));
		searchBox.setTypeAction(this::search);
		searchBox.setPlaceHolder("Search...");
		searchBox.setMaxLength(Worlds.NAME_MAX_LENGTH);
		searchBox.setFocus(true);
		p.elements.add(new Button("Search", p.width - 110, 10, 100, 30, this::search));
		panel.elements.add(p);
		
		fillTypes(null);
	}
	
	private void search(Element e) {
		if (searchBox.getText().isEmpty()) {
			fillTypes(null);
			return;
		}
		
		String text = searchBox.getText().toLowerCase();
		fillTypes(gt -> {
			if (gt.getID().toString().contains(text)) return true;
			
			if (gt.getLine1() != null) {
				String line1 = gt.getLine1().toLowerCase();
				if (line1.contains(text)) {
					return true;
				}
			}
			
			if (gt.getLine2() != null) {
				String line2 = gt.getLine2().toLowerCase();
				if (line2.contains(text)) {
					return true;
				}
			}
			
			return false;
		});
	}
	
	private void fillTypes(Predicate<IGateType> p) {
		scroll.clear();
		
		for (NativeGateType gt : NativeGateType.values()) {
			if (p != null && !p.test(gt)) continue;
			
			scroll.add(new GateButton(gt));
		}
	}
}

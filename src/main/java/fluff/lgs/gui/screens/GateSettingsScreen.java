package fluff.lgs.gui.screens;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fluff.lgs.LGS;
import fluff.lgs.gate.connection.Link;
import fluff.lgs.gui.Element;
import fluff.lgs.gui.elements.Button;
import fluff.lgs.gui.elements.Label;
import fluff.lgs.gui.elements.NicePanel;
import fluff.lgs.gui.elements.TextBox;
import fluff.lgs.gui.elements.gate.ButtonConnection;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.storage.Worlds;

public class GateSettingsScreen extends ScrollMenuScreen {
	
	private final GateWindow gw;
	
	private TextBox nameBox;
	
	public GateSettingsScreen(GateWindow gw) {
		super("Gate Settings");
		this.gw = gw;
		
		NicePanel typePanel = new NicePanel(0, 0, scroll.getContentWidth(), 50, false);
		typePanel.elements.add(new Label("Type", 10, 0, 0, typePanel.height, Fonts.NORMAL, Align.START, Align.CENTER));
		typePanel.elements.add(new Label(gw.gate.type.getID(), typePanel.width - 10, 0, 0, typePanel.height, Fonts.NORMAL, Align.END, Align.CENTER));
		scroll.add(typePanel);
		
		NicePanel namePanel = new NicePanel(0, 0, scroll.getContentWidth(), 50, false);
		namePanel.elements.add(new Label("Name", 10, 0, 0, namePanel.height, Fonts.NORMAL, Align.START, Align.CENTER));
		namePanel.elements.add(nameBox = new TextBox(namePanel.width - 270, 10, 200, 30, this::setWindowName));
		nameBox.setPlaceHolder("Name");
		nameBox.setText(gw.title);
		nameBox.setCursorPosAtEnd();
		nameBox.setMaxLength(Worlds.NAME_MAX_LENGTH);
		namePanel.elements.add(new Button("Set", namePanel.width - 60, 10, 50, 30, this::setWindowName));
		scroll.add(namePanel);
		
		// Input count section (only for gates that support variable inputs)
		if (gw.gate != null && gw.gate.type.getMaxInputs() > gw.gate.type.getMinInputs()) {
			NicePanel inputPanel = new NicePanel(0, 0, scroll.getContentWidth(), 50, false);
			inputPanel.elements.add(new Label("Inputs", 10, 0, 0, inputPanel.height, Fonts.NORMAL, Align.START, Align.CENTER));
			
			// Add number buttons for each possible input count
			int x = 80;
			for (int i = gw.gate.type.getMinInputs(); i <= gw.gate.type.getMaxInputs(); i++) {
				final int count = i;
				Button numButton;
				if (i == gw.getInputCount()) {
					// Create a highlighted button for current input count
					numButton = new Button(String.valueOf(i), x, 10, 30, 30, e -> {
						gw.updateInputCount(count);
						LGS.setScreen(null);
					}) {
						@Override
						public void render(Graphics g, int mouseX, int mouseY) {
							g.setColor(new Color(100, 150, 255));
							g.fillRect(getTotalX(), getTotalY(), width, height);
							super.render(g, mouseX, mouseY);
						}
					};
				} else {
					// Normal button for other input counts
					numButton = new Button(String.valueOf(i), x, 10, 30, 30, e -> {
						gw.updateInputCount(count);
						LGS.setScreen(null);
					});
				}
				inputPanel.elements.add(numButton);
				x += 40;
			}
			scroll.add(inputPanel);
		}
		
		NicePanel removePanel = new NicePanel(0, 0, scroll.getContentWidth(), 50, false);
		horizontal(removePanel, removePanel.width,
				new Button("Remove Gate", 0, 0, 0, 0, this::removeGate),
				new Button("Remove Connections", 0, 0, 0, 0, this::removeConnections)
				);
		scroll.add(removePanel);
	}
	
	protected void setWindowName(Element e) {
		if (nameBox.getText().isBlank()) return;
		
		gw.title = nameBox.getText();
	}
	
	protected void removeGate(Element e) {
		removeConnections(e);
		
		LGS.world().gates.remove(gw);
		LGS.setScreen(null);
	}
	
	protected void removeConnections(Element e) {
		for (ButtonConnection to : gw.gate.inputs) {
			Link link = LGS.world().connections.findTo(to);
			LGS.world().connections.remove(link);
		}
		for (ButtonConnection from : gw.gate.outputs) {
			for (Link link : LGS.world().connections.findFrom(from)) {
				LGS.world().connections.remove(link);
			}
		}
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, LGS.container().getWidth(), LGS.container().getHeight());
		
		super.render(g, mouseX, mouseY);
	}
}

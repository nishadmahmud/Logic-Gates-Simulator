package fluff.lgs.gui.elements.gate;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import fluff.lgs.LGS;
import fluff.lgs.gate.IGateType;
import fluff.lgs.gui.ActionListener;
import fluff.lgs.gui.Screen;
import fluff.lgs.gui.elements.Button;
import fluff.lgs.gui.elements.IconButton;
import fluff.lgs.gui.elements.Label;
import fluff.lgs.gui.elements.NicePanel;
import fluff.lgs.gui.screens.WorldSettingsScreen;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.resources.Icons;
import fluff.lgs.storage.impl.WorldStorage;
import fluff.lgs.utils.Colors;

public class WorldButton extends NicePanel {
	
	private final Screen screen;
	private final WorldStorage ws;
	
	public WorldButton(Screen screen, int width, WorldStorage ws) {
		super(0, 0, width, 50, true);
		this.screen = screen;
		this.ws = ws;
		
		elements.add(new Button("Save", width - 120, 10, 50, 30, this::save));
		elements.add(new Button("Load", width - 60, 10, 50, 30, this::load));
		elements.add(new IconButton(Icons.SETTINGS, width - 160, 10, 30, 30, true, this::settings));
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		super.render(g, mouseX, mouseY);
		
		Fonts.draw(Align.START, Align.CENTER, ws.getWorld().name, 10, height / 2, Colors.text);
	}
	
	@Override
	protected Color getNiceColor() {
		return ws.getWorld().equals(LGS.world()) ? allowHover && hovered ? Colors.current_world_hover : Colors.current_world : super.getNiceColor();
	}
	
	protected void save(Button b) {
		LGS.WORLDS.save(LGS.world(), ws.file);
	}
	
	protected void load(Button b) {
		LGS.WORLDS.load(ws.getWorld(), ws.file);
	}
	
	protected void settings(IconButton b) {
		LGS.setScreen(new WorldSettingsScreen(screen, ws));
	}
	
	@Override
	public void mousePress(int button, int mouseX, int mouseY) {
		super.mousePress(button, mouseX, mouseY);
		
		if (found || !hovered) return;
		
		LGS.setWorld(ws.getWorld());
		LGS.setScreen(null);
	}
}

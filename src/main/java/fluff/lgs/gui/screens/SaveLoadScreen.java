package fluff.lgs.gui.screens;

import java.util.Map;

import fluff.lgs.LGS;
import fluff.lgs.World;
import fluff.lgs.gui.Element;
import fluff.lgs.gui.elements.Button;
import fluff.lgs.gui.elements.Label;
import fluff.lgs.gui.elements.Panel;
import fluff.lgs.gui.elements.gate.WorldButton;
import fluff.lgs.storage.Worlds;
import fluff.lgs.storage.impl.WorldStorage;
import fluff.lgs.utils.Utils;

public class SaveLoadScreen extends ScrollMenuScreen {
	
	public SaveLoadScreen() {
		super("Save/Load");
		
		Panel p = new Panel(scroll.x, scroll.y, scroll.width, 50);
		scroll.y += p.height;
		scroll.height -= p.height;
		horizontal(p, p.width,
				new Button("New", 0, 0, 0, 0, e -> {
					LGS.setWorld(LGS.WORLDS.newWorld());
					LGS.setScreen(null);
				}),
				new Button("Export", 0, 0, 0, 0, e -> {
					if (LGS.WORLDS.save(LGS.world(), null)) {
						LGS.setScreen(null);
					}
				}),
				new Button("Import", 0, 0, 0, 0, e -> {
					World w = LGS.WORLDS.newWorld();
					if (LGS.WORLDS.load(w, null)) {
						LGS.setWorld(w);
						LGS.setScreen(null);
					}
				})
		);
		panel.elements.add(p);
		
		for (WorldStorage ws : LGS.WORLDS.worlds) {
			scroll.add(new WorldButton(this, scroll.getContentWidth(), ws));
		}
	}
}

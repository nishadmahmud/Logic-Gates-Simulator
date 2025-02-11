package fluff.lgs.gui.screens;

import fluff.lgs.VersionChecker;
import fluff.lgs.gui.elements.Button;
import fluff.lgs.gui.elements.Label;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;

public class AboutScreen extends MenuScreen {
	
	public AboutScreen() {
		super("About", 350, 180);
		
		String text = """
				Made with the help of
				LWJGL, Slick2D and JNA by musca
				Enjoy! :)
				""";
		
		String[] split = text.split("\n");
		for (int i = 0; i < split.length; i++) {
			panel.elements.add(new Label(split[i], 0, i * 20, panel.width, 20, Fonts.NORMAL, Align.CENTER, Align.START));
		}
		
		panel.elements.add(new Label("Version: 1", 0, panel.height - 30, 0, 30, Fonts.NORMAL, Align.START, Align.CENTER));
		panel.elements.add(new Button("Open LGS Github", panel.width - 200, panel.height - 30, 200, 30, e -> VersionChecker.openGithub()));
	}
}

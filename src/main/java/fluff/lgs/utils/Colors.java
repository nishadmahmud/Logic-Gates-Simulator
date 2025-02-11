package fluff.lgs.utils;

import org.newdawn.slick.Color;

public class Colors {
	
	public static final Color white = gray(250);
	public static final Color text = white;
	public static final Color text_dark = darker(text, 60);
	public static final Color accent = rgb(50, 150, 240);
	
	public static final Color world = gray(20);
	public static final Color screen = gray(10, 100);
	
	public static final Color toolsBar = gray(40);
	
	public static final Color panel = gray(50);
	
	public static final Color scrollbar_path = darker(panel, 20);
	public static final Color scrollbar = lighter(panel, 20);
	
	public static final Color window_border = darker(panel, 20);
	public static final Color window_title = lighter(panel, 20);
	
	public static final Color spacer = lighter(panel, 130);
	public static final Color icon_button = lighter(panel, 130);
	
	public static final Color button = darkerPercent(accent, 0.2F);
	public static final Color button_border_hover = white;
	
	public static final Color toggle_button = lighter(panel, 80);
	public static final Color toggle_button_hover = lighter(toggle_button, 20);
	public static final Color toggle_button_dark = darker(toggle_button, 20);
	
	public static final Color toggle_button_on = accent;
	public static final Color toggle_button_on_hover = lighterPercent(toggle_button_on, 0.2F);
	public static final Color toggle_button_on_dark = darkerPercent(toggle_button_on, 0.3F);
	
	public static final Color textbox = gray(40);
	public static final Color textbox_border = lighter(textbox, 60);
	public static final Color textbox_border_focus = accent;
	
	public static final Color nice_panel = lighter(panel, 30);
	public static final Color nice_panel_hovered = lighter(nice_panel, 20);
	public static final Color nice_panel_border = darker(nice_panel, 40);
	
	public static final Color connection_undefined = rgb(100, 50, 200);
	public static final Color connection_undefined_hover = lighterPercent(connection_undefined, 0.5F);
	public static final Color connection_undefined_dark = darkerPercent(connection_undefined, 0.4F);
	
	public static final Color connection_true = rgb(20, 180, 20);
	public static final Color connection_true_hover = lighterPercent(connection_true, 0.5F);
	public static final Color connection_true_dark = darkerPercent(connection_true, 0.4F);
	
	public static final Color connection_false = rgb(180, 20, 20);
	public static final Color connection_false_hover = lighterPercent(connection_false, 0.5F);
	public static final Color connection_false_dark = darkerPercent(connection_false, 0.4F);
	
	public static final Color current_world = darkerPercent(accent, 0.5F);
	public static final Color current_world_hover = lighterPercent(current_world, 0.1F);
	
	public static Color rgba(int r, int g, int b, int a) {
		return new Color(MathUtils.clampInt(r, 0, 255), MathUtils.clampInt(g, 0, 255), MathUtils.clampInt(b, 0, 255), MathUtils.clampInt(a, 0, 255));
	}
	
	public static Color rgb(int r, int g, int b) {
		return rgba(r, g, b, 255);
	}
	
	public static Color gray(int gray, int a) {
		return rgba(gray, gray + 3, gray + 6, a);
	}
	
	public static Color gray(int gray) {
		return gray(gray, 255);
	}
	
	public static Color lighter(Color base, int amount) {
		return rgb(base.getRed() + amount, base.getGreen() + amount, base.getBlue() + amount);
	}
	
	public static Color darker(Color base, int amount) {
		return lighter(base, -amount);
	}
	
	public static Color lighterPercent(Color base, float percent) {
		return rgb(base.getRed() + (int) (base.getRed() * percent), base.getGreen() + (int) (base.getGreen() * percent), base.getBlue() + (int) (base.getBlue() * percent));
	}
	
	public static Color darkerPercent(Color base, float percent) {
		return lighterPercent(base, -percent);
	}
}

package fluff.lgs.utils;

import java.awt.Desktop;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;

import fluff.lgs.Main;
import fluff.lgs.filechooser.FCFilter;
import fluff.lgs.filechooser.JnaFileChooser;
import fluff.lgs.filechooser.JnaFileChooser.Mode;

public class Utils {
	
	private static final Field _width = rField(GameContainer.class, "width");
	private static final Field _height = rField(GameContainer.class, "height");
	
	private static final Method _initGL = rMethod(GameContainer.class, "initGL");
	private static final Method _enterOrtho = rMethod(GameContainer.class, "enterOrtho");
	
	public static File fileChooser(FCFilter filter, boolean save) {
		JnaFileChooser fc = new JnaFileChooser();
		fc.setMode(Mode.Files);
		
		fc.addFilter(filter);
		fc.addFilter("All Files", "*");
		
		if (save) {
			if (fc.showSaveDialog(null)) {
				File f = fc.getSelectedFile();
				if (f == null) return null;
				if (!f.getName().endsWith(filter.getExtension())) {
					f = new File(f.getAbsolutePath() + "." + filter.getExtension());
				}
				return f;
			}
		} else {
			if (fc.showOpenDialog(null)) {
				return fc.getSelectedFile();
			}
		}
        return null;
	}
	
	public static void openURL(String url) {
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI(url));
			} else {
				Runtime.getRuntime().exec("xdg-open " + url);
			}
        } catch (Exception e) {
        	Sys.alert("Error", e.getMessage());
        }
	}
	
	public static void resize() {
		if (Display.getWidth() != Main.GAME.getWidth() || Display.getHeight() != Main.GAME.getHeight()) {
			try {
				_width.set(Main.GAME, Display.getWidth());
				_height.set(Main.GAME, Display.getHeight());
				
				_initGL.invoke(Main.GAME);
				_enterOrtho.invoke(Main.GAME);
			} catch (Exception e) {}
		}
	}
	
	public static Field rField(Class<?> clazz, String name) {
		try {
			Field f = clazz.getDeclaredField(name);
			f.setAccessible(true);
			return f;
		} catch (NoSuchFieldException | SecurityException e) {}
		return null;
	}
	
	public static Method rMethod(Class<?> clazz, String name) {
		try {
			Method m = clazz.getDeclaredMethod(name);
			m.setAccessible(true);
			return m;
		} catch (NoSuchMethodException | SecurityException e) {}
		return null;
	}
	
	public static <V> V lazy(Lazy<V> load) {
		try {
			return load.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static interface Lazy<V> {
		
		V load() throws Exception;
	}
}

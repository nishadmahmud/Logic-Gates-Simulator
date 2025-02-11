package fluff.lgs;

import javax.swing.UIManager;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.ResourceLoader;

import fluff.lgs.natives.LWJGLNatives;
import fluff.lgs.resources.Fonts;
import fluff.lgs.utils.LGSResourceLoader;
import fluff.lgs.utils.Utils;

public class Main {
	
	static {
		LWJGLNatives.load();
	}
	
	public static final AppGameContainer GAME = Utils.lazy(() -> new AppGameContainer(LGS.INSTANCE));
	
	public static final int FPS_LOCK = 120;
	
	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        
			ResourceLoader.addResourceLocation(new LGSResourceLoader());
			Input.disableControllers();
			
			GAME.setIcons(new String[] { "/assets/icon_32.png", "/assets/icon_64.png" });
			GAME.setDisplayMode(1920 * 3 / 4, 1080 * 3 / 4, false);
			GAME.setTargetFrameRate(FPS_LOCK);
			GAME.setVSync(false);
			Display.setResizable(true);
			GAME.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

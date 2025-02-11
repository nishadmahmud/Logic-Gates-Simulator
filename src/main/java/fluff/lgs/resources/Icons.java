package fluff.lgs.resources;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import fluff.lgs.utils.Utils;

public class Icons {
	
	// gates
	public static final Image BUFFER = get("/assets/gates/buffer.png");
	public static final Image NOT = get("/assets/gates/not.png");
	public static final Image AND = get("/assets/gates/and.png");
	public static final Image NAND = get("/assets/gates/nand.png");
	public static final Image OR = get("/assets/gates/or.png");
	public static final Image NOR = get("/assets/gates/nor.png");
	public static final Image XOR = get("/assets/gates/xor.png");
	public static final Image XNOR = get("/assets/gates/xnor.png");
	
	// other
	public static final Image SETTINGS = get("/assets/settings.png");
	public static final Image BACK = get("/assets/back.png");
	
	public static Image get(String path) {
		try {
			return new Image(path);
		} catch (SlickException e) {}
		return null;
	}
}

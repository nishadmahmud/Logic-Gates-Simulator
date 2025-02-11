package fluff.lgs;

import org.newdawn.slick.Graphics;

public interface ILayer {
	
	void render(Graphics g, int mouseX, int mouseY);
	
	boolean hover(int mouseX, int mouseY, boolean found);
	
	void update(int delta);
	
	void mousePress(int button, int mouseX, int mouseY);
	
	void mouseRelease(int button, int mouseX, int mouseY);
	
	void mouseDrag(int oldX, int oldY, int mouseX, int mouseY);
	
	boolean mouseScroll(int delta);
	
	boolean captureMouse(int mouseX, int mouseY);
	
	boolean keyPress(int key, char c);
	
	boolean keyRelease(int key, char c);
}

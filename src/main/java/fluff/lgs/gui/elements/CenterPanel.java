package fluff.lgs.gui.elements;

public class CenterPanel extends Panel {
	
	public CenterPanel(int width, int height) {
		super(0, 0, width, height);
	}
	
	@Override
	public int getX() {
		return parent.getWidth() / 2 - width / 2;
	}
	
	@Override
	public int getY() {
		return parent.getHeight() / 2 - height / 2;
	}
}

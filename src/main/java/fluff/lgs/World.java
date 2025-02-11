package fluff.lgs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fluff.lgs.gate.connection.ConnectionType;
import fluff.lgs.gate.connection.Connections;
import fluff.lgs.gate.connection.Link;
import fluff.lgs.gate.connection.OverflowChecker;
import fluff.lgs.gate.impl.OutputGate;
import fluff.lgs.gui.Elements;
import fluff.lgs.gui.IParent;
import fluff.lgs.gui.WindowRegistry;
import fluff.lgs.gui.elements.Window;
import fluff.lgs.gui.elements.gate.ButtonConnection;
import fluff.lgs.gui.elements.gate.GateElements;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.resources.Fonts;
import fluff.lgs.storage.impl.Config;
import fluff.lgs.utils.Colors;
import fluff.lgs.utils.RenderUtils;

public class World implements ILayer, IParent {
	
	public final Elements overlay = new Elements(this);
	public final GateElements gates = new GateElements(this);
	public final WindowRegistry windowReg = new WindowRegistry();
	public final Connections connections = new Connections(this);
	
	private boolean found;
	private boolean dragging;
	
	public ButtonConnection currentConnection;
	
	public float viewX = 0;
	public float viewY = 0;
	public float scale = 1.0F;
	
	public String name;
	
	public World(String name) {
		this.name = name;
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		final int width = LGS.container().getWidth();
		final int height = LGS.container().getHeight();
		
		final float scaledViewX = getScaledViewX();
		final float scaledViewY = getScaledViewY();
		
		g.setColor(Colors.world);
		g.fillRect(0, 0, width, height);
		
		g.pushTransform();
		g.translate(scaledViewX, scaledViewY);
		g.scale(scale, scale);
		
		renderWorld(g, getScaledPosX(mouseX), getScaledPosY(mouseY));
		
		g.popTransform();
		
		renderInfo(g, mouseX, mouseY, scaledViewX, scaledViewY);
	}
	
	private void renderWorld(Graphics g, int mouseX, int mouseY) {
		gates.render(g, mouseX, mouseY);
		
		if (currentConnection != null) {
			RenderUtils.drawConnection(g, currentConnection, mouseX, mouseY, currentConnection.getValue(), true, true, true);
			currentConnection.renderTranslated(g, currentConnection.getValue());
		}
		
		overlay.render(g, mouseX, mouseY);
	}
	
	private void renderInfo(Graphics g, int mouseX, int mouseY, float scaledViewX, float scaledViewY) {
		if (!Config.SHOW_WORLD_INFO.get()) return;
		
		final int xOff = 10;
		final int yOff = 10;
		int i = 0;
		
		if (VersionChecker.isOutdated()) {
			Fonts.draw("New version is available on github.", xOff, yOff + 20 * i++, Color.orange);
		}
		
		// Fonts.draw("FPS: " + Main.GAME.getFPS() + " (lock @ " + Main.FPS_LOCK + ")", xOff, yOff + 20 * i++, Colors.text);
		Fonts.draw("Project: " + name, xOff, yOff + 20 * i++, Colors.text);
		Fonts.draw("Zoom: " + (Math.round(scale * 10000) / 100) + "%", xOff, yOff + 20 * i++, Colors.text);
		Fonts.draw("X: " + (Math.round(-scaledViewX * 100) / 100), xOff, yOff + 20 * i++, Colors.text);
		Fonts.draw("Y: " + (Math.round(-scaledViewY * 100) / 100), xOff, yOff + 20 * i++, Colors.text);
	}
	
	public void connect(ButtonConnection button) {
		if (currentConnection == null) {
			currentConnection = button;
			return;
		}
		
		if (currentConnection.equals(button)) {
			currentConnection = null;
			return;
		}
		
		connections.addOrRemove(currentConnection, button);
		currentConnection = null;
	}
	
	@Override
	public boolean hover(int mouseX, int mouseY, boolean found) {
		this.found = overlay.hover(getScaledPosX(mouseX), getScaledPosY(mouseY), found);
		return this.found = gates.hover(getScaledPosX(mouseX), getScaledPosY(mouseY), this.found);
	}
	
	@Override
	public void update(int delta) {
		gates.update(delta);
		overlay.update(delta);
	}
	
	@Override
	public void mousePress(int button, int mouseX, int mouseY) {
		overlay.mousePress(button, getScaledPosX(mouseX), getScaledPosY(mouseY));
		gates.mousePress(button, getScaledPosX(mouseX), getScaledPosY(mouseY));
		
		if (found) return;
		
		if (button == 0) dragging = true;
	}
	
	@Override
	public void mouseRelease(int button, int mouseX, int mouseY) {
		overlay.mouseRelease(button, getScaledPosX(mouseX), getScaledPosY(mouseY));
		gates.mouseRelease(button, getScaledPosX(mouseX), getScaledPosY(mouseY));
		
		if (button == 0) dragging = false;
	}
	
	@Override
	public void mouseDrag(int oldX, int oldY, int mouseX, int mouseY) {
		overlay.mouseDrag(getScaledPosX(oldX), getScaledPosY(oldY), getScaledPosX(mouseX), getScaledPosY(mouseY));
		gates.mouseDrag(getScaledPosX(oldX), getScaledPosY(oldY), getScaledPosX(mouseX), getScaledPosY(mouseY));
		
		if (found || !dragging) return;
		
		int dx = mouseX - oldX;
		int dy = mouseY - oldY;
		
		viewX += dx / scale;
		viewY += dy / scale;
	}
	
	@Override
	public boolean mouseScroll(int delta) {
		if (overlay.mouseScroll(delta)) return true;
		if (gates.mouseScroll(delta)) return true;
		
		if (found) return false;
		
		float d = 0.05F;
		if (delta < 0) d = -d;
		
		float newScale = Math.round((scale + d) * 100.0F) / 100.0F;
		if (newScale >= 0.25F && newScale <= 1.5F) {
			scale = newScale;
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean captureMouse(int mouseX, int mouseY) {
		return true;
	}
	
	@Override
	public boolean keyPress(int key, char c) {
		if (overlay.keyPress(key, c)) return true;
		return gates.keyPress(key, c);
	}
	
	@Override
	public boolean keyRelease(int key, char c) {
		if (overlay.keyRelease(key, c)) return true;
		return gates.keyRelease(key, c);
	}
	
	@Override
	public int getTotalX() {
		return 0;
	}
	
	@Override
	public int getTotalY() {
		return 0;
	}
	
	@Override
	public int getWidth() {
		return LGS.container().getWidth();
	}
	
	@Override
	public int getHeight() {
		return LGS.container().getHeight();
	}
	
	public int getCenterX() {
		return getScaledPosX(LGS.container().getWidth() / 2);
	}
	
	public int getCenterY() {
		return getScaledPosY(LGS.container().getHeight() / 2);
	}
	
	public int getScaledPosX(int posX) {
		return (int) ((posX - getScaledViewX()) / scale);
	}
	
	public int getScaledPosY(int posY) {
		return (int) ((posY - getScaledViewY()) / scale);
	}
	
	public float getScaledViewX() {
		final int width = LGS.container().getWidth();
		final float scaledWidth = (width * scale - width) / 2.0F;
		final float scaledViewX = viewX * scale - scaledWidth;
		return scaledViewX;
	}
	
	public float getScaledViewY() {
		final int height = LGS.container().getHeight();
		final float scaledHeight = (height * scale - height) / 2.0F;
		final float scaledViewY = viewY * scale - scaledHeight;
		return scaledViewY;
	}
}

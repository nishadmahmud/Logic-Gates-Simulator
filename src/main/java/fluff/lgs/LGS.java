package fluff.lgs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import fluff.lgs.gate.connection.OverflowChecker;
import fluff.lgs.gui.Screen;
import fluff.lgs.resources.Fonts;
import fluff.lgs.storage.Worlds;
import fluff.lgs.utils.Utils;
import fluff.lgs.clipboard.CircuitClipboard;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.gui.Element;

public class LGS extends BasicGame {
	
	public static final LGS INSTANCE = new LGS();
	public static final Worlds WORLDS = new Worlds();
	
	private GameContainer container;
	private Graphics graphics;
	
	private ILayer world;
	private ILayer toolsBar;
	private ILayer screen;
	
	private Input keyboard;
	
	// Add new fields for box selection
	private boolean boxSelectMode = false;
	private boolean isBoxSelecting = false;
	private int boxStartX, boxStartY;
	private int boxEndX, boxEndY;
	
	public LGS() {
		super("Logic Gates Simulator");
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		this.keyboard = container.getInput();
		
		container.setShowFPS(false);
		container.getInput().addMouseListener(this);
		
		VersionChecker.check();
		
		this.world = WORLDS.newWorld();
		this.toolsBar = new ToolsBar();
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		graphics = g;
		
		g.setAntiAlias(true);
		
		Fonts.reset();
		
		final int mouseX = container.getInput().getMouseX();
		final int mouseY = container.getInput().getMouseY();
		
		boolean found = false;
		if (screen != null) {
			screen.hover(mouseX, mouseY, found);
			found = true;
		}
		if (toolsBar.hover(mouseX, mouseY, found)) {
			found = true;
		}
		if (world.hover(mouseX, mouseY, found)) {
			found = true;
		}
		
		world.render(g, mouseX, mouseY);
		toolsBar.render(g, mouseX, mouseY);
		if (screen != null) screen.render(g, mouseX, mouseY);
		
		// Draw selection box if in box select mode
		if (isBoxSelecting) {
			g.setColor(org.newdawn.slick.Color.blue);
			g.setLineWidth(1);
			int x = Math.min(boxStartX, boxEndX);
			int y = Math.min(boxStartY, boxEndY);
			int width = Math.abs(boxEndX - boxStartX);
			int height = Math.abs(boxEndY - boxStartY);
			g.drawRect(x, y, width, height);
			
			// Draw semi-transparent fill
			org.newdawn.slick.Color fillColor = new org.newdawn.slick.Color(0.2f, 0.2f, 1f, 0.1f);
			g.setColor(fillColor);
			g.fillRect(x, y, width, height);
		}
		// Show indicator when box select mode is active but not currently selecting
		else if (boxSelectMode) {
			String text = "Box Select Mode - Click and drag to select";
			// Position above toolbar (50 pixels is toolbar height)
			int toolbarHeight = 50;
			int padding = 10;
			g.setColor(org.newdawn.slick.Color.white); // Use toolbar text color
			g.drawString(text, padding, container.getHeight() - toolbarHeight - padding * 2);
		}
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Utils.resize();
		
		world.update(delta);
		toolsBar.update(delta);
		if (screen != null) screen.update(delta);
		
		// Handle copy/paste shortcuts only
		if (keyboard.isKeyDown(Input.KEY_LCONTROL)) {
			if (keyboard.isKeyPressed(Input.KEY_C)) {
				// Copy
				List<GateWindow> selectedGates = GateWindow.getSelectedGates();
				if (!selectedGates.isEmpty()) {
					CircuitClipboard.getInstance().copyGates(selectedGates);
				}
			} 
			else if (keyboard.isKeyPressed(Input.KEY_V)) {
				// Paste
				List<GateWindow> pastedGates = CircuitClipboard.getInstance()
					.pasteGates(20, 20);  // Offset from original position
				for (GateWindow gate : pastedGates) {
					if (world instanceof World) {
						((World)world).gates.add(gate);
					}
				}
			}
		}
	}
	
	@Override
	public void mousePressed(int button, int mouseX, int mouseY) {
		// Only handle box selection if we're in box select mode and in the world area
		if (boxSelectMode && button == 0 && 
			screen == null && 
			!toolsBar.captureMouse(mouseX, mouseY)) {
			isBoxSelecting = true;
			boxStartX = mouseX;
			boxStartY = mouseY;
			boxEndX = mouseX;
			boxEndY = mouseY;
			return;
		}
		
		boolean captured = false;
		
		// Handle screen if it exists
		if (screen != null) {
			if (screen.captureMouse(mouseX, mouseY)) {
				screen.mousePress(button, mouseX, mouseY);
				captured = true;
			}
		}
		
		// Handle tools bar
		if (!captured && toolsBar.captureMouse(mouseX, mouseY)) {
			toolsBar.mousePress(button, mouseX, mouseY);
			captured = true;
		}
		
		// Handle world and deselection
		if (!captured) {
			if (world.captureMouse(mouseX, mouseY)) {
				world.mousePress(button, mouseX, mouseY);
			}
			
			// If clicked in world area but not on a gate, clear selection
			if (button == 0 && !GateWindow.isMouseOverSelectedGate(mouseX, mouseY)) {
				GateWindow.clearSelection();
			}
		}
	}
	
	@Override
	public void mouseReleased(int button, int mouseX, int mouseY) {
		if (isBoxSelecting && button == 0) {
			isBoxSelecting = false;
			selectGatesInBox();
			return;
		}
		
		boolean captured = false;
		
		if (screen != null) {
			if (screen.captureMouse(mouseX, mouseY)) {
				screen.mouseRelease(button, mouseX, mouseY);
				captured = true;
			}
		}
		
		if (!captured && toolsBar.captureMouse(mouseX, mouseY)) {
			toolsBar.mouseRelease(button, mouseX, mouseY);
			captured = true;
		}
		
		if (!captured && world.captureMouse(mouseX, mouseY)) {
			world.mouseRelease(button, mouseX, mouseY);
			captured = true;
		}
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (isBoxSelecting) {
			boxEndX = newx;
			boxEndY = newy;
			return;
		}
		
		boolean captured = false;
		
		if (!captured && screen != null) {
			screen.mouseDrag(oldx, oldy, newx, newy);
			captured = true;
		}
		
		if (!captured && toolsBar.captureMouse(oldx, oldy)) {
			toolsBar.mouseDrag(oldx, oldy, newx, newy);
			captured = true;
		}
		
		if (!captured && world.captureMouse(oldx, oldy)) {
			world.mouseDrag(oldx, oldy, newx, newy);
			captured = true;
		}
	}
	
	@Override
	public void mouseWheelMoved(int change) {
		final int mouseX = container.getInput().getMouseX();
		final int mouseY = container.getInput().getMouseY();
		
		boolean captured = false;
		
		if (!captured && screen != null) {
			screen.mouseScroll(change);
			captured = true;
		}
		
		if (!captured && toolsBar.captureMouse(mouseX, mouseY)) {
			toolsBar.mouseScroll(change);
			captured = true;
		}
		
		if (!captured && world.captureMouse(mouseX, mouseY)) {
			world.mouseScroll(change);
			captured = true;
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		// Handle box select toggle
		if (key == Input.KEY_B && keyboard.isKeyDown(Input.KEY_LCONTROL)) {
			// Only toggle if we're not currently in a screen and not over toolbar
			if (screen == null && !toolsBar.captureMouse(container.getInput().getMouseX(), container.getInput().getMouseY())) {
				boxSelectMode = !boxSelectMode;
				isBoxSelecting = false; // Reset this flag when toggling mode
				System.out.println("Box select mode toggled: " + boxSelectMode); // Debug print
				return;
			}
		}

		// Rest of key handling...
		boolean captured = false;
		
		if (!captured && screen != null) {
			captured = screen.keyPress(key, c);
		}
		
		if (!captured) {
			captured = toolsBar.keyPress(key, c);
		}
		
		if (!captured) {
			captured = world.keyPress(key, c);
		}
	}
	
	@Override
	public void keyReleased(int key, char c) {
		boolean captured = false;
		
		if (!captured && screen != null) {
			captured = screen.keyRelease(key, c);
		}
		
		if (!captured) {
			captured = toolsBar.keyRelease(key, c);
		}
		
		if (!captured) {
			captured = world.keyRelease(key, c);
		}
	}
	
	public static void setScreen(Screen screen) {
		INSTANCE.screen = screen;
	}
	
	public static void setWorld(World world) {
		INSTANCE.world = world;
	}
	
	// STATIC GETTERS
	
	public static GameContainer container() {
		return INSTANCE.container;
	}
	
	public static Graphics graphics() {
		return INSTANCE.graphics;
	}
	
	public static World world() {
		return (World) INSTANCE.world;
	}
	
	public static ToolsBar toolsBar() {
		return (ToolsBar) INSTANCE.toolsBar;
	}
	
	public static Screen screen() {
		return (Screen) INSTANCE.screen;
	}
	
	private void selectGatesInBox() {
		// Convert screen coordinates to world coordinates using world's view position
		World currentWorld = (World)world;
		int worldStartX = boxStartX - (int)currentWorld.viewX;
		int worldStartY = boxStartY - (int)currentWorld.viewY;
		int worldEndX = boxEndX - (int)currentWorld.viewX;
		int worldEndY = boxEndY - (int)currentWorld.viewY;
		
		// Also need to account for scale
		float scale = currentWorld.scale;
		worldStartX /= scale;
		worldStartY /= scale;
		worldEndX /= scale;
		worldEndY /= scale;
		
		// Get bounds
		int left = Math.min(worldStartX, worldEndX);
		int right = Math.max(worldStartX, worldEndX);
		int top = Math.min(worldStartY, worldEndY);
		int bottom = Math.max(worldStartY, worldEndY);
		
		// Clear existing selection if not holding Ctrl
		if (!keyboard.isKeyDown(Input.KEY_LCONTROL)) {
			GateWindow.clearSelection();
		}
		
		// Select all gates within the box
		for (Element element : world().gates.list) {
			if (element instanceof GateWindow gate) {
				if (gate.x >= left && gate.x + gate.width <= right &&
					gate.y >= top && gate.y + gate.height <= bottom) {
					gate.setSelected(true);
				}
			}
		}
		
		// Exit box select mode after selection
		boxSelectMode = false;
	}
}

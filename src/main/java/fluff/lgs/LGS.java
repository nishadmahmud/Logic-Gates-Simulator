package fluff.lgs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

public class LGS extends BasicGame {
	
	public static final LGS INSTANCE = new LGS();
	public static final Worlds WORLDS = new Worlds();
	
	private GameContainer container;
	private Graphics graphics;
	
	private ILayer world;
	private ILayer toolsBar;
	private ILayer screen;
	
	public LGS() {
		super("Logic Gates Simulator");
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		
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
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Utils.resize();
		
		world.update(delta);
		toolsBar.update(delta);
		if (screen != null) screen.update(delta);
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		boolean captured = false;
		
		if (!captured && screen != null) {
			screen.mousePress(button, x, y);
			captured = true;
		}
		
		if (!captured && toolsBar.captureMouse(x, y)) {
			toolsBar.mousePress(button, x, y);
			captured = true;
		}
		
		if (!captured && world.captureMouse(x, y)) {
			world.mousePress(button, x, y);
			captured = true;
		}
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		boolean captured = false;
		
		if (!captured && screen != null) {
			screen.mouseRelease(button, x, y);
			captured = true;
		}
		
		if (!captured && toolsBar.captureMouse(x, y)) {
			toolsBar.mouseRelease(button, x, y);
			captured = true;
		}
		
		if (!captured && world.captureMouse(x, y)) {
			world.mouseRelease(button, x, y);
			captured = true;
		}
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
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
}

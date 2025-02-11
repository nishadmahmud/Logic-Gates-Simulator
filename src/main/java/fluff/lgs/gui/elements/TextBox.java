package fluff.lgs.gui.elements;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import fluff.lgs.LGS;
import fluff.lgs.gui.ActionListener;
import fluff.lgs.gui.Element;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.utils.Colors;
import fluff.lgs.utils.Timer;

public class TextBox extends Element {
	
	private static final int INITIAL_KEY_REPEAT_INTERVAL = 400;
	private static final int KEY_REPEAT_INTERVAL = 50;
	
	private final ActionListener action;
	private ActionListener typeAction;
	
	private String text = "";
	private String placeHolder;
	
	private int maxCharacter = 10000;
	private int cursorPos;
	
	private int lastKey = -1;
	private char lastChar = 0;
	private Timer timer = new Timer();
	
	public TextBox(int x, int y, int width, int height, ActionListener<? extends TextBox> action) {
		super(x, y, width, height);
		this.action = action;
	}
	
	public TextBox(int x, int y, int width, int height) {
		this(x, y, width, height, null);
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		g.setColor(hasFocus() ? Colors.textbox_border_focus : Colors.textbox_border);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Colors.textbox);
		g.fillRect(2, 2, width - 4, height - 4);
		
		final int textWidth = width - 4;
		final int caretWidth = g.getFont().getWidth("_");
		
		int cpos = g.getFont().getWidth(text.substring(0, cursorPos));
		int tx = cpos > textWidth ? textWidth - cpos - caretWidth : 0;
		tx += 4;
		
		g.pushTransform();
		g.setWorldClip(2, 2, width - 4, height - 4);
		
		Fonts.draw(Align.START, Align.CENTER, text, tx, height / 2, Colors.text);
		
		if (text.isEmpty()) {
			Fonts.draw(Align.START, Align.CENTER, placeHolder, tx, height / 2, Colors.text_dark);
		}
		
		if (hasFocus()) {
			Fonts.draw(Align.START, Align.CENTER, "_", tx + cpos, height / 2, Colors.text);
		}
		
		g.clearWorldClip();
		g.popTransform();
	}
	
	@Override
	public void update(int delta) {
		if (lastKey == -1) return;
		
		if (LGS.container().getInput().isKeyDown(lastKey)) {
			if (timer.passed(KEY_REPEAT_INTERVAL, true)) {
				keyPress(lastKey, lastChar);
			}
		} else {
			lastKey = -1;
		}
	}
	
	@Override
	public boolean keyPress(int key, char c) {
		if (!hasFocus()) return false;
		Input input = LGS.container().getInput();
		
		if (key != -1) {
			if ((key == Input.KEY_V) && ((input.isKeyDown(Input.KEY_LCONTROL)) || (input.isKeyDown(Input.KEY_RCONTROL)))) {
				String text = Sys.getClipboard();
				if (text != null) {
					paste(text);
				}
				return true;
			}
			
			if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {
				return true;
			}
			if (input.isKeyDown(Input.KEY_LALT) || input.isKeyDown(Input.KEY_RALT)) {
				return true;
			}
		}
		
		if (lastKey != key) {
			lastKey = key;
			timer.reset(INITIAL_KEY_REPEAT_INTERVAL);
		} else {
			timer.reset();
		}
		lastChar = c;
		
		if (key == Input.KEY_LEFT) {
			if (cursorPos > 0) {
				cursorPos--;
			}
		} else if (key == Input.KEY_RIGHT) {
			if (cursorPos < text.length()) {
				cursorPos++;
			}
		} else if (key == Input.KEY_BACK) {
			if ((cursorPos > 0) && (text.length() > 0)) {
				if (cursorPos < text.length()) {
					type(text.substring(0, cursorPos - 1) + text.substring(cursorPos));
				} else {
					type(text.substring(0, cursorPos - 1));
				}
				cursorPos--;
			}
		} else if (key == Input.KEY_DELETE) {
			if (text.length() > cursorPos) {
				type(text.substring(0, cursorPos) + text.substring(cursorPos + 1));
			}
		} else if ((c < 127) && (c > 31) && (text.length() < maxCharacter)) {
			if (cursorPos < text.length()) {
				type(text.substring(0, cursorPos) + c + text.substring(cursorPos));
			} else {
				type(text.substring(0, cursorPos) + c);
			}
			cursorPos++;
		} else if (key == Input.KEY_RETURN) {
			if (action != null) action.onAction(this);
		}
		
		return true;
	}
	
	@Override
	public void setFocus(boolean focus) {
		lastKey = -1;
		super.setFocus(focus);
	}
	
	private void type(String text) {
		this.text = text;
		
		if (typeAction != null) typeAction.onAction(this);
	}
	
	public void paste(String text) {
		for (int i = 0; i < text.length(); i++) {
			keyPress(-1, text.charAt(i));
		}
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String value) {
		this.text = value;
		if (cursorPos > value.length()) {
			cursorPos = value.length();
		}
	}
	
	public String getPlaceHolder() {
		return placeHolder;
	}
	
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}
	
	public void setCursorPos(int pos) {
		cursorPos = pos;
		if (cursorPos > text.length()) {
			cursorPos = text.length();
		}
	}
	
	public void setCursorPosAtEnd() {
		cursorPos = text.length();
	}
	
	public void setMaxLength(int length) {
		maxCharacter = length;
		if (text.length() > maxCharacter) {
			text = text.substring(0, maxCharacter);
		}
	}
	
	public ActionListener<? extends TextBox> getTypeAction() {
		return typeAction;
	}
	
	public void setTypeAction(ActionListener<? extends TextBox> typeAction) {
		this.typeAction = typeAction;
	}
}

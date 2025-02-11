package fluff.lgs.gui.elements.gate;

import java.io.IOException;

import org.newdawn.slick.Graphics;

import fluff.lgs.World;
import fluff.lgs.gate.connection.ConnectionType;
import fluff.lgs.gui.Element;
import fluff.lgs.gui.Elements;
import fluff.lgs.gui.IParent;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.data.IDataOutput;
import fluff.lgs.storage.data.IDataParser;
import fluff.lgs.storage.parser.ObjectParsers;
import fluff.lgs.utils.RenderUtils;

public class GateElements extends Elements implements IDataParser {
	
	private final World world;
	
	private boolean render = true;
	
	public GateElements(World world) {
		super(world);
		this.world = world;
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		for (int i = 0; i < list.size(); i++) {
			Element e = list.get(i);
			if (e instanceof GateWindow gw) {
				for (ButtonConnection bc : gw.gate.inputs) {
					if (bc.render == render) continue;
					bc.render = !bc.render;
					
					if (bc.from == null) continue;
					
					ButtonConnection from = (ButtonConnection) bc.from;
					
					RenderUtils.drawConnection(g, bc, from, true, true, true);
					
					from.renderTranslated(g, from.getValue()); // to fix overlap on outputs
				}
			}
			
			g.pushTransform();
			g.translate(e.getX(), e.getY());
			e.render(g, mouseX - e.getX(), mouseY - e.getY());
			g.popTransform();
		}
		
		render = !render;
	}
	
	@Override
	public void read(IDataInput data) throws IOException {
		world.windowReg.reset(data.Long());
		
		list.clear();
		int elementsSize = data.Int();
		for (int i = 0; i < elementsSize; i++) {
			add(ObjectParsers.read(world, data));
		}
	}
	
	@Override
	public void write(IDataOutput data) throws IOException {
		data.Long(world.windowReg.winID);
		
		data.Int(list.size());
		for (Element e : list) {
			ObjectParsers.write(world, data, e);
		}
	}
}

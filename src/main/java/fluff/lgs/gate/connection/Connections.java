package fluff.lgs.gate.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fluff.lgs.World;
import fluff.lgs.gui.Element;
import fluff.lgs.gui.Elements;
import fluff.lgs.gui.elements.gate.ButtonConnection;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.data.IDataOutput;
import fluff.lgs.storage.data.IDataParser;
import fluff.lgs.storage.parser.ObjectParsers;

public class Connections implements IDataParser {
	
	private final World world;
	
	public final List<Link> list = new ArrayList<>();
	
	public Connections(World world) {
		this.world = world;
	}
	
	public Link findTo(ButtonConnection to) {
		if (to == null) return null;
		
		for (Link link : list) {
			if (link.to.equals(to)) {
				return link;
			}
		}
		return null;
	}
	
	public List<Link> findFrom(ButtonConnection from) {
		List<Link> found = new ArrayList<>();
		if (from == null) return found;
		
		for (Link link : list) {
			if (link.from.equals(from)) {
				found.add(link);
			}
		}
		return found;
	}
	
	public void remove(Link link) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(link)) {
				link.to.from = null;
				list.remove(i);
				break;
			}
		}
	}
	
	public int findIndex(ButtonConnection from, ButtonConnection to) {
		if (from.getType() == to.getType()) return -1;
		if (Objects.equals(from.parent, to.parent)) return -1;
		
		if (from.getType() == ConnectionType.INPUT) {
			return findIndex(to, from);
		}
		
		for (int i = 0; i < list.size(); i++) {
			Link link = list.get(i);
			
			if (link.from.equals(from) && link.to.equals(to)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public boolean addOrRemove(ButtonConnection from, ButtonConnection to) {
		// from = OUTPUT (connection = GateConnection)
		// to = INPUT (connection = ButtonConnection)
		
		if (from.getType() == to.getType()) return false;
		if (Objects.equals(from.parent, to.parent)) return false;
		
		if (from.getType() == ConnectionType.INPUT) {
			return addOrRemove(to, from);
		}
		
		int index = findIndex(from, to);
		if (index != -1) {
			to.from = null;
			list.remove(index);
			
			return true;
		}
		
		if (to.from != null) {
			int i = findIndex((ButtonConnection) to.from, to);
			if (i != -1) list.remove(i);
		}
		
		to.from = from;
		list.add(new Link(from, to));
		
		return true;
	}
	
	@Override
	public void read(IDataInput data) throws IOException {
		world.currentConnection = null;
		
		list.clear();
		int connectionsSize = data.Int();
		for (int i = 0; i < connectionsSize; i++) {
			GateWindow fromWindow = (GateWindow) world.windowReg.lookup(data.Long());
			int fromIndex = data.Int();
			GateWindow toWindow = (GateWindow) world.windowReg.lookup(data.Long());
			int toIndex = data.Int();
			
			ButtonConnection from = fromWindow.gate.outputs[fromIndex];
			ButtonConnection to = toWindow.gate.inputs[toIndex];
			to.from = from;
			
			list.add(new Link(from, to));
		}
	}
	
	@Override
	public void write(IDataOutput data) throws IOException {
		data.Int(list.size());
		for (Link link : list) {
			data.Long(((GateWindow) link.from.parent).winID);
			data.Int(link.from.index);
			data.Long(((GateWindow) link.to.parent).winID);
			data.Int(link.to.index);
		}
	}
}

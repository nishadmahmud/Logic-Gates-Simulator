package fluff.lgs.storage.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fluff.lgs.World;
import fluff.lgs.gui.Element;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.data.IDataOutput;
import fluff.lgs.storage.parser.impl.GateWindowParser;

public class ObjectParsers {
	
	private static final Map<String, IObjectParser> REG = new HashMap<>();
	
	static {
		add(GateWindow.class, new GateWindowParser());
	}
	
	public static <V> V read(World world, IDataInput data) throws IOException {
		String id = data.LenString();
		
		IObjectParser parser = REG.get(id);
		if (parser == null) throw new IOException("Object parser missing!");
		
		return (V) parser.read(world, data);
	}
	
	public static <V> void write(World world, IDataOutput data, V obj) throws IOException {
		String id = obj.getClass().getName();
		IObjectParser parser = REG.get(id);
		if (parser == null) throw new IOException("Object parser missing!");
		
		data.LenString(id);
		
		parser.write(world, data, obj);
	}
	
	public static <V> void add(Class<V> clazz, IObjectParser<V> parser) {
		REG.put(clazz.getName(), parser);
	}
}

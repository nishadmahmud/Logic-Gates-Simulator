package fluff.lgs.storage.parser;

import java.io.IOException;

import fluff.lgs.World;
import fluff.lgs.gui.Element;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.data.IDataOutput;

public interface IObjectParser<V> {
	
	V read(World world, IDataInput data) throws IOException;
	
	void write(World world, IDataOutput data, V o) throws IOException;
}

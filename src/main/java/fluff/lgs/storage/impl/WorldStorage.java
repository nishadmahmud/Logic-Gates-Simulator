package fluff.lgs.storage.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fluff.lgs.World;
import fluff.lgs.gui.Element;
import fluff.lgs.storage.IStorage;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.data.IDataOutput;

public class WorldStorage implements IStorage {
	
	private final World world;
	public File file;
	
	public WorldStorage(World world) {
		this.world = world;
	}
	
	@Override
	public void read(IDataInput data) throws IOException {
		world.viewX = data.Float();
		world.viewY = data.Float();
		world.scale = data.Float();
		world.name = data.LenString();
		
		data.Data(world.gates);
		data.Data(world.connections);
	}
	
	@Override
	public void write(IDataOutput data) throws IOException {
		data.Float(world.viewX);
		data.Float(world.viewY);
		data.Float(world.scale);
		data.LenString(world.name);
		
		data.Data(world.gates);
		data.Data(world.connections);
	}
	
	public World getWorld() {
		return world;
	}
}

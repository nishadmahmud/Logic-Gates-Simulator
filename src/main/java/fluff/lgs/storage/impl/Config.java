package fluff.lgs.storage.impl;

import java.io.File;
import java.io.IOException;

import fluff.lgs.storage.IStorage;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.data.IDataOutput;
import fluff.lgs.storage.values.BooleanValue;

public class Config implements IStorage {
	
	private static final Config INSTANCE = new Config();
	
	public static BooleanValue SHOW_WORLD_INFO = BooleanValue.of(true);
	
	@Override
	public void read(IDataInput data) throws IOException {
	}
	
	@Override
	public void write(IDataOutput data) throws IOException {
	}
	
	// dont rly need it right now since we dont have many settings, but its here
	public static boolean save() {
		try {
			IStorage.save(INSTANCE, new File("config.lgscfg"));
			return true;
		} catch (Exception e) {}
		return false;
	}
	
	public static boolean load() {
		try {
			IStorage.load(INSTANCE, new File("config.lgscfg"));
			return true;
		} catch (Exception e) {}
		return false;
	}
}

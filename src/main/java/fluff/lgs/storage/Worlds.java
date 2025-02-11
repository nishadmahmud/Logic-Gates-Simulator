package fluff.lgs.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.Sys;

import fluff.lgs.World;
import fluff.lgs.filechooser.FCFilter;
import fluff.lgs.storage.impl.WorldStorage;
import fluff.lgs.utils.Utils;

public class Worlds {
	
	private static final FCFilter FILTER = new FCFilter("Logic Gates Sim World", "lgsworld");
	public static final int NAME_MAX_LENGTH = 30;
	
	public final List<WorldStorage> worlds = new ArrayList<>();
	
	public boolean save(World world, File file) {
		try {
			if (file == null) file = Utils.fileChooser(FILTER, true);
			if (file == null) return false;
			final WorldStorage storage = world(world);
			storage.file = file;
			IStorage.save(storage, file);
			return true;
		} catch (Exception e) {
			Sys.alert("Error", e.getMessage());
		}
		return false;
	}
	
	public boolean load(World world, File file) {
		try {
			if (file == null) file = Utils.fileChooser(FILTER, false);
			if (file == null) return false;
			final WorldStorage storage = world(world);
			storage.file = file;
			IStorage.load(storage, file);
			return true;
		} catch (Exception e) {
			Sys.alert("Error", e.getMessage());
		}
		return false;
	}
	
	public World newWorld() {
		World w = new World("New World");
		world(w);
		return w;
	}
	
	private WorldStorage world(World world) {
		for (WorldStorage ws : worlds) {
			if (ws.getWorld().equals(world)) {
				return ws;
			}
		}
		
		WorldStorage storage = new WorldStorage(world);
		worlds.add(storage);
		return storage;
	}
}

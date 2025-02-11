package fluff.lgs.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import fluff.lgs.World;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.data.IDataOutput;
import fluff.lgs.storage.data.IDataParser;
import fluff.lgs.storage.data.stream.StreamDataInput;
import fluff.lgs.storage.data.stream.StreamDataOutput;
import fluff.lgs.storage.impl.WorldStorage;

public interface IStorage extends IDataParser {
	
	int VERSION = 1;
	
	static void save(IStorage storage, File file) throws Exception {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			IDataOutput out = new StreamDataOutput(fos);
			
			// HEADER
			out.Int(VERSION);
			
			// BODY
			storage.write(out);
		}
	}
	
	static void load(IStorage storage, File file) throws Exception {
		try (FileInputStream fis = new FileInputStream(file)) {
			IDataInput in = new StreamDataInput(fis);
			
			// HEADER
			int version = in.Int();
			if (version != VERSION) throw new Exception("Storage version mismatch!");
			
			// BODY
			storage.read(in);
		}
	}
}

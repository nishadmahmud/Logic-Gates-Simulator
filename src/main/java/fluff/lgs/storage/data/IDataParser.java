package fluff.lgs.storage.data;

import java.io.IOException;

public interface IDataParser {
	
	void read(IDataInput data) throws IOException;
	
	void write(IDataOutput data) throws IOException;
}

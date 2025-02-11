package fluff.lgs.storage.data;

import java.io.DataInputStream;
import java.io.IOException;

public interface IDataInput {
	
	void Data(IDataParser data) throws IOException;
	
	boolean Boolean() throws IOException;
	
	byte Byte() throws IOException;
	
	char Char() throws IOException;
	
	short Short() throws IOException;
	
	int Int() throws IOException;
	
	long Long() throws IOException;
	
	float Float() throws IOException;
	
	double Double() throws IOException;
	
	String LenString() throws IOException;
	
	String String(int length) throws IOException;
	
	byte[] LenBytes() throws IOException;
	
	byte[] Bytes(int length) throws IOException;
	
	byte[] Available() throws IOException;
}

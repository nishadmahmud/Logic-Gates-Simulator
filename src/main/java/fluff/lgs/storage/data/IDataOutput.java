package fluff.lgs.storage.data;

import java.io.DataOutputStream;
import java.io.IOException;

public interface IDataOutput {
	
	void Data(IDataParser data) throws IOException;
	
	void Boolean(boolean value) throws IOException;
	
	void Byte(byte value) throws IOException;
	
	void Char(char value) throws IOException;
	
	void Short(short value) throws IOException;
	
	void Int(int value) throws IOException;
	
	void Long(long value) throws IOException;
	
	void Float(float value) throws IOException;
	
	void Double(double value) throws IOException;
	
	void LenString(String value) throws IOException;
	
	void String(String value) throws IOException;
	
	void LenBytes(byte[] value) throws IOException;
	
	void Bytes(byte[] value) throws IOException;
}

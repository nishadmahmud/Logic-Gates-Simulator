package fluff.lgs.storage.data.stream;

import java.io.IOException;
import java.io.OutputStream;

import fluff.lgs.storage.data.DataParser;
import fluff.lgs.storage.data.IDataOutput;
import fluff.lgs.storage.data.IDataParser;

public class StreamDataOutput implements IDataOutput {
	
	private OutputStream stream;
	
	public StreamDataOutput(OutputStream stream) {
		this.stream = stream;
	}
	
	@Override
	public void Data(IDataParser data) throws IOException {
		data.write(this);
	}
	
	@Override
	public void Boolean(boolean value) throws IOException {
		stream.write(DataParser.sBoolean(value));
	}
	
	@Override
	public void Byte(byte value) throws IOException {
		stream.write(DataParser.sByte(value));
	}
	
	@Override
	public void Char(char value) throws IOException {
		stream.write(DataParser.sChar(value));
	}
	
	@Override
	public void Short(short value) throws IOException {
		stream.write(DataParser.sShort(value));
	}
	
	@Override
	public void Int(int value) throws IOException {
		stream.write(DataParser.sInt(value));
	}
	
	@Override
	public void Long(long value) throws IOException {
		stream.write(DataParser.sLong(value));
	}
	
	@Override
	public void Float(float value) throws IOException {
		stream.write(DataParser.sFloat(value));
	}
	
	@Override
	public void Double(double value) throws IOException {
		stream.write(DataParser.sDouble(value));
	}
	
	@Override
	public void LenString(String value) throws IOException {
		stream.write(DataParser.sInt(value.length()));
		stream.write(DataParser.sString(value));
	}
	
	@Override
	public void String(String value) throws IOException {
		stream.write(DataParser.sString(value));
	}
	
	@Override
	public void LenBytes(byte[] value) throws IOException {
		stream.write(DataParser.sInt(value.length));
		stream.write(value);
	}
	
	@Override
	public void Bytes(byte[] value) throws IOException {
		stream.write(value);
	}
}

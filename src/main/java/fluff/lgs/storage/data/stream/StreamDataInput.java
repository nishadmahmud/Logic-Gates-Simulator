package fluff.lgs.storage.data.stream;

import java.io.IOException;
import java.io.InputStream;

import fluff.lgs.storage.data.DataParser;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.data.IDataParser;

public class StreamDataInput implements IDataInput {
	
	private InputStream stream;
	
	public StreamDataInput(InputStream stream) {
		this.stream = stream;
	}
	
	@Override
	public void Data(IDataParser data) throws IOException {
		data.read(this);
	}
	
	@Override
	public boolean Boolean() throws IOException {
		return DataParser.dBoolean(read(DataParser.SIZE_BOOLEAN));
	}
	
	@Override
	public byte Byte() throws IOException {
		return DataParser.dByte(read(DataParser.SIZE_BYTE));
	}
	
	@Override
	public char Char() throws IOException {
		return DataParser.dChar(read(DataParser.SIZE_CHAR));
	}
	
	@Override
	public short Short() throws IOException {
		return DataParser.dShort(read(DataParser.SIZE_SHORT));
	}
	
	@Override
	public int Int() throws IOException {
		return DataParser.dInt(read(DataParser.SIZE_INT));
	}
	
	@Override
	public long Long() throws IOException {
		return DataParser.dLong(read(DataParser.SIZE_LONG));
	}
	
	@Override
	public float Float() throws IOException {
		return DataParser.dFloat(read(DataParser.SIZE_FLOAT));
	}
	
	@Override
	public double Double() throws IOException {
		return DataParser.dDouble(read(DataParser.SIZE_DOUBLE));
	}
	
	@Override
	public String LenString() throws IOException {
		return DataParser.dString(read(Int()));
	}
	
	@Override
	public String String(int length) throws IOException {
		return DataParser.dString(read(length));
	}
	
	@Override
	public byte[] LenBytes() throws IOException {
		return read(Int());
	}
	
	@Override
	public byte[] Bytes(int length) throws IOException {
		return read(length);
	}
	
	@Override
	public byte[] Available() throws IOException {
		return read();
	}
	
	private byte[] read() throws IOException {
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		return bytes;
	}
	
	private byte[] read(int length) throws IOException {
		byte[] bytes = new byte[length];
		stream.read(bytes);
		return bytes;
	}
}

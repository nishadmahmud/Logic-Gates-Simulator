package fluff.lgs.storage.data;

import java.io.EOFException;

public class DataParser {
	
	public static final int SIZE_BOOLEAN = 1;
	public static final int SIZE_BYTE = 1;
	public static final int SIZE_CHAR = 2;
	public static final int SIZE_SHORT = 2;
	public static final int SIZE_INT = 4;
	public static final int SIZE_LONG = 8;
	public static final int SIZE_FLOAT = 4;
	public static final int SIZE_DOUBLE = 8;
	
	// serializers
	
	public static byte[] sBoolean(boolean v) {
		return new byte[] { (byte) (v ? 1 : 0) };
	}
	
	public static byte[] sByte(byte v) {
		return new byte[] { v };
	}
	
	public static byte[] sChar(char v) {
		return new byte[] {
		        (byte) ((v >>  8) & 0xFF),   
		        (byte) ((v      ) & 0xFF)
		};
	}
	
	public static byte[] sShort(short v) {
		return new byte[] {
		        (byte) ((v >>  8) & 0xFF),   
		        (byte) ((v      ) & 0xFF)
		};
	}
	
	public static byte[] sInt(int v) {
		return new byte[] {
				(byte) ((v >> 24) & 0xFF),
		        (byte) ((v >> 16) & 0xFF),   
		        (byte) ((v >>  8) & 0xFF),   
		        (byte) ((v      ) & 0xFF)
		};
	}
	
	public static byte[] sLong(long v) {
		return new byte[] {
				(byte) ((v >> 56) & 0xFF),
		        (byte) ((v >> 48) & 0xFF),   
		        (byte) ((v >> 40) & 0xFF),   
		        (byte) ((v >> 32) & 0xFF),
				(byte) ((v >> 24) & 0xFF),
		        (byte) ((v >> 16) & 0xFF),   
		        (byte) ((v >>  8) & 0xFF),   
		        (byte) ((v      ) & 0xFF)
		};
	}
	
	public static byte[] sFloat(float v) {
		return sInt(Float.floatToIntBits(v));
	}
	
	public static byte[] sDouble(double v) {
		return sLong(Double.doubleToLongBits(v));
	}
	
	public static byte[] sString(String string) {
		return string.getBytes();
	}
	
	// deserializers
	
	public static boolean dBoolean(byte[] bytes) {
		return bytes[0] != 0;
	}
	
	public static byte dByte(byte[] bytes) {
		return bytes[0];
	}
	
	public static char dChar(byte[] bytes) {
		return (char) (
        		(bytes[1] & 0xFF)       |
                (bytes[0] & 0xFF) <<  8
                );
	}
	
	public static short dShort(byte[] bytes) {
        return (short) (
        		(bytes[1] & 0xFF)       |
                (bytes[0] & 0xFF) <<  8
                );
	}
	
	public static int dInt(byte[] bytes) {
		return (int) (
        		(bytes[3] & 0xFF)       |
                (bytes[2] & 0xFF) <<  8 |
                (bytes[1] & 0xFF) << 16 |
                (bytes[0] & 0xFF) << 24
                );
	}
	
	public static long dLong(byte[] bytes) {
		return (long) (
				(bytes[7] & 0xFF)       |
                (bytes[6] & 0xFF) <<  8 |
                (bytes[5] & 0xFF) << 16 |
                (bytes[4] & 0xFF) << 24 |
        		(bytes[3] & 0xFF) << 32 |
                (bytes[2] & 0xFF) << 40 |
                (bytes[1] & 0xFF) << 48 |
                (bytes[0] & 0xFF) << 56
                );
	}
	
	public static float dFloat(byte[] bytes) {
		return Float.intBitsToFloat(dInt(bytes));
	}
	
	public static double dDouble(byte[] bytes) {
		return Double.longBitsToDouble(dLong(bytes));
	}
	
	public static String dString(byte[] bytes) {
		return new String(bytes);
	}
}

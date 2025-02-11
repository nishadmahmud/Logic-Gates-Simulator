package fluff.lgs.gate;

public enum LogicalValue {
	TRUE(1),
	FALSE(0),
	UNDEFINED(2),
	;
	
	private final byte byteValue;
	
	private LogicalValue(int byteValue) {
		this.byteValue = (byte) byteValue;
	}
	
	public LogicalValue buffer() {
		if (isUndefined(this)) return UNDEFINED;
		
		return this;
	}
	
	public LogicalValue not() {
		if (isUndefined(this)) return UNDEFINED;
		
		if (this == TRUE) {
			return FALSE;
		}
		return TRUE;
	}
	
	public LogicalValue and(LogicalValue v) {
		if (isUndefined(this, v)) return UNDEFINED;
		
		if (this == TRUE && v == TRUE) {
			return TRUE;
		}
		return FALSE;
	}
	
	public LogicalValue nand(LogicalValue v) {
		if (isUndefined(this, v)) return UNDEFINED;
		
		if (this == TRUE && v == TRUE) {
			return FALSE;
		}
		return TRUE;
	}
	
	public LogicalValue or(LogicalValue v) {
		if (isUndefined(this, v)) return UNDEFINED;
		
		if (this == TRUE || v == TRUE) {
			return TRUE;
		}
		return FALSE;
	}
	
	public LogicalValue nor(LogicalValue v) {
		if (isUndefined(this, v)) return UNDEFINED;
		
		if (this == TRUE || v == TRUE) {
			return FALSE;
		}
		return TRUE;
	}
	
	public LogicalValue xor(LogicalValue v) {
		if (isUndefined(this, v)) return UNDEFINED;
		
		if (this != v) {
			return TRUE;
		}
		return FALSE;
	}
	
	public LogicalValue xnor(LogicalValue v) {
		if (isUndefined(this, v)) return UNDEFINED;
		
		if (this != v) {
			return FALSE;
		}
		return TRUE;
	}
	
	public byte asByteValue() {
		return byteValue;
	}
	
	public static boolean isUndefined(LogicalValue... values) {
		for (LogicalValue v : values) {
			if (v == UNDEFINED) return true;
		}
		return false;
	}
	
	public static LogicalValue fromByteValue(byte byteValue) {
		for (LogicalValue v : values()) {
			if (v.byteValue == byteValue) {
				return v;
			}
		}
		return null;
	}
}

package fluff.lgs.storage.values;

public interface BooleanValue {
	
	boolean get();
	
	void set(boolean value);
	
	static BooleanValue of(boolean value) {
		return new BooleanValue() {
			private boolean bval = value;
			
			@Override
			public void set(boolean value) {
				this.bval = value;
			}
			
			@Override
			public boolean get() {
				return bval;
			}
		};
	}
}

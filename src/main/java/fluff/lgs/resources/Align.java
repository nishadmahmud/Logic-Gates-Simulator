package fluff.lgs.resources;


public enum Align {
	START {
		@Override
		public int getX(String text, int x) {
			return x;
		}
		
		@Override
		public int getY(String text, int y) {
			return y;
		}
	},
	CENTER {
		@Override
		public int getX(String text, int x) {
			return x - Fonts.getWidth(text) / 2;
		}
		
		@Override
		public int getY(String text, int y) {
			return y - Fonts.getHeight() / 2;
		}
	},
	END {
		@Override
		public int getX(String text, int x) {
			return x - Fonts.getWidth(text);
		}
		
		@Override
		public int getY(String text, int y) {
			return y - Fonts.getHeight();
		}
	},
	;
	
	public abstract int getX(String text, int x);
	
	public abstract int getY(String text, int y);
}

package fluff.lgs.filechooser;

public class FCFilter {
	
	private final String name;
	private final String extension;
	
	public FCFilter(String name, String extension) {
		this.name = name;
		this.extension = extension;
	}
	
	public String getName() {
		return name;
	}
	
	public String getExtension() {
		return extension;
	}
}

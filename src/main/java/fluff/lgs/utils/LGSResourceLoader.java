package fluff.lgs.utils;

import java.io.InputStream;
import java.net.URL;

import org.newdawn.slick.util.ResourceLocation;

import fluff.lgs.LGS;

public class LGSResourceLoader implements ResourceLocation {
	
	@Override
	public InputStream getResourceAsStream(final String ref) {
		final String cpRef = ref.replace('\\', '/');
		return LGS.class.getResourceAsStream(cpRef);
	}
	
	@Override
	public URL getResource(final String ref) {
		final String cpRef = ref.replace('\\', '/');
		return LGS.class.getResource(cpRef);
	}
}

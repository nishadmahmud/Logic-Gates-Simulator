package fluff.lgs.natives;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.Sys;

public class LWJGLNatives {
	
	static {
        final List<NativeLoader.LibraryLoadInfo> loadInfos = new ArrayList<NativeLoader.LibraryLoadInfo>();
        
        // Windows
        
        // 386
        loadInfos.add(new NativeLoader.LibraryLoadInfo("OpenAL32.dll", NativeLoader.System.Windows, NativeLoader.Arch.i386));
        loadInfos.add(new NativeLoader.LibraryLoadInfo("lwjgl.dll", NativeLoader.System.Windows, NativeLoader.Arch.i386));
        
        // x64
        loadInfos.add(new NativeLoader.LibraryLoadInfo("OpenAL64.dll", NativeLoader.System.Windows, NativeLoader.Arch.x64));
        loadInfos.add(new NativeLoader.LibraryLoadInfo("lwjgl64.dll", NativeLoader.System.Windows, NativeLoader.Arch.x64));
        
        // Linux
        
        // 386
        loadInfos.add(new NativeLoader.LibraryLoadInfo("liblwjgl.so", NativeLoader.System.Linux, NativeLoader.Arch.i386));
        loadInfos.add(new NativeLoader.LibraryLoadInfo("libopenal.so", NativeLoader.System.Linux, NativeLoader.Arch.i386));
        
        // x64
        loadInfos.add(new NativeLoader.LibraryLoadInfo("liblwjgl64.so", NativeLoader.System.Linux, NativeLoader.Arch.x64));
        loadInfos.add(new NativeLoader.LibraryLoadInfo("libopenal64.so", NativeLoader.System.Linux, NativeLoader.Arch.x64));
        
        // Mac OS X
        
        // 386
        loadInfos.add(new NativeLoader.LibraryLoadInfo("liblwjgl.dylib", NativeLoader.System.MacOSX, NativeLoader.Arch.i386));
        loadInfos.add(new NativeLoader.LibraryLoadInfo("openal.dylib", NativeLoader.System.MacOSX, NativeLoader.Arch.i386));
        
        // x64
        loadInfos.add(new NativeLoader.LibraryLoadInfo("liblwjgl.dylib", NativeLoader.System.MacOSX, NativeLoader.Arch.x64));
        loadInfos.add(new NativeLoader.LibraryLoadInfo("openal.dylib", NativeLoader.System.MacOSX, NativeLoader.Arch.x64));
        
        final String tmpLibPath = NativeLoader.extractLibraries(loadInfos);
        System.setProperty("org.lwjgl.librarypath", tmpLibPath);
        
        // Touch the LWJGL Sys class to make it load the libraries
        Sys.initialize();
    }
	
    public static void load() {}
}

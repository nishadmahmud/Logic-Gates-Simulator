package fluff.lgs.gate;

import java.io.IOException;

import org.newdawn.slick.Image;

import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.storage.data.IDataInput;

public interface IGateType {
	
	String getID();
	
	int getMinInputs();
	
	int getMaxInputs();
	
	int getDefaultInputs();
	
	int getOutputs();
	
	Image getIcon();
	
	String getLine1();
	
	String getLine2();
	
	LogicGate create(GateWindow gw, IDataInput data) throws IOException;
}

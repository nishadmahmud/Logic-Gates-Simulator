package fluff.lgs.storage.parser.impl;

import java.io.IOException;

import fluff.lgs.World;
import fluff.lgs.gate.NativeGateType;
import fluff.lgs.gate.impl.InputGate;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.storage.data.IDataInput;
import fluff.lgs.storage.data.IDataOutput;
import fluff.lgs.storage.parser.IObjectParser;

public class GateWindowParser implements IObjectParser<GateWindow> {
	
	@Override
	public GateWindow read(World world, IDataInput data) throws IOException {
		GateWindow gw = new GateWindow(null);
		gw.reg = world.windowReg;
		
		gw.winID = data.Long();
		gw.x = data.Int();
		gw.y = data.Int();
		gw.title = data.LenString();
		
		NativeGateType type = NativeGateType.fromID(data.LenString());
		gw.init(type, data);
		
		world.windowReg.put(gw.winID, gw);
		
		return gw;
	}
	
	@Override
	public void write(World world, IDataOutput data, GateWindow gw) throws IOException {
		data.Long(gw.winID);
		data.Int(gw.x);
		data.Int(gw.y);
		data.LenString(gw.title);
		
		data.LenString(gw.gate.type.getID());
		
		if (gw.gate instanceof InputGate in) {
			data.Byte(in.value.getLogicalValue().asByteValue());
		}
	}
}

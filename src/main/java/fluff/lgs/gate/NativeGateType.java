package fluff.lgs.gate;

import java.io.IOException;

import org.newdawn.slick.Image;

import fluff.lgs.gate.impl.natives.AndGate;
//import fluff.lgs.gate.impl.natives.BufferGate;
import fluff.lgs.gate.impl.natives.NandGate;
import fluff.lgs.gate.impl.natives.NorGate;
import fluff.lgs.gate.impl.natives.NotGate;
import fluff.lgs.gate.impl.natives.OrGate;
import fluff.lgs.gate.impl.natives.XnorGate;
import fluff.lgs.gate.impl.natives.XorGate;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.resources.Icons;
import fluff.lgs.storage.data.IDataInput;

public enum NativeGateType implements IGateType {
//	BUFFER(
//			"BUFFER",
//			1, 1,
//			Icons.BUFFER,
//			"Native BUFFER logic gate.",
//			null
//			) {
//		@Override
//		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
//			return GateWindow.nativeGate(gw, new BufferGate());
//		}
//	},
	NOT(
			"NOT",
			1, 1, 1,
			1,
			Icons.NOT,
			"NOT gate. IC No: 7404",
			null
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new NotGate());
		}
	},
	AND(
			"AND",
			2, 8, 2,
			1,
			Icons.AND,
			"AND gate. IC No: 7408",
			null
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new AndGate());
		}
	},
	NAND(
			"NAND",
			2, 8, 2,
			1,
			Icons.NAND,
			"NAND gate. IC No: 7400",
			null
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new NandGate());
		}
	},
	OR(
			"OR",
			2, 8, 2,
			1,
			Icons.OR,
			"OR gate. IC No: 7432",
			null
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new OrGate());
		}
	},
	NOR(
			"NOR",
			2, 8, 2,
			1,
			Icons.NOR,
			"NOR gate. IC No: 7402",
			null
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new NorGate());
		}
	},
	XOR(
			"XOR",
			2, 2, 2,
			1,
			Icons.XOR,
			"XOR gate. IC No: 7486",
			null
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new XorGate());
		}
	},
	XNOR(
			"XNOR",
			2, 2, 2,
			1,
			Icons.XNOR,
			"XNOR gate. IC No: 74266",
			null
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new XnorGate());
		}
	},
	INPUT(
			"INPUT",
			0, 0, 0,
			1,
			null,
			"Input True or False",
			null
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.inputGate(gw, data != null ? LogicalValue.fromByteValue(data.Byte()) : LogicalValue.FALSE);
		}
	},
	OUTPUT(
			"OUTPUT",
			1, 1, 1,
			0,
			null,
			"Output True or False",
			null
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.outputGate(gw);
		}
	},
	;
	
	private final String id;
	private final int minInputs;
	private final int maxInputs;
	private final int defaultInputs;
	private final int outputs;
	private final Image icon;
	private final String line1;
	private final String line2;
	
	private NativeGateType(String id, int minInputs, int maxInputs, int defaultInputs, int outputs, Image icon, String line1, String line2) {
		this.id = id;
		this.minInputs = minInputs;
		this.maxInputs = maxInputs;
		this.defaultInputs = defaultInputs;
		this.outputs = outputs;
		this.icon = icon;
		this.line1 = line1;
		this.line2 = line2;
	}
	
	@Override
	public String getID() {
		return id;
	}
	
	@Override
	public int getMinInputs() {
		return minInputs;
	}
	
	@Override
	public int getMaxInputs() {
		return maxInputs;
	}
	
	@Override
	public int getDefaultInputs() {
		return defaultInputs;
	}
	
	@Override
	public int getOutputs() {
		return outputs;
	}
	
	@Override
	public Image getIcon() {
		return icon;
	}
	
	@Override
	public String getLine1() {
		return line1;
	}
	
	@Override
	public String getLine2() {
		return line2;
	}
	
	@Override
	public abstract LogicGate create(GateWindow gw, IDataInput data) throws IOException;
	
	public static NativeGateType fromID(String id) {
		for (NativeGateType gt : values()) {
			if (gt.getID().equals(id)) {
				return gt;
			}
		}
		return null;
	}
}

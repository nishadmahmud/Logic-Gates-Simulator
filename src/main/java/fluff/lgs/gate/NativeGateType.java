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
import fluff.lgs.gate.impl.natives.HalfAdderGate;
import fluff.lgs.gate.impl.natives.FullAdderGate;
import fluff.lgs.gate.impl.natives.HalfSubtractorGate;
import fluff.lgs.gate.impl.natives.FullSubtractorGate;
import fluff.lgs.gate.impl.natives.Encoder4to2Gate;
import fluff.lgs.gate.impl.natives.Encoder8to3Gate;
import fluff.lgs.gate.impl.natives.Decoder2to4Gate;
import fluff.lgs.gate.impl.natives.Decoder3to8Gate;
import fluff.lgs.gate.impl.natives.Comparator1BitGate;
import fluff.lgs.gate.impl.natives.Comparator2BitGate;
import fluff.lgs.gate.impl.natives.Mux2to1Gate;
import fluff.lgs.gate.impl.natives.Mux4to1Gate;
import fluff.lgs.gate.impl.natives.Demux1to2Gate;
import fluff.lgs.gate.impl.natives.Demux1to4Gate;
import fluff.lgs.gate.impl.natives.ClockGate;
import fluff.lgs.gate.impl.natives.DFlipFlopGate;
import fluff.lgs.gate.impl.natives.JKFlipFlopGate;
import fluff.lgs.gate.impl.natives.TFlipFlopGate;
import fluff.lgs.gate.impl.natives.SRFlipFlopGate;
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
			2, 4, 2,
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
			2, 4, 2,
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
	HALF_ADDER(
			"HALF_ADDER",
			2, 2, 2,
			2,
			null,
			"Half Adder (Sum, Carry)",
			"2-bit binary addition"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new HalfAdderGate());
		}
	},
	FULL_ADDER(
			"FULL_ADDER",
			3, 3, 3,
			2,
			null,
			"Full Adder (Sum, Carry)",
			"3-bit binary addition"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new FullAdderGate());
		}
	},
	HALF_SUBTRACTOR(
			"HALF_SUBTRACTOR",
			2, 2, 2,
			2,
			null,
			"Half Subtractor (Difference, Borrow)",
			"2-bit binary subtraction"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new HalfSubtractorGate());
		}
	},
	FULL_SUBTRACTOR(
			"FULL_SUBTRACTOR",
			3, 3, 3,
			2,
			null,
			"Full Subtractor (Difference, Borrow)",
			"3-bit binary subtraction"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new FullSubtractorGate());
		}
	},
	ENCODER_4TO2(
			"ENCODER_4TO2",
			4, 4, 4,  // 4 inputs fixed
			2,        // 2 outputs
			null,     // icon
			"4-to-2 Encoder",
			"4-bit priority encoder"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new Encoder4to2Gate());
		}
	},
	ENCODER_8TO3(
			"ENCODER_8TO3",
			8, 8, 8,  // 8 inputs fixed
			3,        // 3 outputs
			null,     // icon
			"8-to-3 Encoder",
			"8-bit priority encoder"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new Encoder8to3Gate());
		}
	},
	DECODER_2TO4(
			"DECODER_2TO4",
			2, 2, 2,  // 2 inputs fixed
			4,        // 4 outputs
			null,     // icon
			"2-to-4 Decoder",
			"2-bit binary decoder"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new Decoder2to4Gate());
		}
	},
	DECODER_3TO8(
			"DECODER_3TO8",
			3, 3, 3,  // 3 inputs fixed
			8,        // 8 outputs
			null,     // icon
			"3-to-8 Decoder",
			"3-bit binary decoder"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new Decoder3to8Gate());
		}
	},
	COMPARATOR_1BIT(
			"COMPARATOR_1BIT",
			2, 2, 2,  // 2 inputs fixed (A, B)
			3,        // 3 outputs (A>B, A=B, A<B)
			null,     // icon
			"1-bit Comparator",
			"Compares two 1-bit numbers"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new Comparator1BitGate());
		}
	},
	COMPARATOR_2BIT(
			"COMPARATOR_2BIT",
			4, 4, 4,  // 4 inputs fixed (A1,A0, B1,B0)
			3,        // 3 outputs (A>B, A=B, A<B)
			null,     // icon
			"2-bit Comparator",
			"Compares two 2-bit numbers"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new Comparator2BitGate());
		}
	},
	MUX_2TO1(
			"MUX_2TO1",
			3, 3, 3,  // 3 inputs (2 data, 1 select)
			1,        // 1 output
			null,     // icon
			"2-to-1 Multiplexer",
			"Selects between 2 inputs"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new Mux2to1Gate());
		}
	},
	MUX_4TO1(
			"MUX_4TO1",
			6, 6, 6,  // 6 inputs (4 data, 2 select)
			1,        // 1 output
			null,     // icon
			"4-to-1 Multiplexer",
			"Selects between 4 inputs"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new Mux4to1Gate());
		}
	},
	DEMUX_1TO2(
			"DEMUX_1TO2",
			2, 2, 2,  // 2 inputs (1 data, 1 select)
			2,        // 2 outputs
			null,     // icon
			"1-to-2 Demultiplexer",
			"Routes input to 1 of 2 outputs"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new Demux1to2Gate());
		}
	},
	DEMUX_1TO4(
			"DEMUX_1TO4",
			3, 3, 3,  // 3 inputs (1 data, 2 select)
			4,        // 4 outputs
			null,     // icon
			"1-to-4 Demultiplexer",
			"Routes input to 1 of 4 outputs"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new Demux1to4Gate());
		}
	},
	CLOCK(
			"CLOCK",
			0, 0, 0,  // No inputs needed
			1,        // One output
			null,     // icon
			"Clock Input",
			"0.5s interval"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new ClockGate());
		}
	},
	D_FLIP_FLOP(
			"D_FF",
			2, 2, 2,  // D and Clock inputs
			2,        // Q and Q' outputs
			null,     // icon
			"D Flip-Flop",
			"Edge-triggered"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new DFlipFlopGate());
		}
	},
	JK_FLIP_FLOP(
			"JK_FF",
			3, 3, 3,  // J, K and Clock inputs
			2,        // Q and Q' outputs
			null,     // icon
			"JK Flip-Flop",
			"Edge-triggered"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new JKFlipFlopGate());
		}
	},
	T_FLIP_FLOP(
			"T_FF",
			2, 2, 2,  // T and Clock inputs
			2,        // Q and Q' outputs
			null,     // icon
			"T Flip-Flop",
			"Edge-triggered"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new TFlipFlopGate());
		}
	},
	SR_FLIP_FLOP(
			"SR_FF",
			3, 3, 3,  // S, R and Clock inputs
			2,        // Q and Q' outputs
			null,     // icon
			"SR Flip-Flop",
			"Edge-triggered"
			) {
		@Override
		public LogicGate create(GateWindow gw, IDataInput data) throws IOException {
			return GateWindow.nativeGate(gw, new SRFlipFlopGate());
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

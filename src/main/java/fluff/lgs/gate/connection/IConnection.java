package fluff.lgs.gate.connection;

import fluff.lgs.gate.LogicalValue;

public interface IConnection {
	
	LogicalValue getValue();
	
	ConnectionType getType();
}

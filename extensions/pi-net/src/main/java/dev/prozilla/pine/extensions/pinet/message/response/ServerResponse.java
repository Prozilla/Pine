package dev.prozilla.pine.extensions.pinet.message.response;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.message.ServerMessage;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.session.ClientSession;

/**
 * A response received by a client from the {@link Server}, optionally containing a {@link Packet} as a payload.
 */
public class ServerResponse extends ServerMessage {
	
	public ServerResponse(Packet payload, ClientSession source) {
		super(payload, source);
	}
	
	@Override
	public String toString() {
		return Printable.objectToString(this,
			"payload", getPayload(),
			"source", source,
			"isAcknowledged", isAcknowledged(),
			"isDiscarded", isDiscarded());
	}
	
}

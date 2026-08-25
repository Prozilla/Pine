package dev.prozilla.pine.examples.chat.request;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record SetUsernameRequest(int clientId, String username) implements Packet {
	
	public static final int ID = 4;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeVarInt(clientId);
		buffer.writeString(username);
	}
	
	public static SetUsernameRequest decode(PacketBuffer buffer) {
		return new SetUsernameRequest(buffer.readVarInt(), buffer.readString());
	}
	
}

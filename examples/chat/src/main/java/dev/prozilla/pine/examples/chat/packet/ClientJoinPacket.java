package dev.prozilla.pine.examples.chat.packet;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record ClientJoinPacket(int clientId, String username) implements Packet {
	
	public static final int ID = 1;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeVarInt(clientId);
		buffer.writeString(username);
	}
	
	public static ClientJoinPacket decode(PacketBuffer buffer) {
		return new ClientJoinPacket(buffer.readVarInt(), buffer.readString());
	}
	
}

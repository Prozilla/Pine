package dev.prozilla.pine.examples.chat.response;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record ClientLeavePacket(String username) implements Packet {
	
	public static final int ID = 2;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeString(username);
	}
	
	public static ClientLeavePacket decode(PacketBuffer buffer) {
		return new ClientLeavePacket(buffer.readString());
	}
	
}

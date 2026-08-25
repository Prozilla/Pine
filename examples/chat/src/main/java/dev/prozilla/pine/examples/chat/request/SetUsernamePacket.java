package dev.prozilla.pine.examples.chat.request;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record SetUsernamePacket(String username) implements Packet {
	
	public static final int ID = 4;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeString(username);
	}
	
	public static SetUsernamePacket decode(PacketBuffer buffer) {
		return new SetUsernamePacket(buffer.readString());
	}
	
}

package dev.prozilla.pine.examples.chat.packet;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record MessagePacket(int clientId, String username, String content) implements Packet {
	
	public static final int ID = 6;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeVarInt(clientId);
		buffer.writeString(username);
		buffer.writeString(content);
	}
	
	public static MessagePacket decode(PacketBuffer buffer) {
		return new MessagePacket(buffer.readVarInt(), buffer.readString(), buffer.readString());
	}
	
}

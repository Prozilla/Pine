package dev.prozilla.pine.examples.chat.request;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record SendMessageRequest(int clientId, String content) implements Packet {
	
	public static final int ID = 5;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeVarInt(clientId);
		buffer.writeString(content);
	}
	
	public static SendMessageRequest decode(PacketBuffer buffer) {
		return new SendMessageRequest(buffer.readVarInt(), buffer.readString());
	}
	
}

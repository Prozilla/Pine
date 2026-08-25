package dev.prozilla.pine.examples.chat.request;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record SendMessagePacket(String content) implements Packet {
	
	public static final int ID = 5;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeString(content);
	}
	
	public static SendMessagePacket decode(PacketBuffer buffer) {
		return new SendMessagePacket(buffer.readString());
	}
	
}

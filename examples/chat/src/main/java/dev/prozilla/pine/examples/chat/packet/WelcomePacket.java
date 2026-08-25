package dev.prozilla.pine.examples.chat.packet;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record WelcomePacket(int clientId) implements Packet {
	
	public static final int ID = 3;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeVarInt(clientId);
	}
	
	public static WelcomePacket decode(PacketBuffer buffer) {
		return new WelcomePacket(buffer.readVarInt());
	}
	
}

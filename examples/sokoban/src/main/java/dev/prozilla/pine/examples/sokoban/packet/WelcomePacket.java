package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record WelcomePacket(int playerId) implements Packet {
	
	public static final int ID = 10;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(playerId);
	}
	
	public static WelcomePacket decode(PacketBuffer buffer) {
		return new WelcomePacket(buffer.readVarInt());
	}
	
}

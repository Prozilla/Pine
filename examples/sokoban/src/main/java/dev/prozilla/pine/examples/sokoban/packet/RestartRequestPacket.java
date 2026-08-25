package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record RestartRequestPacket() implements Packet {
	
	public static final int ID = 4;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
	}
	
	public static RestartRequestPacket decode(PacketBuffer buffer) {
		return new RestartRequestPacket();
	}
	
}

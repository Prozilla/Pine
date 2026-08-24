package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record RejectionPacket() implements Packet {
	
	public static final int ID = 15;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void write(PacketBuffer buffer) {
	}
	
	public static RejectionPacket decode(PacketBuffer buffer) {
		return new RejectionPacket();
	}
	
}

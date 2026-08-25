package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record UndoRequestPacket() implements Packet {
	
	public static final int ID = 2;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
	}
	
	public static UndoRequestPacket decode(PacketBuffer buffer) {
		return new UndoRequestPacket();
	}
	
}

package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.examples.sokoban.net.packet.Packet;
import dev.prozilla.pine.examples.sokoban.net.packet.PacketBuffer;

public record UndoRequestPacket() implements Packet {
	
	public static final int ID = 2;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void write(PacketBuffer buffer) {
	}
	
	public static UndoRequestPacket decode(PacketBuffer buffer) {
		return new UndoRequestPacket();
	}
	
}

package dev.prozilla.pine.examples.sokoban.net.packet;

public record UndoRequestPacket() implements Packet {
	
	public static final int ID = 2;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void write(PacketBuffer buffer) {
	}
	
	public static UndoRequestPacket read(PacketBuffer buffer) {
		return new UndoRequestPacket();
	}
	
}

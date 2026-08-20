package dev.prozilla.pine.examples.sokoban.net.packet;

public record RejectionPacket() implements Packet {
	
	public static final int ID = 15;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void write(PacketBuffer buffer) {
	}
	
	public static RejectionPacket read(PacketBuffer buffer) {
		return new RejectionPacket();
	}
	
}

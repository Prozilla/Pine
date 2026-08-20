package dev.prozilla.pine.examples.sokoban.net.packet;

public record RestartRequestPacket() implements Packet {
	
	public static final int ID = 4;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void write(PacketBuffer buffer) {
	}
	
	public static RestartRequestPacket read(PacketBuffer buffer) {
		return new RestartRequestPacket();
	}
	
}

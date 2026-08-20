package dev.prozilla.pine.examples.sokoban.net.packet;

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
	
	public static WelcomePacket read(PacketBuffer buffer) {
		return new WelcomePacket(buffer.readVarInt());
	}
	
}

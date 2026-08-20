package dev.prozilla.pine.examples.sokoban.net.packet;

public record PlayerJoinPacket(int playerId, int x, int y) implements Packet {
	
	public static final int ID = 12;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(playerId);
		buffer.writeInt(x);
		buffer.writeInt(y);
	}
	
	public static PlayerJoinPacket read(PacketBuffer buffer) {
		return new PlayerJoinPacket(buffer.readVarInt(), buffer.readInt(), buffer.readInt());
	}
	
}

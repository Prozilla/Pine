package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.examples.sokoban.net.packet.Packet;
import dev.prozilla.pine.examples.sokoban.net.packet.PacketBuffer;

public record PlayerLeavePacket(int playerId) implements Packet {
	
	public static final int ID = 13;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(playerId);
	}
	
	public static PlayerLeavePacket decode(PacketBuffer buffer) {
		return new PlayerLeavePacket(buffer.readVarInt());
	}
	
}

package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

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

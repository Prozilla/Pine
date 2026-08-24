package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

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
	
	public static PlayerJoinPacket decode(PacketBuffer buffer) {
		return new PlayerJoinPacket(buffer.readVarInt(), buffer.readInt(), buffer.readInt());
	}
	
}

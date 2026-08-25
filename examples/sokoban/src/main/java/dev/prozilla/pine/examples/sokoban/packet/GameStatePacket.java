package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record GameStatePacket(int[] playerIds, int[] playerX, int[] playerY, int[] crateX, int[] crateY) implements Packet {
	
	public static final int ID = 11;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeIntArray(playerIds);
		buffer.writeIntArray(playerX);
		buffer.writeIntArray(playerY);
		buffer.writeIntArray(crateX);
		buffer.writeIntArray(crateY);
	}
	
	public static GameStatePacket decode(PacketBuffer buffer) {
		return new GameStatePacket(
			buffer.readIntArray(),
			buffer.readIntArray(),
			buffer.readIntArray(),
			buffer.readIntArray(),
			buffer.readIntArray()
		);
	}
	
}

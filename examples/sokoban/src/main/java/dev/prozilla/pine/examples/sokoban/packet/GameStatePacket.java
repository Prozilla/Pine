package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3i;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record GameStatePacket(Vector3i[] players, Vector2i[] crates) implements Packet {
	
	public static final int ID = 11;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeArray(players);
		buffer.writeArray(crates);
	}
	
	public static GameStatePacket decode(PacketBuffer buffer) {
		Vector3i[] players = buffer.readArray(Vector3i.class);
		Vector2i[] crates = buffer.readArray(Vector2i.class);
		return new GameStatePacket(players, crates);
	}
	
}

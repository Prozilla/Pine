package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record PlayerJoinPacket(int playerId, Vector2i position) implements Packet {
	
	public static final int ID = 12;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeVarInt(playerId).writeVector2i(position);
	}
	
	public static PlayerJoinPacket decode(PacketBuffer buffer) {
		return new PlayerJoinPacket(buffer.readVarInt(), buffer.readVector2i());
	}
	
}

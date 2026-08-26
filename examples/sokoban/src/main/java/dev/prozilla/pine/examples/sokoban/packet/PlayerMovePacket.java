package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.examples.sokoban.component.Move;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record PlayerMovePacket(Move move) implements Packet {
	
	public static final int ID = 14;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeVarInt(move.playerId());
		buffer.writeByte(move.direction().ordinal());
		buffer.writeVector2i(move.start()).writeVector2i(move.end());
		buffer.writeBoolean(move.pushedCrate());
		if (move.pushedCrate()) {
			buffer.writeVector2i(move.crateStart()).writeVector2i(move.crateEnd());
		}
	}
	
	public static PlayerMovePacket decode(PacketBuffer buffer) {
		int playerId = buffer.readVarInt();
		Direction direction = Direction.values()[buffer.readUnsignedByte()];
		Vector2i start = buffer.readVector2i();
		Vector2i end = buffer.readVector2i();
		boolean pushedCrate = buffer.readBoolean();
		Vector2i crateStart = null;
		Vector2i crateEnd = null;
		if (pushedCrate) {
			crateStart = buffer.readVector2i();
			crateEnd = buffer.readVector2i();
		}
		
		Move move = new Move(playerId, direction, start, end, pushedCrate, crateStart, crateEnd);
		return new PlayerMovePacket(move);
	}
	
}

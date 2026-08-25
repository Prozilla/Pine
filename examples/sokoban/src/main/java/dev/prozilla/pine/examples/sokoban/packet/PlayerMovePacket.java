package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.common.math.vector.Direction;
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
		buffer.writeInt(move.fromX()).writeInt(move.fromY());
		buffer.writeInt(move.toX()).writeInt(move.toY());
		buffer.writeBoolean(move.pushedCrate());
		if (move.pushedCrate()) {
			buffer.writeInt(move.crateFromX()).writeInt(move.crateFromY());
			buffer.writeInt(move.crateToX()).writeInt(move.crateToY());
		}
	}
	
	public static PlayerMovePacket decode(PacketBuffer buffer) {
		int playerId = buffer.readVarInt();
		Direction direction = Direction.values()[buffer.readUnsignedByte()];
		int fromX = buffer.readInt();
		int fromY = buffer.readInt();
		int toX = buffer.readInt();
		int toY = buffer.readInt();
		boolean pushedCrate = buffer.readBoolean();
		int crateFromX = 0, crateFromY = 0, crateToX = 0, crateToY = 0;
		if (pushedCrate) {
			crateFromX = buffer.readInt();
			crateFromY = buffer.readInt();
			crateToX = buffer.readInt();
			crateToY = buffer.readInt();
		}
		
		Move move = new Move(playerId, direction, fromX, fromY, toX, toY,
			pushedCrate, crateFromX, crateFromY, crateToX, crateToY);
		return new PlayerMovePacket(move);
	}
	
}

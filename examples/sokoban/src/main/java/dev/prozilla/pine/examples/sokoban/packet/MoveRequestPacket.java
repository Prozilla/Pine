package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record MoveRequestPacket(Direction direction) implements Packet {
	
	public static final int ID = 1;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeByte(direction.ordinal());
	}
	
	public static MoveRequestPacket decode(PacketBuffer buffer) {
		return new MoveRequestPacket(Direction.values()[buffer.readUnsignedByte()]);
	}
	
}

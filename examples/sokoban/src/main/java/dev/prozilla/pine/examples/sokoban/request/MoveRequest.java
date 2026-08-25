package dev.prozilla.pine.examples.sokoban.request;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record MoveRequest(Direction direction) implements Packet {
	
	public static final int ID = 1;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeByte(direction.ordinal());
	}
	
	public static MoveRequest decode(PacketBuffer buffer) {
		return new MoveRequest(Direction.values()[buffer.readUnsignedByte()]);
	}
	
}

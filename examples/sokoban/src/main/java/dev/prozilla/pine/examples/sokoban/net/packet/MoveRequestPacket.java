package dev.prozilla.pine.examples.sokoban.net.packet;

import dev.prozilla.pine.common.math.vector.Direction;

public record MoveRequestPacket(Direction direction) implements Packet {
	
	public static final int ID = 1;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeByte(direction.ordinal());
	}
	
	public static MoveRequestPacket read(PacketBuffer buffer) {
		return new MoveRequestPacket(Direction.values()[buffer.readUnsignedByte()]);
	}
	
}

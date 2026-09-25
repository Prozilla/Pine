package dev.prozilla.pine.examples.sokoban.packet;

import dev.prozilla.pine.examples.sokoban.level.Level;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record LevelPacket(Level level) implements Packet {
	
	public static final int ID = 16;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeObject(Level.class);
	}
	
	public static LevelPacket decode(PacketBuffer buffer) {
		return new LevelPacket(buffer.readObject(Level.class));
	}
	
}

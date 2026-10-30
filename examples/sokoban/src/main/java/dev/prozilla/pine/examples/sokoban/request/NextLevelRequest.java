package dev.prozilla.pine.examples.sokoban.request;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record NextLevelRequest() implements Packet {
	
	public static final int ID = 5;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {}
	
	public static NextLevelRequest decode(PacketBuffer buffer) {
		return new NextLevelRequest();
	}
	
}

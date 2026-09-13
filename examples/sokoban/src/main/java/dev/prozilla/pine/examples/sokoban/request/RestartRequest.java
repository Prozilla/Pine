package dev.prozilla.pine.examples.sokoban.request;

import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

public record RestartRequest() implements Packet {
	
	public static final int ID = 4;
	
	@Override
	public int getPacketId() {
		return ID;
	}
	
	@Override
	public void encode(PacketBuffer buffer) {}
	
	public static RestartRequest decode(PacketBuffer buffer) {
		return new RestartRequest();
	}
	
}

package dev.prozilla.pine.examples.sokoban.net.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class PacketRegistry {
	
	private static final Map<Integer, Function<PacketBuffer, Packet>> READERS = new HashMap<>();
	
	static {
		// Client to Server
		register(MoveRequestPacket.ID, MoveRequestPacket::read);
		register(UndoRequestPacket.ID, UndoRequestPacket::read);
		register(RestartRequestPacket.ID, RestartRequestPacket::read);
		
		// Server to Client
		register(WelcomePacket.ID, WelcomePacket::read);
		register(GameStatePacket.ID, GameStatePacket::read);
		register(PlayerJoinPacket.ID, PlayerJoinPacket::read);
		register(PlayerLeavePacket.ID, PlayerLeavePacket::read);
		register(PlayerMovePacket.ID, PlayerMovePacket::read);
		register(RejectionPacket.ID, RejectionPacket::read);
	}
	
	private PacketRegistry() {}
	
	private static void register(int id, Function<PacketBuffer, Packet> reader) {
		if (READERS.put(id, reader) != null) {
			throw new IllegalStateException("Duplicate packet id: " + id);
		}
	}
	
	public static Packet read(int id, PacketBuffer buffer) {
		Function<PacketBuffer, Packet> reader = READERS.get(id);
		if (reader == null) {
			throw new IllegalArgumentException("Unknown packet id: " + id);
		}
		return reader.apply(buffer);
	}
	
}

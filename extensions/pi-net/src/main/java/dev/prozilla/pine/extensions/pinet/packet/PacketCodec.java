package dev.prozilla.pine.extensions.pinet.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Encodes packets into byte buffers and decodes byte buffers into packets, using {@link PacketBuffer} as an intermediate step.
 */
public class PacketCodec extends MessageToMessageCodec<ByteBuf, Packet> {
	
	private final Map<Integer, Function<PacketBuffer, Packet>> decoders;
	
	public PacketCodec() {
		decoders = new HashMap<>();
	}
	
	@Override
	protected void encode(ChannelHandlerContext context, Packet packet, List<Object> out) {
		ByteBuf buffer = context.alloc().buffer();
		PacketBuffer packetBuffer = new PacketBuffer(buffer);
		packetBuffer.writeVarInt(packet.getPacketId());
		packet.write(packetBuffer);
		out.add(buffer);
	}
	
	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> out) {
		PacketBuffer packetBuffer = new PacketBuffer(buffer);
		int id = packetBuffer.readVarInt();
		out.add(decode(id, packetBuffer));
	}
	
	protected Packet decode(int id, PacketBuffer buffer) {
		Function<PacketBuffer, Packet> decoder = decoders.get(id);
		if (decoder == null) {
			throw new IllegalArgumentException("Unknown packet id: " + id);
		}
		return decoder.apply(buffer);
	}
	
	/**
	 * Adds a decoder for packets with the given ID.
	 * @param id The ID of the packets
	 * @param decoder The decoder
	 * @return This codec.
	 */
	public PacketCodec addDecoder(int id, Function<PacketBuffer, Packet> decoder) {
		if (decoders.put(id, decoder) != null) {
			throw new IllegalStateException("Duplicate packet id: " + id);
		}
		return this;
	}
	
}

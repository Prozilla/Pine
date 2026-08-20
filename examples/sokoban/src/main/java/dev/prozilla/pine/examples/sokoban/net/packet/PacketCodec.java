package dev.prozilla.pine.examples.sokoban.net.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

public class PacketCodec extends MessageToMessageCodec<ByteBuf, Packet> {
	
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
		out.add(PacketRegistry.read(id, packetBuffer));
	}
	
}

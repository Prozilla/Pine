package dev.prozilla.pine.extensions.pinet.packet;

import dev.prozilla.pine.common.math.vector.*;
import dev.prozilla.pine.common.system.Color;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Encodes packets into byte buffers and decodes byte buffers into packets, using {@link PacketBuffer} as an intermediate step.
 */
@ChannelHandler.Sharable
public class PacketCodec extends MessageToMessageCodec<ByteBuf, Packet> {
	
	private final Map<Integer, Function<PacketBuffer, Packet>> packetDecoders;
	private final Map<Class<?>, ObjectCodec<?, PacketBuffer>> objectCodecs;
	
	public PacketCodec() {
		packetDecoders = new HashMap<>();
		objectCodecs = new HashMap<>();
		
		addObjectCodec(Vector2i.class, PacketBuffer::writeVector2i, PacketBuffer::readVector2i);
		addObjectCodec(Vector3i.class, PacketBuffer::writeVector3i, PacketBuffer::readVector3i);
		addObjectCodec(Vector4i.class, PacketBuffer::writeVector4i, PacketBuffer::readVector4i);
		addObjectCodec(Vector2f.class, PacketBuffer::writeVector2f, PacketBuffer::readVector2f);
		addObjectCodec(Vector3f.class, PacketBuffer::writeVector3f, PacketBuffer::readVector3f);
		addObjectCodec(Vector4f.class, PacketBuffer::writeVector4f, PacketBuffer::readVector4f);
		addObjectCodec(Color.class, PacketBuffer::writeColor, PacketBuffer::readColor);
	}
	
	@Override
	protected void encode(ChannelHandlerContext context, Packet packet, List<Object> out) {
		ByteBuf buffer = context.alloc().buffer();
		PacketBuffer packetBuffer = new PacketBuffer(buffer, this);
		packetBuffer.writeVarInt(packet.getPacketId());
		packet.encode(packetBuffer);
		out.add(buffer);
	}
	
	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> out) {
		PacketBuffer packetBuffer = new PacketBuffer(buffer, this);
		int id = packetBuffer.readVarInt();
		out.add(decode(id, packetBuffer));
	}
	
	protected Packet decode(int id, PacketBuffer buffer) {
		Function<PacketBuffer, Packet> decoder = packetDecoders.get(id);
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
		if (packetDecoders.put(id, decoder) != null) {
			throw new IllegalStateException("Duplicate packet id: " + id);
		}
		return this;
	}
	
	public <T> PacketCodec addObjectCodec(Class<T> type, BiConsumer<PacketBuffer, T> encoder, Function<PacketBuffer, T> decoder) {
		return addObjectCodec(type, new ObjectCodec<>() {
			@Override
			public void encode(T object, PacketBuffer buffer) {
				encoder.accept(buffer, object);
			}
			
			@Override
			public T decode(PacketBuffer buffer) {
				return decoder.apply(buffer);
			}
		});
	}
	
	public <T> PacketCodec addObjectCodec(Class<T> type, ObjectCodec<T, PacketBuffer> codec) {
		objectCodecs.put(type, codec);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T> void writeObject(PacketBuffer buffer, T object) {
		if (object == null) {
			buffer.writeBoolean(false);
			return;
		}
		buffer.writeBoolean(true);
		
		ObjectCodec<T, PacketBuffer> codec = getObjectCodec((Class<T>)object.getClass());
		codec.encode(object, buffer);
	}
	
	public <T> T readObject(PacketBuffer buffer, Class<T> type) {
		if (!buffer.readBoolean()) {
			return null;
		}
		
		ObjectCodec<T, PacketBuffer> codec = getObjectCodec(type);
		return codec.decode(buffer);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> ObjectCodec<T, PacketBuffer> getObjectCodec(Class<T> type) {
		ObjectCodec<T, PacketBuffer> codec = (ObjectCodec<T, PacketBuffer>)objectCodecs.get(type);
		if (codec == null) {
			throw new IllegalArgumentException("Unknown object type: " + type.getSimpleName());
		}
		return codec;
	}
	
}

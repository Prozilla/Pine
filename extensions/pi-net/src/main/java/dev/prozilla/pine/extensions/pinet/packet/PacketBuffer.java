package dev.prozilla.pine.extensions.pinet.packet;

import dev.prozilla.pine.common.math.vector.*;
import dev.prozilla.pine.common.system.Color;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A buffer that contains the data of a {@link Packet} as bytes.
 */
public final class PacketBuffer {
	
	private final ByteBuf buffer;
	private final PacketCodec codec;
	
	public PacketBuffer(ByteBuf buffer, PacketCodec codec) {
		this.buffer = buffer;
		this.codec = codec;
	}
	
	public PacketBuffer writeByte(int value) {
		buffer.writeByte(value);
		return this;
	}
	
	public byte readByte() {
		return buffer.readByte();
	}
	
	public int readUnsignedByte() {
		return buffer.readUnsignedByte();
	}
	
	public PacketBuffer writeBoolean(boolean value) {
		buffer.writeBoolean(value);
		return this;
	}
	
	public boolean readBoolean() {
		return buffer.readBoolean();
	}
	
	public PacketBuffer writeColor(Color value) {
		writeFloat(value.getRed()).writeFloat(value.getGreen()).writeFloat(value.getBlue());
		boolean hasAlpha = value.getAlpha() < 1;
		writeBoolean(hasAlpha);
		if (hasAlpha) {
			writeFloat(value.getAlpha());
		}
		return this;
	}
	
	public Color readColor() {
		float red = readFloat();
		float green = readFloat();
		float blue = readFloat();
		boolean hasAlpha = readBoolean();
		return hasAlpha ? new Color(red, green, blue, readFloat()) : new Color(red, green, blue);
	}
	
	public PacketBuffer writeVector2i(Vector2i value) {
		return writeInt(value.x).writeInt(value.y);
	}
	
	public Vector2i readVector2i() {
		return new Vector2i(readInt(), readInt());
	}
	
	public PacketBuffer writeVector3i(Vector3i value) {
		return writeInt(value.x).writeInt(value.y).writeInt(value.z);
	}
	
	public Vector3i readVector3i() {
		return new Vector3i(readInt(), readInt(), readInt());
	}
	
	public PacketBuffer writeVector4i(Vector4i value) {
		return writeInt(value.x).writeInt(value.y).writeInt(value.z).writeInt(value.w);
	}
	
	public Vector4i readVector4i() {
		return new Vector4i(readInt(), readInt(), readInt(), readInt());
	}
	
	public PacketBuffer writeInt(int value) {
		buffer.writeInt(value);
		return this;
	}
	
	public int readInt() {
		return buffer.readInt();
	}
	
	public PacketBuffer writeVector2f(Vector2f value) {
		return writeFloat(value.x).writeFloat(value.y);
	}
	
	public Vector2f readVector2f() {
		return new Vector2f(readFloat(), readFloat());
	}
	
	public PacketBuffer writeVector3f(Vector3f value) {
		return writeFloat(value.x).writeFloat(value.y).writeFloat(value.z);
	}
	
	public Vector3f readVector3f() {
		return new Vector3f(readFloat(), readFloat(), readFloat());
	}
	
	public PacketBuffer writeVector4f(Vector4f value) {
		return writeFloat(value.x).writeFloat(value.y).writeFloat(value.z).writeFloat(value.w);
	}
	
	public Vector4f readVector4f() {
		return new Vector4f(readFloat(), readFloat(), readFloat(), readFloat());
	}
	
	public PacketBuffer writeFloat(float value) {
		buffer.writeFloat(value);
		return this;
	}
	
	public float readFloat() {
		return buffer.readFloat();
	}
	
	public PacketBuffer writeIntArray(int[] values) {
		writeVarInt(values.length);
		for (int value : values) {
			buffer.writeInt(value);
		}
		return this;
	}
	
	public int[] readIntArray() {
		int length = readVarInt();
		int[] values = new int[length];
		for (int i = 0; i < length; i++) {
			values[i] = buffer.readInt();
		}
		return values;
	}
	
	public <E> PacketBuffer writeArray(E[] elements) {
		writeVarInt(elements.length);
		for (E element : elements) {
			writeObject(element);
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <E> E[] readArray(Class<E> type) {
		E[] array = (E[])Array.newInstance(type, readVarInt());
		for (int i = 0; i < array.length; i++) {
			array[i] = readObject(type);
		}
		return array;
	}
	
	public PacketBuffer writeString(String string) {
		return writeString(string, StandardCharsets.UTF_8);
	}
	
	public PacketBuffer writeString(String string, Charset charset) {
		writeVarInt(string.length());
		buffer.writeCharSequence(string, charset);
		return this;
	}
	
	public String readString() {
		return readString(StandardCharsets.UTF_8);
	}
	
	public String readString(Charset charset) {
		int length = readVarInt();
		return buffer.readCharSequence(length, charset).toString();
	}
	
	public PacketBuffer writeVarInt(int value) {
		while ((value & ~0x7F) != 0) {
			buffer.writeByte((value & 0x7F) | 0x80);
			value >>>= 7;
		}
		buffer.writeByte(value);
		return this;
	}
	
	public int readVarInt() {
		int value = 0;
		int position = 0;
		byte current;
		
		while (true) {
			current = buffer.readByte();
			value |= (current & 0x7F) << position;
			if ((current & 0x80) == 0) {
				break;
			}
			position += 7;
			if (position >= 32) {
				throw new IllegalArgumentException("VarInt too big");
			}
		}
		
		return value;
	}
	
	public PacketBuffer writeObject(Object object) {
		codec.writeObject(this, object);
		return this;
	}
	
	public <T> T readObject(Class<T> type) {
		return codec.readObject(this, type);
	}
	
	public int readableBytes() {
		return buffer.readableBytes();
	}
	
}

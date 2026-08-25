package dev.prozilla.pine.extensions.pinet.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A buffer that contains the data of a {@link Packet} as bytes.
 */
public final class PacketBuffer {
	
	private final ByteBuf buffer;
	
	public PacketBuffer(ByteBuf buffer) {
		this.buffer = buffer;
	}
	
	public void writeByte(int value) {
		buffer.writeByte(value);
	}
	
	public byte readByte() {
		return buffer.readByte();
	}
	
	public int readUnsignedByte() {
		return buffer.readUnsignedByte();
	}
	
	public void writeBoolean(boolean value) {
		buffer.writeBoolean(value);
	}
	
	public boolean readBoolean() {
		return buffer.readBoolean();
	}
	
	public void writeInt(int value) {
		buffer.writeInt(value);
	}
	
	public int readInt() {
		return buffer.readInt();
	}
	
	public void writeIntArray(int[] values) {
		writeVarInt(values.length);
		for (int value : values) {
			buffer.writeInt(value);
		}
	}
	
	public int[] readIntArray() {
		int length = readVarInt();
		int[] values = new int[length];
		for (int i = 0; i < length; i++) {
			values[i] = buffer.readInt();
		}
		return values;
	}
	
	public void writeString(String string) {
		writeString(string, StandardCharsets.UTF_8);
	}
	
	public void writeString(String string, Charset charset) {
		writeVarInt(string.length());
		buffer.writeCharSequence(string, charset);
	}
	
	public String readString() {
		return readString(StandardCharsets.UTF_8);
	}
	
	public String readString(Charset charset) {
		int length = readVarInt();
		return buffer.readCharSequence(length, charset).toString();
	}
	
	public void writeVarInt(int value) {
		while ((value & ~0x7F) != 0) {
			buffer.writeByte((value & 0x7F) | 0x80);
			value >>>= 7;
		}
		buffer.writeByte(value);
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
	
	public int readableBytes() {
		return buffer.readableBytes();
	}
	
}

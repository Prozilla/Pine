package dev.prozilla.pine.extensions.pinet.packet;

public interface ObjectCodec<O, B> {
	
	void encode(O object, B buffer);
	
	O decode(B buffer);
	
}

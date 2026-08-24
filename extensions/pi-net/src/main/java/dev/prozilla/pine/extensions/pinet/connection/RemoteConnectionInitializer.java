package dev.prozilla.pine.extensions.pinet.connection;

import dev.prozilla.pine.extensions.pinet.packet.PacketCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class RemoteConnectionInitializer extends ChannelInitializer<SocketChannel> {
	
	private final RemoteConnection connection;
	private final PacketCodec codec;
	
	public RemoteConnectionInitializer(RemoteConnection connection, PacketCodec codec) {
		this.connection = connection;
		this.codec = codec;
	}
	
	@Override
	protected void initChannel(SocketChannel socketChannel) {
		socketChannel.pipeline()
			.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4))
			.addLast(new LengthFieldPrepender(4))
			.addLast(codec)
			.addLast(connection);
	}
	
}

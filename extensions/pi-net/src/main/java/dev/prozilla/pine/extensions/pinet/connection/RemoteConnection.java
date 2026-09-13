package dev.prozilla.pine.extensions.pinet.connection;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketCodec;
import dev.prozilla.pine.extensions.pinet.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.function.Supplier;

public class RemoteConnection extends SimpleChannelInboundHandler<Packet> implements Connection {
	
	private Channel channel;
	private Session session;
	
	@Override
	public void bind(Session session) {
		this.session = session;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext context) {
		channel = context.channel();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext context, Packet packet) {
		if (session != null) {
			session.receive(packet);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
		Logger logger = session != null ? session.getLogger() : Logger.system;
		logger.error("Connection aborted", cause);
		context.close();
	}
	
	@Override
	public void send(Packet packet) {
		if (channel != null && channel.isActive()) {
			channel.writeAndFlush(packet);
		}
	}
	
	@Override
	public void destroy() {
		if (channel != null) {
			channel.close();
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext context) {
		if (session != null) {
			session.disconnect();
			session = null;
		}
	}
	
	public static class Initializer extends ChannelInitializer<SocketChannel> {
		
		private final Supplier<RemoteConnection> connectionFactory;
		private final PacketCodec codec;
		
		public static final int LENGTH_FIELD_SIZE = 4;
		public static final int MAX_FRAME_LENGTH = 1 << 4 * LENGTH_FIELD_SIZE;
		
		public Initializer(Supplier<RemoteConnection> connectionFactory, PacketCodec codec) {
			this.connectionFactory = connectionFactory;
			this.codec = Checks.isNotNull(codec, "codec");
		}
		
		@Override
		protected void initChannel(SocketChannel socketChannel) {
			socketChannel.pipeline()
				.addLast(new LengthFieldBasedFrameDecoder(MAX_FRAME_LENGTH, 0, LENGTH_FIELD_SIZE, 0, LENGTH_FIELD_SIZE))
				.addLast(new LengthFieldPrepender(LENGTH_FIELD_SIZE))
				.addLast(codec)
				.addLast(connectionFactory.get());
		}
		
	}
}

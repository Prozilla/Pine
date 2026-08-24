package dev.prozilla.pine.extensions.pinet.connection;

import dev.prozilla.pine.extensions.pinet.Session;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.packet.PacketCodec;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

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
	
	public static <T extends RemoteConnection> T configure(ChannelPipeline pipeline, T connection) {
		pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4));
		pipeline.addLast(new LengthFieldPrepender(4));
		pipeline.addLast(new PacketCodec());
		pipeline.addLast(connection);
		return connection;
	}
	
}

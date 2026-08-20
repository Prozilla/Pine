package dev.prozilla.pine.examples.sokoban.net.connection;

import dev.prozilla.pine.examples.sokoban.net.packet.Packet;
import dev.prozilla.pine.examples.sokoban.net.packet.PacketCodec;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.function.Consumer;

public class RemoteConnection extends SimpleChannelInboundHandler<Packet> implements Connection {
	
	private Channel channel;
	private Consumer<Packet> receiver;
	
	public void bind(Consumer<Packet> receiver) {
		this.receiver = receiver;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext context) {
		channel = context.channel();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext context, Packet packet) {
		if (receiver != null) {
			receiver.accept(packet);
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
	
	public static <T extends RemoteConnection> T configure(ChannelPipeline pipeline, T connection) {
		pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4));
		pipeline.addLast(new LengthFieldPrepender(4));
		pipeline.addLast(new PacketCodec());
		pipeline.addLast(connection);
		return connection;
	}
	
}

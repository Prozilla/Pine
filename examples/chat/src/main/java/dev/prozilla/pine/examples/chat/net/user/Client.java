package dev.prozilla.pine.examples.chat.net.user;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.nio.charset.Charset;

public class Client extends User {

	private final Channel channel;
	private final EventLoopGroup group;
	private final String username;
	
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 1234;
	
	public Client(String host, int port, String username) throws IOException {
		this.username = username;
		
		group = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
		
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) {
					ChannelPipeline pipeline = socketChannel.pipeline();
					pipeline.addLast(new LineBasedFrameDecoder(8192));
					pipeline.addLast(new StringDecoder(Charset.defaultCharset()));
					pipeline.addLast(new StringEncoder(Charset.defaultCharset()));
					pipeline.addLast(new MessageHandler());
				}
			});
		
		try {
			channel = bootstrap.connect(host, port).sync().channel();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			group.shutdownGracefully();
			throw new IOException("Failed to connect to server", e);
		}
	}
	
	@Override
	public void sendMessage(String message) {
		channel.writeAndFlush(message + "\n");
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public void destroy() {
		try {
			channel.close();
			group.shutdownGracefully();
		} catch (SecurityException e) {
			e.printStackTrace();
		} finally {
			super.destroy();
		}
	}
	
	public static Client create(String host, int port, String username) throws IOException {
		Client client = new Client(host, port, username);
		client.sendMessage(username);
		return client;
	}
	
	private class MessageHandler extends SimpleChannelInboundHandler<String> {
		
		@Override
		protected void channelRead0(ChannelHandlerContext context, String message) {
			receiveMessage(message);
		}
		
		@Override
		public void channelInactive(ChannelHandlerContext context) {
			destroy();
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
			destroy();
		}
		
	}
	
}

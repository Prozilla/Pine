package dev.prozilla.pine.examples.chat.net.server;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.examples.chat.net.user.UserData;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> implements UserData, Destructible {
	
	private final Server server;
	private Channel channel;
	private String username;
	
	public ClientHandler(Server server) {
		this.server = server;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext context) {
		channel = context.channel();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext context, String message) {
		if (username == null) {
			username = message;
			server.connect(this);
		} else {
			server.broadcastChatMessage(this, message);
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext context) {
		disconnect();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
		disconnect();
	}
	
	public void receiveMessage(String message) {
		channel.writeAndFlush(message + "\n");
	}
	
	public void disconnect() {
		System.out.println("Disconnecting: " + username);
		server.disconnect(this);
		destroy();
	}
	
	@Override
	public void destroy() {
		try {
			if (channel != null) {
				channel.close();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
}

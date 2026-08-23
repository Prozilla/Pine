package dev.prozilla.pine.examples.chat.net.server;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.system.Ansi;
import dev.prozilla.pine.examples.chat.net.user.Host;
import dev.prozilla.pine.examples.chat.net.user.UserData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server implements Destructible {
	
	private final Channel channel;
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;
	private final List<ClientHandler> clientHandlers;
	private final Set<ClientHandler> clientHandlersToAdd;
	private final Set<ClientHandler> clientHandlersToRemove;
	private final Host host;
	
	private final AtomicBoolean usingClientHandlers;
	
	public static final int DEFAULT_PORT = 1234;
	
	public Server(int port) throws IOException {
		clientHandlers = new ArrayList<>();
		clientHandlersToAdd = new HashSet<>();
		clientHandlersToRemove = new HashSet<>();
		usingClientHandlers = new AtomicBoolean(false);
		host = new Host(this);
		
		bossGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
		workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) {
					ChannelPipeline pipeline = socketChannel.pipeline();
					pipeline.addLast(new LineBasedFrameDecoder(8192));
					pipeline.addLast(new StringDecoder(Charset.defaultCharset()));
					pipeline.addLast(new StringEncoder(Charset.defaultCharset()));
					pipeline.addLast(new ClientHandler(Server.this));
				}
			});
		
		try {
			channel = bootstrap.bind(port).sync().channel();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			throw new IOException("Failed to start server", e);
		}
	}
	
	void connect(ClientHandler clientHandler) {
		if (!clientHandlers.contains(clientHandler) && !clientHandlersToAdd.contains(clientHandler)) {
			broadcastServerMessage(clientHandler.getUsername() + " has joined");
			addClientHandler(clientHandler);
		}
	}
	
	public void disconnect(ClientHandler clientHandler) {
		if (clientHandlers.contains(clientHandler) && !clientHandlersToRemove.contains(clientHandler)) {
			removeClientHandler(clientHandler);
			broadcastServerMessage(clientHandler.getUsername() + " has left");
		}
	}
	
	private void broadcastServerMessage(String message) {
		broadcastMessage(Ansi.yellow(String.format("[SERVER] %s", message)));
	}
	
	public void broadcastHostMessage(Host host, String message) {
		broadcastMessage(String.format("[%s]: %s", host.getUsername(), Ansi.cyan(message)));
	}
	
	public void broadcastChatMessage(UserData sender, String message) {
		broadcastMessage(String.format("[%s]: %s", sender.getUsername(), Ansi.cyan(message)));
	}
	
	private void broadcastMessage(String message) {
		if (!usingClientHandlers.get()) {
			updateClientHandlers();
			usingClientHandlers.set(true);
		}
		host.receiveMessage(message);
		for (ClientHandler clientHandler : clientHandlers) {
			clientHandler.receiveMessage(message);
		}
		usingClientHandlers.set(false);
	}
	
	private void addClientHandler(ClientHandler clientHandler) {
		if (usingClientHandlers.get()) {
			clientHandlersToAdd.add(clientHandler);
		} else {
			clientHandlers.add(clientHandler);
		}
	}
	
	private void removeClientHandler(ClientHandler clientHandler) {
		if (usingClientHandlers.get()) {
			clientHandlersToRemove.add(clientHandler);
		} else {
			clientHandlers.remove(clientHandler);
		}
	}
	
	private void updateClientHandlers() {
		clientHandlers.addAll(clientHandlersToAdd);
		clientHandlers.removeAll(clientHandlersToRemove);
		clientHandlersToAdd.clear();
		clientHandlersToRemove.clear();
	}
	
	public int getPort() {
		return ((InetSocketAddress)channel.localAddress()).getPort();
	}
	
	public InetAddress getAddress() {
		return ((InetSocketAddress)channel.localAddress()).getAddress();
	}
	
	public Host getHost() {
		return host;
	}
	
	@Override
	public void destroy() {
		try {
			usingClientHandlers.set(true);
			for (ClientHandler clientHandler : clientHandlers) {
				clientHandler.destroy();
			}
			channel.close();
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static Server create(int port) throws IOException {
		return new Server(port);
	}
	
}

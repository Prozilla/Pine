package dev.prozilla.pine.examples.sokoban.net.server;

import dev.prozilla.pine.examples.sokoban.net.connection.RemoteConnection;
import io.netty.channel.ChannelHandlerContext;

/**
 * @see ServerSession
 */
public class ServerConnection extends RemoteConnection {
	
	private final Server server;
	
	public ServerConnection(Server server) {
		this.server = server;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext context) {
		super.channelActive(context);
		ServerSession session = new ServerSession(server, this, false);
		bind(session);
		server.connect(session);
	}
	
}

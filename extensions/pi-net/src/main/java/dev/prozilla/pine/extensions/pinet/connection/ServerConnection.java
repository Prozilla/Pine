package dev.prozilla.pine.extensions.pinet.connection;

import dev.prozilla.pine.extensions.pinet.Server;
import dev.prozilla.pine.extensions.pinet.session.ServerSession;
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

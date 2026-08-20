package dev.prozilla.pine.examples.sokoban.net.server;

import dev.prozilla.pine.examples.sokoban.net.connection.RemoteConnection;
import dev.prozilla.pine.examples.sokoban.system.RequestProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * @see ServerSession
 */
public class ServerConnection extends RemoteConnection {
	
	private final Server server;
	private final RequestProcessor processor;
	private ServerSession session;
	
	public ServerConnection(Server server, RequestProcessor processor) {
		this.server = server;
		this.processor = processor;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext context) {
		super.channelActive(context);
		session = new ServerSession(server, this, processor, false);
		bind(session::enqueue);
		server.connect(session);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext context) {
		if (session != null) {
			session.markDisconnected();
			session = null;
		}
	}
	
}

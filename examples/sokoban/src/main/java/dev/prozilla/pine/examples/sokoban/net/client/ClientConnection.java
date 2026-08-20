package dev.prozilla.pine.examples.sokoban.net.client;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.examples.sokoban.net.connection.RemoteConnection;
import io.netty.channel.ChannelHandlerContext;

/**
 * @see ClientSession
 */
public class ClientConnection extends RemoteConnection {
	
	private ClientSession session;
	
	void bind(ClientSession session) {
		this.session = session;
		super.bind(session::receive);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext context) {
		if (session != null) {
			session.markDisconnected();
			session = Destructible.destroy(session);
		}
	}
	
}

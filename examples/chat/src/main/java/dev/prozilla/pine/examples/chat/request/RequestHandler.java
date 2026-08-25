package dev.prozilla.pine.examples.chat.request;

import dev.prozilla.pine.examples.chat.packet.ClientJoinPacket;
import dev.prozilla.pine.examples.chat.packet.ClientLeavePacket;
import dev.prozilla.pine.examples.chat.packet.MessagePacket;
import dev.prozilla.pine.examples.chat.packet.WelcomePacket;
import dev.prozilla.pine.extensions.pinet.packet.Packet;
import dev.prozilla.pine.extensions.pinet.server.Server;
import dev.prozilla.pine.extensions.pinet.server.ServerRequestHandler;

import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements ServerRequestHandler {
	
	private final Map<Integer, String> usernames;
	
	public RequestHandler() {
		usernames = new HashMap<>();
	}
	
	@Override
	public void handleRequest(Server.Request request) {
		Packet payload = request.getPayload();
		
		if (payload instanceof SetUsernameRequest(int clientId, String username)) {
			if (clientId == request.getAuthorId() && usernames.put(clientId, username) == null) {
				request.replyToOthers(new ClientJoinPacket(clientId, username));
			}
		} else if (payload instanceof SendMessageRequest(int clientId, String content)) {
			String username = usernames.get(clientId);
			if (clientId == request.getAuthorId() && username != null) {
				request.replyToAll(new MessagePacket(clientId, username, content));
			}
		}
	}
	
	@Override
	public void handleJoin(Server.Request request) {
		request.reply(new WelcomePacket(request.getAuthorId()));
	}
	
	@Override
	public void handleLeave(Server.Request request) {
		String username = usernames.get(request.getAuthorId());
		if (username != null) {
			request.replyToOthers(new ClientLeavePacket(request.getAuthorId(), username));
		}
	}
	
}

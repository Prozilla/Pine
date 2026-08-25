package dev.prozilla.pine.examples.chat.request;

import dev.prozilla.pine.examples.chat.response.ClientJoinPacket;
import dev.prozilla.pine.examples.chat.response.ClientLeavePacket;
import dev.prozilla.pine.examples.chat.response.MessagePacket;
import dev.prozilla.pine.examples.chat.response.WelcomePacket;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequest;
import dev.prozilla.pine.extensions.pinet.message.request.ServerRequestHandler;
import dev.prozilla.pine.extensions.pinet.packet.Packet;

import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements ServerRequestHandler {
	
	private final Map<Integer, String> usernames;
	
	public RequestHandler() {
		usernames = new HashMap<>();
	}
	
	@Override
	public void handleRequest(ServerRequest request) {
		Packet payload = request.getPayload();
		
		if (payload instanceof SetUsernamePacket(String username)) {
			if (usernames.put(request.getSenderId(), username) == null) {
				request.replyToOthers(new ClientJoinPacket(username));
			}
		} else if (payload instanceof SendMessagePacket(String content)) {
			String username = usernames.get(request.getSenderId());
			if (username != null) {
				request.replyToAll(new MessagePacket(username, content));
			}
		}
	}
	
	@Override
	public void handleJoin(ServerRequest request) {
		request.reply(new WelcomePacket(request.getSenderId()));
	}
	
	@Override
	public void handleLeave(ServerRequest request) {
		String username = usernames.get(request.getSenderId());
		if (username != null) {
			request.replyToOthers(new ClientLeavePacket(username));
		}
	}
	
}

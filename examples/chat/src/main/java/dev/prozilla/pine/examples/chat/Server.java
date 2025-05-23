package dev.prozilla.pine.examples.chat;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.system.Ansi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Lifecycle {
	
	private final ServerSocket serverSocket;
	private final List<ClientHandler> clientHandlers;
	
	private static final int PORT = 1234;
	
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		clientHandlers = new ArrayList<>();
	}
	
	@Override
	public void init() {
		System.out.println("Server has started");
		try {
			while (!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				ClientHandler clientHandler = new ClientHandler(this, socket);
				connect(clientHandler);
				
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch (IOException e) {
			destroy();
		}
	}
	
	@Override
	public void destroy() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void connect(ClientHandler clientHandler) {
		if (!clientHandlers.contains(clientHandler)) {
			broadcastServerMessage(clientHandler.getUsername() + " has joined");
			clientHandlers.add(clientHandler);
		}
	}
	
	public void disconnect(ClientHandler clientHandler) {
		if (clientHandlers.remove(clientHandler)) {
			broadcastServerMessage(clientHandler.getUsername() + " has left");
		}
	}
	
	private void broadcastServerMessage(String message) {
		broadcastMessage(Ansi.yellow(String.format("[SERVER] %s", message)));
	}
	
	public void broadcastChatMessage(ClientHandler sender, String message) {
		broadcastMessage(String.format("[%s]: %s", sender.getUsername(), Ansi.cyan(message)));
	}
	
	private void broadcastMessage(String message) {
		System.out.println(message);
		for (ClientHandler clientHandler : clientHandlers) {
			clientHandler.receiveMessage(message);
		}
	}
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(PORT);
		Server server = new Server(serverSocket);
		server.init();
	}
	
}

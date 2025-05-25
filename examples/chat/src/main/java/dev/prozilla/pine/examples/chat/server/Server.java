package dev.prozilla.pine.examples.chat.server;

import dev.prozilla.pine.common.system.Ansi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
	
	private final ServerSocket serverSocket;
	private final List<ClientHandler> clientHandlers;
	
	public static final int DEFAULT_PORT = 1234;
	
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		clientHandlers = new ArrayList<>();
	}
	
	@Override
	public void run() {
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
	
	public int getPort() {
		return serverSocket.getLocalPort();
	}
	
	public InetAddress getAddress() {
		return serverSocket.getInetAddress();
	}
	
	public static Server create(int port) throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		return new Server(serverSocket);
	}
	
}

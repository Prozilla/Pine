package dev.prozilla.pine.examples.chat.server;

import dev.prozilla.pine.common.lifecycle.Destructable;
import dev.prozilla.pine.common.system.Ansi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server implements Runnable, Destructable {
	
	private final ServerSocket serverSocket;
	private final List<ClientHandler> clientHandlers;
	private final Set<ClientHandler> clientHandlersToAdd;
	private final Set<ClientHandler> clientHandlersToRemove;
	
	private final AtomicBoolean usingClientHandlers;
	
	public static final int DEFAULT_PORT = 1234;
	
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		clientHandlers = new ArrayList<>();
		clientHandlersToAdd = new HashSet<>();
		clientHandlersToRemove = new HashSet<>();
		usingClientHandlers = new AtomicBoolean(false);
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
	
	public void broadcastChatMessage(ClientHandler sender, String message) {
		broadcastMessage(String.format("[%s]: %s", sender.getUsername(), Ansi.cyan(message)));
	}
	
	private void broadcastMessage(String message) {
		if (!usingClientHandlers.get()) {
			updateClientHandlers();
			usingClientHandlers.set(true);
		}
		System.out.println(message);
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

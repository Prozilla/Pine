package dev.prozilla.pine.examples.chat;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.examples.chat.client.Client;
import dev.prozilla.pine.examples.chat.scene.ClientScene;
import dev.prozilla.pine.examples.chat.scene.CreateClientScene;
import dev.prozilla.pine.examples.chat.scene.MenuScene;
import dev.prozilla.pine.examples.chat.server.Server;
import dev.prozilla.pine.examples.chat.scene.ServerScene;
import dev.prozilla.pine.examples.chat.scene.CreateServerScene;

import java.io.IOException;

public class Chat extends Application {
	
	// Server
	private Server server;
	private Thread serverThread;
	
	// Client
	private Client client;
	private Thread clientThread;
	
	public final int connectScene;
	public final int serverStartupScene;
	
	public Chat() {
		super("Chat", 900, 600, new MenuScene());
		
		connectScene = addScene(new CreateClientScene());
		serverStartupScene = addScene(new CreateServerScene());
	}
	
	public void startClient(String host, int port, String username) {
		try {
			client = Client.create(host, port, username);
			clientThread = new Thread(client);
			clientThread.start();
			
			loadScene(addScene(new ClientScene(client)));
		} catch (IOException e) {
			logger.error("Failed to start server", e);
		}
	}
	
	public void startServer(int port) {
		try {
			server = Server.create(port);
			serverThread = new Thread(server);
			serverThread.start();
			
			loadScene(addScene(new ServerScene(server)));
		} catch (IOException e) {
			logger.error("Failed to start server", e);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (clientThread != null) {
			try {
				clientThread.interrupt();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		if (client != null) {
			client.destroy();
		}
		
		if (serverThread != null) {
			try {
				serverThread.interrupt();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		if (server != null) {
			server.destroy();
		}
	}
	
	public static void main(String[] args) {
		Chat chat = new Chat();
		chat.run();
	}
	
}

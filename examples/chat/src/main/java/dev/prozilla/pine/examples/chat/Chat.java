package dev.prozilla.pine.examples.chat;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.examples.chat.net.server.Server;
import dev.prozilla.pine.examples.chat.net.user.Client;
import dev.prozilla.pine.examples.chat.scene.MenuScene;
import dev.prozilla.pine.examples.chat.scene.client.ClientScene;
import dev.prozilla.pine.examples.chat.scene.client.CreateClientScene;
import dev.prozilla.pine.examples.chat.scene.server.CreateServerScene;
import dev.prozilla.pine.examples.chat.scene.server.ServerScene;

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
	
	public static final String FONT = "fonts/aoboshi-one/AoboshiOne-Regular.ttf";
	public static final Color BACKGROUND_COLOR_A = Color.decode("#1c232b");
	public static final Color BACKGROUND_COLOR_B = Color.decode("#0b0e11");
	public static final Color FOREGROUND_COLOR_A = Color.white();
	
	public Chat() {
		super("Chat", 480, 270, new MenuScene());
		
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
			logger.error("Failed to start client", e);
			destroy();
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
			destroy();
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

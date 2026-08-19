package dev.prozilla.pine.examples.chat;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.scene.Scene;
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
	
	// Client
	private Client client;
	
	public final Scene connectScene;
	public final Scene serverStartupScene;
	
	public static final String FONT = "fonts/aoboshi-one/AoboshiOne-Regular.ttf";
	public static final Color BACKGROUND_COLOR_A = Color.hex("#1c232b");
	public static final Color BACKGROUND_COLOR_B = Color.hex("#0b0e11");
	public static final Color FOREGROUND_COLOR_A = Color.white();
	
	public Chat() {
		super("Chat", 480, 270, new MenuScene());
		
		config.rendering.snapPixels.set(true);
		
		connectScene = new CreateClientScene();
		serverStartupScene = new CreateServerScene();
		addScene(connectScene);
		addScene(serverStartupScene);
	}
	
	public void startClient(String host, int port, String username) {
		try {
			client = Client.create(host, port, username);
			
			Scene clientScene = new ClientScene(client);
			addScene(clientScene);
			loadScene(clientScene);
		} catch (IOException e) {
			logger.error("Failed to start client", e);
			destroy();
		}
	}
	
	public void startServer(int port) {
		try {
			server = Server.create(port);
			
			Scene serverScene = new ServerScene(server);
			addScene(serverScene);
			loadScene(serverScene);
		} catch (IOException e) {
			logger.error("Failed to start server", e);
			destroy();
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (client != null) {
			client.destroy();
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

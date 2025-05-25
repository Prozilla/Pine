package dev.prozilla.pine.examples.chat.client;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.examples.chat.client.scene.ChatScene;
import dev.prozilla.pine.examples.chat.client.scene.ConnectScene;

import java.io.IOException;

public class ClientApp extends Application {
	
	private Client client;
	
	public ClientApp() {
		super("Chat", 400, 200, new ConnectScene());
	}
	
	public void startClient(String host, int port, String username) {
		try {
			client = Client.create(host, port, username);
			
			loadScene(addScene(new ChatScene(client)));
		} catch (IOException e) {
			logger.error("Failed to start server", e);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if (client != null) {
			client.destroy();
		}
	}
	
	public static void main(String[] args) {
		ClientApp clientApp = new ClientApp();
		clientApp.run();
	}
	
}

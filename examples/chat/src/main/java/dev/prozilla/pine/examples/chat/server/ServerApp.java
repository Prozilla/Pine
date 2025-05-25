package dev.prozilla.pine.examples.chat.server;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.examples.chat.server.scene.ServerScene;
import dev.prozilla.pine.examples.chat.server.scene.StartupScene;

import java.io.IOException;

public class ServerApp extends Application {
	
	private Server server;
	
	public ServerApp() {
		super("Chat Server", 300, 150, new StartupScene());
	}
	
	public void startServer(int port) {
		try {
			server = Server.create(port);
			new Thread(server).start();
			
			loadScene(addScene(new ServerScene(server)));
		} catch (IOException e) {
			logger.error("Failed to start server", e);
		}
	}
	
	public Server getServer() {
		return server;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if (server != null) {
			server.destroy();
		}
	}
	
	public static void main(String[] args) {
		ServerApp serverApp = new ServerApp();
		serverApp.run();
	}
	
}

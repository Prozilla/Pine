package dev.prozilla.pine.examples.chat.server;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.examples.chat.server.scene.ServerScene;
import dev.prozilla.pine.examples.chat.server.scene.StartupScene;

import java.io.IOException;

public class ServerApp extends Application {
	
	private Server server;
	private Thread serverThread;
	
	public ServerApp() {
		super("Chat Server", 250, 125, new StartupScene());
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
		ServerApp serverApp = new ServerApp();
		serverApp.run();
	}
	
}

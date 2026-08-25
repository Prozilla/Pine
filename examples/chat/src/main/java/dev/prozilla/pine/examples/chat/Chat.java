package dev.prozilla.pine.examples.chat;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.chat.scene.ChatScene;
import dev.prozilla.pine.examples.chat.scene.HostScene;
import dev.prozilla.pine.examples.chat.scene.JoinScene;
import dev.prozilla.pine.examples.chat.scene.MenuScene;

public class Chat extends Application {
	
	public final Scene joinScene;
	public final Scene hostScene;
	
	public static final String FONT = "fonts/aoboshi-one/AoboshiOne-Regular.ttf";
	public static final Color BACKGROUND_COLOR_A = Color.hex("#1c232b");
	public static final Color BACKGROUND_COLOR_B = Color.hex("#0b0e11");
	public static final Color FOREGROUND_COLOR_A = Color.white();
	
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 1234;
	
	public Chat() {
		super("Chat", 480, 270, new MenuScene());
		
		config.rendering.snapPixels.set(true);
		
		joinScene = new JoinScene();
		hostScene = new HostScene();
		addScene(joinScene);
		addScene(hostScene);
	}
	
	public ChatScene loadChatScene(String username) {
		ChatScene chatScene = new ChatScene(username);
		addScene(chatScene);
		loadScene(chatScene);
		return chatScene;
	}
	
	public static void main(String[] args) {
		Chat chat = new Chat();
		chat.run();
	}
	
}

package dev.prozilla.pine.examples.chat.client;

import dev.prozilla.pine.common.event.EventDispatcher;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.lifecycle.Destructable;

import java.io.*;
import java.net.Socket;

public class Client implements Destructable, Runnable {

	private final Socket socket;
	private volatile BufferedWriter bufferedWriter;
	private volatile BufferedReader bufferedReader;
	private final String username;
	
	private final EventDispatcher<ClientEvent, String> eventDispatcher;
	
	public enum ClientEvent {
		MESSAGE_RECEIVE
	}
	
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 1234;
	
	public Client(Socket socket, String username) {
		this.socket = socket;
		this.username = username;
		eventDispatcher = new EventDispatcher<>();
		
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			destroy();
		}
	}
	
	@Override
	public void run() {
		sendMessage(username);
		
		while (socket.isConnected()) {
			try {
				receiveMessage(bufferedReader.readLine());
			} catch (IOException e) {
				destroy();
				break;
			}
		}
	}
	
	public void sendMessage(String message) {
		try {
			bufferedWriter.write(message);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			destroy();
		}
	}
	
	public void receiveMessage(String message) {
		eventDispatcher.invoke(ClientEvent.MESSAGE_RECEIVE, message);
	}
	
	public void addMessageListener(EventListener<String> listener) {
		eventDispatcher.addListener(ClientEvent.MESSAGE_RECEIVE, listener);
	}
	
	@Override
	public void destroy() {
		try {
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static Client create(String host, int port, String username) throws IOException {
		Socket socket = new Socket(host, port);
		return new Client(socket, username);
	}
	
}

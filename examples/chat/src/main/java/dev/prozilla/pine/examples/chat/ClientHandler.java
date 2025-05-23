package dev.prozilla.pine.examples.chat;

import dev.prozilla.pine.common.Lifecycle;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable, Lifecycle {
	
	private Server server;
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	
	public ClientHandler(Server server, Socket socket) {
		try {
			this.server = server;
			this.socket = socket;
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			username = bufferedReader.readLine();
		} catch (Exception e) {
			destroy();
		}
	}
	
	@Override
	public void run() {
		while (socket.isConnected()) {
			try {
				String message = bufferedReader.readLine();
				server.broadcastChatMessage(this, message);
			} catch (IOException e) {
				destroy();
				break;
			}
		}
	}
	
	public void receiveMessage(String message) {
		try {
			bufferedWriter.write(message);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			destroy();
		}
	}
	
	@Override
	public void destroy() {
		server.disconnect(this);
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}
	
}

package dev.prozilla.pine.examples.chat.net.server;

import dev.prozilla.pine.common.lifecycle.Destructable;
import dev.prozilla.pine.examples.chat.net.user.UserData;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable, UserData, Destructable {
	
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
			disconnect();
		}
	}
	
	@Override
	public void run() {
		while (socket.isConnected()) {
			try {
				String message = bufferedReader.readLine();
				if (message == null) {
					disconnect();
					break;
				}
				server.broadcastChatMessage(this, message);
			} catch (IOException e) {
				disconnect();
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
			disconnect();
		}
	}
	
	public void disconnect() {
		System.out.println("Disconnecting: " + username);
		server.disconnect(this);
		destroy();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
}

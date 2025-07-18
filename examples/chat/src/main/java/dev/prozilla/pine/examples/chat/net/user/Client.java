package dev.prozilla.pine.examples.chat.net.user;

import java.io.*;
import java.net.Socket;

public class Client extends User implements Runnable {

	private final Socket socket;
	private volatile BufferedWriter bufferedWriter;
	private volatile BufferedReader bufferedReader;
	private final String username;
	
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 1234;
	
	public Client(Socket socket, String username) {
		this.socket = socket;
		this.username = username;
		
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
				String receivedMessage = bufferedReader.readLine();
				if (receivedMessage == null) {
					destroy();
					break;
				}
				receiveMessage(receivedMessage);
			} catch (IOException e) {
				destroy();
				break;
			}
		}
	}
	
	@Override
	public void sendMessage(String message) {
		try {
			bufferedWriter.write(message);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			destroy();
		}
	}
	
	@Override
	public String getUsername() {
		return username;
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
		} finally {
			super.destroy();
		}
	}
	
	public static Client create(String host, int port, String username) throws IOException {
		Socket socket = new Socket(host, port);
		return new Client(socket, username);
	}
	
}

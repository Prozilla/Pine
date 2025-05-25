package dev.prozilla.pine.examples.chat.client;

import dev.prozilla.pine.common.lifecycle.Destructable;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Destructable {

	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	
	private Thread inputThread;
	private Thread outputThread;
	
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 1234;
	
	public Client(Socket socket, String username) {
		try {
			this.socket = socket;
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.username = username;
		} catch (IOException e) {
			destroy();
		}
	}
	
	public void sendInput() {
		inputThread = new Thread(() -> {
			try {
				bufferedWriter.write(username);
				bufferedWriter.newLine();
				bufferedWriter.flush();
				
				Scanner scanner = new Scanner(System.in);
				
				while (socket.isConnected()) {
					String messageToSend = scanner.nextLine();
					bufferedWriter.write(messageToSend);
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}
			} catch (IOException e) {
				destroy();
			}
		});
		inputThread.start();
	}
	
	public void readOutput() {
		outputThread = new Thread(() -> {
			while (socket.isConnected()) {
				try {
					String receivedMessage = bufferedReader.readLine();
					System.out.println(receivedMessage);
				} catch (IOException e) {
					destroy();
				}
			}
		});
		outputThread.start();
	}
	
	@Override
	public void destroy() {
		try {
			if (inputThread != null) {
				inputThread.interrupt();
			}
			if (outputThread != null) {
				outputThread.interrupt();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
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
		Client client = new Client(socket, username);
		client.readOutput();
		client.sendInput();
		return client;
	}
	
}

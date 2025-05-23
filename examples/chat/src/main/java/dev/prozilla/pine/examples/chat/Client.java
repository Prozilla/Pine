package dev.prozilla.pine.examples.chat;

import dev.prozilla.pine.common.Lifecycle;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Lifecycle {

	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	
	private static final String HOST = "localhost";
	private static final int PORT = 1234;
	
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
	
	public void sendInput(Scanner scanner) {
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			while (socket.isConnected()) {
				String messageToSend = scanner.nextLine();
				bufferedWriter.write(messageToSend);
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
		} catch (IOException e) {
			destroy();
		}
	}
	
	public void readOutput() {
		new Thread(() -> {
			while (socket.isConnected()) {
				try {
					String receivedMessage = bufferedReader.readLine();
					System.out.println(receivedMessage);
				} catch (IOException e) {
					destroy();
				}
			}
		}).start();
	}
	
	@Override
	public void destroy() {
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
	
	public static void main(String[] args) throws IOException {
		// Read username
		Scanner scanner = new Scanner(System.in);
		System.out.print("Your username: ");
		String username = scanner.nextLine();
		
		// Connect client
		Socket socket = new Socket(HOST, PORT);
		Client client = new Client(socket, username);
		client.readOutput();
		client.sendInput(scanner);
	}
	
}

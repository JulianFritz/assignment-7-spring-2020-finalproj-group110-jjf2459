package practice;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientMain {

	public static void main(String[] args) {
		Client client = new Client();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				client.runme();
			}
		}).start();
	}

}

class Client {
	int port = 8000;
	String host = "localhost";
	DataInputStream in;
	DataOutputStream out;
	Socket socket;
	double aNumber = 5;
	
	void runme() {
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("requesting conn");
			socket = new Socket(host, port);
			System.out.println("got conn");
			
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(in);
			
			Double msg = 1.0;
			while(true) {

				System.out.print("Enter a command: ");
				String line = sc.nextLine();
				String[] command = line.split("\\s+");
			//System.out.println(line);
			//System.out.println(command[0]);
				switch (command[0]) {
				case "quit": {
					System.exit(0);
					break;
				}
				case "show": {
					out.writeInt(1);
					int amount = in.readInt();
					System.out.println("Here are the items up for auction :");
					for (int i = 0; i < amount; i++) {
						try {
							System.out.println(i + ") " + ois.readObject());
						} catch (ClassNotFoundException e) {
							System.out.println("ClassNotFound");
						}
					}
					break;
				}
				case "bid": {
					out.writeInt(2);
					int index = Integer.parseInt(command[1]);
					out.writeInt(index);
					double price = Double.parseDouble(command[2]);
					out.writeDouble(price);
					break;
				}
				default: {
					System.out.println("Invalid command");
					break;
				}
				}
				
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

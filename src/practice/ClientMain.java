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
				/*try {
					System.out.print("Enter a number to send to server: ");
					msg = sc.nextDouble();
				} catch (Exception e) {
					sc.next();
					System.out.println("Try again");
					continue;
				}
				
				out.writeDouble(msg);
				out.flush();
				*/
				try {
					System.out.println("Would you like to see the items up for bid? (y/n)");
					String c = sc.next();
					if (c.equals("y"))
						out.writeChars(c);
					else
						continue;
				} catch (Exception e) {
					System.out.println("Try again");
					continue;
				}
				
				int amount = in.readInt();
				//System.out.println("Client: The server says the square is: " + in.readDouble());
				System.out.println("Here are the items up for auction :");
				for (int i = 0; i < amount; i++) {
					try {
						System.out.println(ois.readObject());
					} catch (ClassNotFoundException e) {
						System.out.println("Exception!!!");
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

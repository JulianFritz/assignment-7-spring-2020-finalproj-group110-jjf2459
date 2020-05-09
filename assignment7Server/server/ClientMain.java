package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientMain {

	public static void main(String[] args) {
		//Client client = new Client();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Application.launch(Client.class);
			}
		}).start();
	}

}
/*
class Client extends Application {
	int port = 8000;
	String host = "localhost";
	DataInputStream in;
	DataOutputStream out;
	Socket socket;
	
	//public static void main(String[] args) {
	//	Application.launch(args);
	//}

	@Override
	public void start(Stage primaryStage) {				
		try {
			System.out.println("requesting conn");
			socket = new Socket(host, port);
			System.out.println("got conn");
			
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(in);
			
			//Grid for items
			GridPane itemGrid = new GridPane();
			itemGrid.setHgap(10);
			itemGrid.setVgap(10);
			
			out.writeInt(1);
			int noOfItems = in.readInt();
			for (int i = 0; i < noOfItems; i++) {
				try {
					Label itemNameLbl = new Label((String) ois.readObject());
					itemGrid.add(itemNameLbl, 0, i);
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			Scene scene = new Scene(itemGrid, 500, 500);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Auction Client");
			primaryStage.show();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	void runme() {
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("requesting conn");
			socket = new Socket(host, port);
			System.out.println("got conn");
			
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(in);
			
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
*/

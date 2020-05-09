/* From Daniel Liang's book */
package practice;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Date;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MultiThreadChatServer extends Observable {

//	private TextArea ta = new TextArea(); 
	public static ArrayList<Item> items = new ArrayList<Item>();	
	private int clientNo = 0; 
	public static ArrayList<Observer> observers = new ArrayList<Observer>();
	
	public static void main(String[] args) {
		try {
			File f = new File("src/practice/items.txt");
			Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				String name = sc.next();
				double price = sc.nextDouble();
				int time = sc.nextInt();
				items.add(new Item(name, price, time));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

		final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	for (Item i : items) {
            		i.setTimeLeft(i.getTimeLeft() - 1);
            		if (i.getTimeLeft() < 0)
            			i.setBiddable(false);
            	}
            }
        }, 0, 1000);
		
//		launch(args);
        new MultiThreadChatServer().start();
	}

	public void start() { 

		new Thread( () -> { 
			try {  // Create a server socket 
				@SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket(8000); 

				while (true) { 
					// Listen for a new connection request 
					Socket socket = serverSocket.accept(); 
					
					clientNo++; // Increment clientNo 

					Client client1 = new Client();
					
					new Thread(new HandleAClient(socket)).start();	// Create and start a new thread for the connection
					this.addObserver(client1);
					observers.add(client1);
					System.out.println("got a client connection");
				} 
			} 
			catch(IOException ex) { 
				System.err.println(ex);
			}
		}).start();
	}


	// Define the thread class for handling
	class HandleAClient implements Runnable {
		private Socket socket; // A connected socket
		/** Construct a thread */ 
		public HandleAClient(Socket socket) { 
			this.socket = socket;
		}
		/** Run a thread */
		public void run() { 
			try {
				// Create data input and output streams
				DataInputStream inputFromClient = new DataInputStream( socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream( socket.getOutputStream());
				ObjectOutputStream oos = new ObjectOutputStream(outputToClient);
				ObjectInputStream ois = new ObjectInputStream(inputFromClient);
				
				int amount = items.size();
				outputToClient.writeInt(amount);
				for (Item i : items) {
					oos.writeObject(i.getItemName());
					oos.writeObject(i.getCurrPrice());
					oos.writeObject(i.getTimeLeft());
				}
				
		        
				// Continuously serve the client
				while (true) {
					int commandNo = inputFromClient.readInt();
					switch(commandNo) {
					case 1: {
						for (Item i : items) {
							outputToClient.writeDouble(i.getCurrPrice());
							outputToClient.writeInt(i.getTimeLeft());
						}
						break;
					}
					case 2: {
						int index = inputFromClient.readInt();
						double price = inputFromClient.readDouble();
						//Label[] arrLab = (Label[]) ois.readObject();
						if (price > items.get(index).getCurrPrice() && items.get(index).isBiddable() == true) {
							items.get(index).setCurrPrice(price);
//							setChanged();
//							Object[] objs = new Object[3];
//							objs[0] = index; objs[1] = price; //objs[2] = arrLab;
//	System.out.println("index is " + index);
//							for (Observer o : observers) {
//								((Client) o).updatee(objs);
//							}
							//notifyObservers(objs);
							
//							outputToClient.writeInt(2);	//command for setting a new price
//							outputToClient.writeInt(index);
//							outputToClient.writeDouble(price);
						}
						break;
					}
					
					}

				}
				
			} catch(IOException e) {
				e.printStackTrace();
			} //catch (ClassNotFoundException e) {
				//e.printStackTrace();
			//}
		}
	}
	
}

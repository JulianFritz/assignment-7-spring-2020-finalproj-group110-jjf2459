/*  
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Julian Fritz
 * jjf2459
 * 16300
 * Spring 2020
 */

package server;

import java.io.*;
import java.net.*;
import java.util.*;


public class Server {

	public static ArrayList<Item> items = new ArrayList<Item>();	
	public static ArrayList<String> customers = new ArrayList<String>();

	public static void main(String[] args) {
		//Reading Items in from a file:
		try {	
			File f = new File("assignment7Server/server/items.txt");
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

		//Counts down the time left for bidding on items
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

		//Start the server
		new Server().start();
	}

	/**
	 * Listens for socket connections, then creates a thread to handle them
	 */
	public void start() { 

		new Thread( () -> { 
			try {  // Create a server socket 
				@SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket(8000); 

				while (true) { 

					//Listen for a new connection request 
					Socket socket = serverSocket.accept(); 

					// Create and start a new thread for the connection
					new Thread(new HandleAClient(socket)).start();	

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
		public String username;

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

				//give the client the item info
				int amount = items.size();
				outputToClient.writeInt(amount);
				for (Item i : items) {
					oos.writeObject(i.getItemName());
					oos.writeObject(i.getCurrPrice());
					oos.writeObject(i.getTimeLeft());
				}

				boolean go = true;
				// Continuously serve the client
				while (go) {
					//read an integer that signals which command to carry out
					int commandNo = inputFromClient.readInt();
					switch(commandNo) {
					case 1: {	//update labels command
						for (Item i : items) {
							outputToClient.writeDouble(i.getCurrPrice());
							outputToClient.writeInt(i.getTimeLeft());
							oos.writeObject(i.getHighestBidder());
						}
						break;
					}
					case 2: {	//new bid command
						int index = inputFromClient.readInt();		//indicates which item was bid on
						double price = inputFromClient.readDouble();	//the price that was bid
						String user = (String) ois.readObject();		//the user that bid
						if (price == -1.0)		//means there was an error
							break;
						if (price > items.get(index).getCurrPrice() && items.get(index).isBiddable() == true) {
							items.get(index).setCurrPrice(price);
							items.get(index).setHighestBidder(user);
						}
						break;
					}
					case 3: {	//login command
						username = (String) ois.readObject();
						customers.add(username);
						break;
					}
					case 4: {	//quit command
						oos.close();
						ois.close();
						go = false;
						break;
					}

					}
				}

			} catch(IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}

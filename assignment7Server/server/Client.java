/*  
 * EE422C Final Project submission by
 * Replace <...> with your actual data.
 * Julian Fritz
 * jjf2459
 * 16300
 * Spring 2020
 */

package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Client extends Application {
	int port = 8000;
	String host = "localhost";
	public DataInputStream in;
	public DataOutputStream out;
	Socket socket;
	public Label[] priceLbls;
	public Label[] timeLbls;
	public Label[] highBidLbls;
	public Label[] errorLbls;
	public double[] prices;
	public int[] times;
	String username;
	
	public Object lock = new Object();
	

	/**
	 * JavaFX application start method
	 * @param primaryStage - the stage given when Application.launch(args) is called
	 */
	@Override
	public void start(Stage primaryStage) {				
		try {
			System.out.println("requesting conn");
			socket = new Socket(host, port);
			System.out.println("got conn");
			
			//create data streams
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(in);
			ObjectOutputStream oos = new ObjectOutputStream(out);
			
			//Grid for items
			GridPane itemGrid = new GridPane();
			itemGrid.setHgap(50);
			itemGrid.setVgap(10);

			out.writeInt(1);	//command to get the amount and names of items
			int noOfItems = in.readInt();
			
			//arrays to keep track of the things initialized in the following for loop
			priceLbls = new Label[noOfItems];
			timeLbls = new Label[noOfItems];
			highBidLbls = new Label[noOfItems];
			errorLbls = new Label[noOfItems];
			prices = new double[noOfItems];
			times = new int[noOfItems];
			
			for (int i = 0; i < noOfItems; i++) {
				try {
					//Reading in Item info for Item Labels
					Label itemNameLbl = new Label((String) ois.readObject());
					Label currPriceLbl = new Label("Current Price: " + ((Double) ois.readObject()).toString());
					Label timeLeftLbl = new Label("Time left for bidding: " + ((Integer) ois.readObject()).toString());
					Label highBidLbl = new Label("Highest Bidder: <none>");
					Label errorLbl = new Label("");

					priceLbls[i] = currPriceLbl;
					timeLbls[i] = timeLeftLbl;
					highBidLbls[i] = highBidLbl;
					errorLbls[i] = errorLbl;

					TextField priceInput = new TextField();

					//Bid Button Code
					int index = i;
					Button bidBtn = new Button("Bid");
					bidBtn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							synchronized (lock) {	//so that the timer thread doesn't interrupt
								try {
									out.writeInt(2);	//command number for bid is 2
									out.writeInt(index);
									double price = Double.parseDouble(priceInput.getText());
									price = Math.floor(price*100) / 100;	//truncate to 2 decimal places
									out.writeDouble(price);
									oos.writeObject(username);

									priceInput.clear();

									if (price <= prices[index]) {
										errorLbl.setText("Your bid must be greater");
									}

									if (times[index] < 0) {
										errorLbl.setText("This item has been sold");
									}

								} catch (IOException e1) {
									e1.printStackTrace();
								} catch (NumberFormatException e1) {	//If parseDouble failed
									System.out.println("try again, invalid price");
									try {
										out.writeDouble(-1.0);	//tells the server it was an invalid price
										oos.writeObject(username);	//server is expecting to get a String, can't leave it hanging
										errorLbl.setText("Please enter a valid number");
										priceInput.clear();
									} catch (IOException e2) {}
								}
							}
						}
					});
					
					//add all the Labels and buttons to the grid
					itemGrid.add(itemNameLbl, i, 0);
					itemGrid.add(currPriceLbl, i, 1);
					itemGrid.add(highBidLbl, i, 2);
					itemGrid.add(timeLeftLbl, i, 3);
					itemGrid.add(priceInput, i, 4);
					itemGrid.add(bidBtn, i, 5);
					itemGrid.add(errorLbl, i, 6);

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			//quit button for the Auction tab
			Button quitBtn = new Button("Log out");
			quitBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try {
						out.writeInt(4);	//quit command code is 4
						oos.close();
						ois.close();
					} catch (IOException e1) {
					}
					System.exit(0);
				}
			});
			itemGrid.add(quitBtn, 0, 8);
			
			//quit button for the Log in tab
			Button quitBtn2 = new Button("Quit");
			quitBtn2.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try {
						out.writeInt(4);	//quit command code is 4
						oos.close();
						ois.close();
					} catch (IOException e1) {
					}
					System.exit(0);
				}
			});
			
			//Login Screen
			GridPane loginGrid = new GridPane();
			loginGrid.setHgap(50);
			loginGrid.setVgap(50);
			
			TextField usernameTF = new TextField();
			
			Button loginBtn = new Button("Log In");
			
			//add components to the Login Grid
			loginGrid.add(usernameTF, 0, 0);
			loginGrid.add(loginBtn, 1, 0);
			loginGrid.add(quitBtn2, 0, 4);
			
			//Make the Auction scrollable
			ScrollPane scroll = new ScrollPane();
			scroll.setPrefSize(500, 500);
			scroll.setContent(itemGrid);
			scroll.setPannable(true);
			
			//Create tabs
			Tab login = new Tab("Login");
			login.setContent(loginGrid);
			login.setClosable(true);
			
			Tab auction = new Tab("Auction");
			auction.setContent(scroll);
			auction.setClosable(false);
			auction.setDisable(true);	//starts disabled so that you have to login first
			
			//login Button handler
			loginBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					synchronized (lock) {	//make sure it won't get interrupted by the TimeLine timer
						try {
							out.writeInt(3);	//command for giving username
							oos.writeObject(usernameTF.getText());
						} catch (IOException e1) {}
						
						username = usernameTF.getText();
						auction.setDisable(false);
						login.setDisable(true);
					}
				}
			});
			
			//Create a tabpane to hold the 2 tabs
			TabPane tabs = new TabPane();
			tabs.getTabs().add(login);
			tabs.getTabs().add(auction);

			//Make a scene, why don't you!!!
			Scene scene = new Scene(tabs, 1500, 350);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Auction Client");
			primaryStage.show();
			
			//Animation timer that refreshes the labels
			Timeline timer = new Timeline(new KeyFrame(Duration.millis(100), event -> {
				try {
					synchronized(lock) {	//so that we don't interrupt anything
						out.writeInt(1);	//command number for refresh is 1
						for (int i = 0; i < noOfItems; i++) {
							double price = in.readDouble();
							int time = in.readInt();
							String bidder = (String)ois.readObject();
							setLabels(i, price, time, bidder);

							//to keep track of the updated prices and times locally
							prices[i] = price;
							times[i] = time;
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}				
			}));
			timer.setCycleCount(Animation.INDEFINITE);
			timer.play();			


		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * sets the labels for a given index of Item
	 * @param index - the item whose labels we're changing
	 * @param price - the price of that item to update the price label
	 * @param time - the amount of time left for bidding on that item
	 * @param user - the username of the highest bidder
	 */
	public void setLabels(int index, double price, int time, String user) {
		if (time >= 0) {
			int minutes = time / 60;
			int seconds = time % 60;
			timeLbls[index].setText("Bidding ends in: " + minutes + " min and " + seconds + " sec");
			priceLbls[index].setText("Current Price: " + price);
			highBidLbls[index].setText("Highest Bidder: " + user);
		}
		else {
			timeLbls[index].setText("Bidding has ended  " + time);
			priceLbls[index].setText("Sold for: " + price + " to " + user);
			highBidLbls[index].setText("Highest Bidder: " + user);
		}
		if (!(errorLbls[index].getText().equals(""))) { //if the error label is not empty
			try {
				Thread.sleep(1000);		//the error stays up for 1 second
			} catch (InterruptedException e) {}
			
			errorLbls[index].setText("");	//clear the error
		}
		
	}
	
}

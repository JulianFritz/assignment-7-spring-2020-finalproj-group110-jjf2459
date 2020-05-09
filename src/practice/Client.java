package practice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import practice.MultiThreadChatServer.HandleAClient;

public class Client extends Application implements Observer {
	int port = 8000;
	String host = "localhost";
	public DataInputStream in;
	public DataOutputStream out;
	Socket socket;
	public Label[] priceLbls = new Label[5];
	public Label[] timeLbls = new Label[5];
	
	
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
			ObjectOutputStream oos = new ObjectOutputStream(out);
			
			//Grid for items
			GridPane itemGrid = new GridPane();
			itemGrid.setHgap(50);
			itemGrid.setVgap(10);

			out.writeInt(1);	//command to get the amount and names of items
			int noOfItems = in.readInt();
			
			priceLbls = new Label[noOfItems];
			timeLbls = new Label[noOfItems];
			
			for (int i = 0; i < noOfItems; i++) {
				try {
					Label itemNameLbl = new Label((String) ois.readObject());
					Label currPriceLbl = new Label("Current Price: " + ((Double) ois.readObject()).toString());
					Label timeLeftLbl = new Label("Time left for bidding: " + ((Integer) ois.readObject()).toString());

					priceLbls[i] = currPriceLbl;
					timeLbls[i] = timeLeftLbl;
	System.out.println("Added pricelabel " + i);
	//System.out.println(priceLbls[i]);

					
					TextField priceInput = new TextField();

					int index = i;
					Button bidBtn = new Button("Bid");
					bidBtn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							try {
								out.writeInt(2);	//command number for bid is 2
								out.writeInt(index);
								double price = Double.parseDouble(priceInput.getText());
								out.writeDouble(price);

								//setLabel(index, price);
								
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					});
					
//					Button updateBtn = new Button("Refresh");
//					updateBtn.setOnAction(new EventHandler<ActionEvent>() {
//						@Override
//						public void handle(ActionEvent e) {
//							try {
//								out.writeInt(1);	//command number for refresh is 1
//								for (int i = 0; i < noOfItems; i++) {
//									setBothLabel(i, in.readDouble(), in.readInt());
//								}
//								
//							} catch (IOException e1) {
//								e1.printStackTrace();
//							}
//						}
//					});

					itemGrid.add(itemNameLbl, i, 0);
					itemGrid.add(currPriceLbl, i, 1);
					itemGrid.add(timeLeftLbl, i, 2);
					itemGrid.add(priceInput, i, 3);
					itemGrid.add(bidBtn, i, 4);
					//if (i==0)
						//itemGrid.add(updateBtn, i, 5);

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			Scene scene = new Scene(itemGrid, 1500, 300);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Auction Client");
			primaryStage.show();
			
			Timeline timer = new Timeline(new KeyFrame(Duration.millis(100), event -> {
				try {
					out.writeInt(1);	//command number for refresh is 1
					for (int i = 0; i < noOfItems; i++) {
						setBothLabel(i, in.readDouble(), in.readInt());
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			}));
			timer.setCycleCount(Animation.INDEFINITE);
			timer.play();
			
//			final Timer timer = new Timer();
//	        timer.scheduleAtFixedRate(new TimerTask() {
//	            public void run() {
//	            	try {
//						out.writeInt(1);	//command number for refresh is 1
//						for (int i = 0; i < noOfItems; i++) {
//							setBothLabel(i, in.readDouble(), in.readInt());
//						}
//						
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//	            }
//	        }, 0, 100);
			
			//out.writeInt(3); 	//ready for timer to start
			
//			new Thread( () -> { 
//				try {
//					while(true) {
//						int commandNo = in.readInt();
//						switch (commandNo) {
//						case 1: {
//							int idx = in.readInt();
//							timeLbls[idx].setText("Time left for bidding: " + in.readInt());
//							break;
//						}
//						case 2: {
//							int idx = in.readInt();
//							double price = in.readDouble();
//							setLabel(idx, price);
//							break;
//						}
//
//						}
//					}
//				} catch (IOException e) {
//
//				}
//
//			}).start();
			


		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setPriceLabel(int index, double price) {
		priceLbls[index].setText("Current Price: " + price);
	}
	
	public void setBothLabel(int index, double price, int time) {
		if (time >= 0) {
			timeLbls[index].setText("Time left for bidding: " + time);
			priceLbls[index].setText("Current Price: " + price);
		}
		else {
			timeLbls[index].setText("Bidding has ended  " + time);
			priceLbls[index].setText("Sold for: " + price);
		}
	}

	//@Override
	public void updatee(/*Observable obs,*/ Object[] arg) {
		Object[] objs = (Object[]) arg;
		
		int index = ((Integer) objs[0]);
		double price = ((Double) objs[1]);
		//Label[] priceLabels = ((Label[]) objs[2]);
		
		System.out.println("Client side index: " + index + " and price: " + price);
		System.out.println(socket);
		System.out.println(priceLbls[0]);
		
		//priceLabels[index].setText("Current Price: " + price);
		setPriceLabel(index, price);
		//priceLbls[index].setText("Current Price: " + price);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
}

//package client;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.util.*;
//
//import javafx.animation.Animation;
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.Tab;
//import javafx.scene.control.TabPane;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//public class Client extends Application implements Observer {
//	int port = 8000;
//	String host = "localhost";
//	public DataInputStream in;
//	public DataOutputStream out;
//	Socket socket;
//	public Label[] priceLbls;
//	public Label[] timeLbls;
//	public Label[] highBidLbls;
//	public Label[] errorLbls;
//	public double[] prices;
//	public int[] times;
//	String username;
//	Object lock = new Object();
//	
//	
//	//public static void main(String[] args) {
//	//	Application.launch(args);
//	//}
//
//	@Override
//	public void start(Stage primaryStage) {				
//		try {
//			System.out.println("requesting conn");
//			socket = new Socket(host, port);
//			System.out.println("got conn");
//			
//			in = new DataInputStream(socket.getInputStream());
//			out = new DataOutputStream(socket.getOutputStream());
//			ObjectInputStream ois = new ObjectInputStream(in);
//			ObjectOutputStream oos = new ObjectOutputStream(out);
//			
//			//Grid for items
//			GridPane itemGrid = new GridPane();
//			itemGrid.setHgap(50);
//			itemGrid.setVgap(10);
//
//			out.writeInt(1);	//command to get the amount and names of items
//			int noOfItems = in.readInt();
//			
//			priceLbls = new Label[noOfItems];
//			timeLbls = new Label[noOfItems];
//			highBidLbls = new Label[noOfItems];
//			errorLbls = new Label[noOfItems];
//			prices = new double[noOfItems];
//			times = new int[noOfItems];
//			
//			for (int i = 0; i < noOfItems; i++) {
//				try {
//					Label itemNameLbl = new Label((String) ois.readObject());
//					Label currPriceLbl = new Label("Current Price: " + ((Double) ois.readObject()).toString());
//					Label timeLeftLbl = new Label("Time left for bidding: " + ((Integer) ois.readObject()).toString());
//					Label highBidLbl = new Label("Highest Bidder: <none>");
//					Label errorLbl = new Label("");
//					
//					priceLbls[i] = currPriceLbl;
//					timeLbls[i] = timeLeftLbl;
//					highBidLbls[i] = highBidLbl;
//					errorLbls[i] = errorLbl;
//	System.out.println("Added pricelabel " + i);
//	//System.out.println(priceLbls[i]);
//
//					
//					TextField priceInput = new TextField();
//
//					int index = i;
//					Button bidBtn = new Button("Bid");
//					bidBtn.setOnAction(new EventHandler<ActionEvent>() {
//						@Override
//						public void handle(ActionEvent e) {
//							try {
//								out.writeInt(2);	//command number for bid is 2
//								out.writeInt(index);
//								double price = Double.parseDouble(priceInput.getText());
//								price = Math.floor(price*100) / 100;
//								out.writeDouble(price);
//								oos.writeObject(username);
//
//								priceInput.clear();
//								
//								if (price <= prices[index]) {
//									errorLbl.setText("Your bid must be greater");
//								}
//								
//								if (times[index] < 0) {
//									errorLbl.setText("This item has been sold");
//								}
//
////								double code = ois.readDouble();
////								if (code == -1)
////									errorLbl.setText("Bid value too small");
////								if (code == -2)
////									errorLbl.setText("Bidding has ended for this item");
//								
//							} catch (IOException e1) {
//								e1.printStackTrace();
//							} catch (NumberFormatException e1) {
//								System.out.println("try again, invalid price");
//								try {
//									out.writeDouble(-1.0);	//tells the server it was an invalid price
//									oos.writeObject(username);
//									errorLbl.setText("Please enter a valid number");
//									priceInput.clear();
//								} catch (IOException e2) {}
//							}
//						}
//					});
//					
////					Button updateBtn = new Button("Refresh");
////					updateBtn.setOnAction(new EventHandler<ActionEvent>() {
////						@Override
////						public void handle(ActionEvent e) {
////							try {
////								out.writeInt(1);	//command number for refresh is 1
////								for (int i = 0; i < noOfItems; i++) {
////									setBothLabel(i, in.readDouble(), in.readInt());
////								}
////								
////							} catch (IOException e1) {
////								e1.printStackTrace();
////							}
////						}
////					});
//
//					itemGrid.add(itemNameLbl, i, 0);
//					itemGrid.add(currPriceLbl, i, 1);
//					itemGrid.add(highBidLbl, i, 2);
//					itemGrid.add(timeLeftLbl, i, 3);
//					itemGrid.add(priceInput, i, 4);
//					itemGrid.add(bidBtn, i, 5);
//					itemGrid.add(errorLbl, i, 6);
//					//if (i==0)
//						//itemGrid.add(updateBtn, i, 5);
//
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//
//			//quit button
//			Button quitBtn = new Button("Log out");
//			quitBtn.setOnAction(new EventHandler<ActionEvent>() {
//				@Override
//				public void handle(ActionEvent e) {
//					System.exit(0);
//				}
//			});
//			itemGrid.add(quitBtn, 0, 8);
//			Button quitBtn2 = new Button("Quit");
//			quitBtn2.setOnAction(new EventHandler<ActionEvent>() {
//				@Override
//				public void handle(ActionEvent e) {
//					System.exit(0);
//				}
//			});
//			
//			//Login Screen
//			GridPane loginGrid = new GridPane();
//			loginGrid.setHgap(50);
//			loginGrid.setVgap(50);
//			
//			TextField usernameTF = new TextField();
//			
//			Button loginBtn = new Button("Log In");
//			
//			loginGrid.add(usernameTF, 0, 0);
//			loginGrid.add(loginBtn, 1, 0);
//			loginGrid.add(quitBtn2, 0, 4);
//			
//			
//			Tab login = new Tab("Login");
//			login.setContent(loginGrid);
//			login.setClosable(true);
//			
//			Tab auction = new Tab("Auction");
//			auction.setContent(itemGrid);
//			auction.setClosable(false);
//			auction.setDisable(true);
//			
//			loginBtn.setOnAction(new EventHandler<ActionEvent>() {
//				@Override
//				public void handle(ActionEvent e) {
//					try {
//						out.writeInt(3);	//command for giving username
//						oos.writeObject(usernameTF.getText());
//					} catch (IOException e1) {}
//					username = usernameTF.getText();
//					auction.setDisable(false);
//					login.setDisable(true);
//				}
//			});
//			
//			TabPane tabs = new TabPane();
//			tabs.getTabs().add(login);
//			tabs.getTabs().add(auction);
//			
//			
//			Scene scene = new Scene(tabs, 1500, 350);
//			primaryStage.setScene(scene);
//			primaryStage.setTitle("Auction Client");
//			primaryStage.show();
//			
//			Timeline timer = new Timeline(new KeyFrame(Duration.millis(100), event -> {
//				try {
//					out.writeInt(1);	//command number for refresh is 1
//					for (int i = 0; i < noOfItems; i++) {
//						double price = in.readDouble();
//						int time = in.readInt();
//						String bidder = (String)ois.readObject();
//						setLabels(i, price, time, bidder);
//						
//						prices[i] = price;
//						times[i] = time;
//					}
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				} catch (ClassNotFoundException e1) {
//					e1.printStackTrace();
//				}				
//			}));
//			timer.setCycleCount(Animation.INDEFINITE);
//			timer.play();
//			
////			final Timer timer = new Timer();
////	        timer.scheduleAtFixedRate(new TimerTask() {
////	            public void run() {
////	            	try {
////						out.writeInt(1);	//command number for refresh is 1
////						for (int i = 0; i < noOfItems; i++) {
////							setBothLabel(i, in.readDouble(), in.readInt());
////						}
////						
////					} catch (IOException e1) {
////						e1.printStackTrace();
////					}
////	            }
////	        }, 0, 100);
//			
//			//out.writeInt(3); 	//ready for timer to start
//			
////			new Thread( () -> { 
////				try {
////					while(true) {
////						int commandNo = in.readInt();
////						switch (commandNo) {
////						case 1: {
////							int idx = in.readInt();
////							timeLbls[idx].setText("Time left for bidding: " + in.readInt());
////							break;
////						}
////						case 2: {
////							int idx = in.readInt();
////							double price = in.readDouble();
////							setLabel(idx, price);
////							break;
////						}
////
////						}
////					}
////				} catch (IOException e) {
////
////				}
////
////			}).start();
//			
//
//
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void setPriceLabel(int index, double price) {
//		priceLbls[index].setText("Current Price: " + price);
//	}
//	
//	public void setLabels(int index, double price, int time, String user) {
//		if (time >= 0) {
//			timeLbls[index].setText("Time left for bidding: " + time);
//			priceLbls[index].setText("Current Price: " + price);
//			highBidLbls[index].setText("Highest Bidder: " + user);
//		}
//		else {
//			timeLbls[index].setText("Bidding has ended  " + time);
//			priceLbls[index].setText("Sold for: " + price + " to " + user);
//			highBidLbls[index].setText("Highest Bidder: " + user);
//		}
//		if (!(errorLbls[index].getText().equals(""))) { //"if the error label is not empty"
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//
//			}
//			errorLbls[index].setText("");
//		}
//		
//	}
//
//	//@Override
//	public void updatee(/*Observable obs,*/ Object[] arg) {
//		Object[] objs = (Object[]) arg;
//		
//		int index = ((Integer) objs[0]);
//		double price = ((Double) objs[1]);
//		//Label[] priceLabels = ((Label[]) objs[2]);
//		
//		System.out.println("Client side index: " + index + " and price: " + price);
//		System.out.println(socket);
//		System.out.println(priceLbls[0]);
//		
//		//priceLabels[index].setText("Current Price: " + price);
//		setPriceLabel(index, price);
//		//priceLbls[index].setText("Current Price: " + price);
//		
//	}
//
//	@Override
//	public void update(Observable o, Object arg) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//}

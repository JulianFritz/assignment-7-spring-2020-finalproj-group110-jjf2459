package practice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Client extends Application implements Observer {
	int port = 8000;
	String host = "localhost";
	DataInputStream in;
	DataOutputStream out;
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
								//Label[] priceLbls2 = priceLbls;
								//oos.writeObject(priceLbls2);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					});

					itemGrid.add(itemNameLbl, i, 0);
					itemGrid.add(currPriceLbl, i, 1);
					itemGrid.add(timeLeftLbl, i, 2);
					itemGrid.add(priceInput, i, 3);
					itemGrid.add(bidBtn, i, 4);

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			Scene scene = new Scene(itemGrid, 1500, 500);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Auction Client");
			primaryStage.show();
			
			//out.writeInt(3); 	//ready for timer to start
			
//			while(true) {
//				int commandNo = in.readInt();
//				switch (commandNo) {
//				case 1: {
//					int idx = in.readInt();
//					timeLbls[idx].setText("Time left for bidding: " + in.readInt());
//					break;
//				}
//				case 2: {
//					break;
//				}
//				
//				}
//			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setLabel(int index, double price) {
		priceLbls[index].setText("Current Price: " + price);
	}

	@Override
	public void update(Observable obs, Object arg) {
		Object[] objs = (Object[]) arg;
		
		int index = ((Integer) objs[0]);
		double price = ((Double) objs[1]);
		//Label[] priceLabels = ((Label[]) objs[2]);
		
		System.out.println("Client side index: " + index + " and price: " + price);
		System.out.println(socket);
		System.out.println(priceLbls[0]);
		
		//priceLabels[index].setText("Current Price: " + price);
		setLabel(index, price);
		//priceLbls[index].setText("Current Price: " + price);
		
	}
	
}

package server;

import java.io.*;

public class Item implements Serializable {
	private double startPrice;
	private double currPrice;
	private String itemName;
	private int timeLeft;
	private boolean biddable;
	
	public Item (String itemName, double startPrice, int timeLeft) {
		this.itemName = itemName;
		this.startPrice = startPrice;
		this.currPrice = startPrice;
		this.timeLeft = timeLeft;
		biddable = true;
	}

	public boolean isBiddable() {
		return biddable;
	}

	public void setBiddable(boolean biddable) {
		this.biddable = biddable;
	}

	public double getCurrPrice() {
		return currPrice;
	}

	public void setCurrPrice(double currPrice) {
		this.currPrice = currPrice;
	}
	
	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	@Override
	public String toString() {
		String result = null;
		if (timeLeft >= 0) {
			result = itemName + " currently at $" + currPrice + " with " + timeLeft + " seconds left";
		}
		else {
			result = itemName + " was sold for $" + currPrice;
		}
		return result;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}


//Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
//int idx = 0;
//for (Item i : items) {
//	i.setTimeLeft(i.getTimeLeft() - 1);
//	if (i.getTimeLeft() < 0)
//		i.setBiddable(false);
//	//try {
//	//	outputToClient.writeInt(1);	//command number for updating time
//	//	outputToClient.writeInt(idx);
//	//	outputToClient.writeInt(i.getTimeLeft());
//	//} catch (IOException e) {}
//	idx++;
//}
//}));
//timer.setCycleCount(Animation.INDEFINITE);





//Platform.runLater( () -> { 
// Display the client number 
//ta.appendText("Starting thread for client " + clientNo +
//		" at " + new Date() + '\n'); 
//
//// Find the client's host name, and IP address 
//InetAddress inetAddress = socket.getInetAddress();
//ta.appendText("Client " + clientNo + "'s host name is "
//		+ inetAddress.getHostName() + "\n");
//ta.appendText("Client " + clientNo + "'s IP Address is " 
//		+ inetAddress.getHostAddress() + "\n");	
//}); 





// Create a scene and place it in the stage 
//Scene scene = new Scene(new ScrollPane(ta), 450, 200); 
//primaryStage.setTitle("MultiThreadServer"); // Set the stage title 
//primaryStage.setScene(scene); // Place the scene in the stage 
//ta.appendText("Hello, Welcome to the Multithread Server\n");
//primaryStage.show(); // Display the stage 

//ta.appendText("MultiThreadServer started at " 
//+ new Date() + '\n'); 


package practice;

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

package practice;

import java.io.*;

public class Item implements Serializable {
	private double startPrice;
	private double currPrice;
	private String itemName;
	
	public Item (String itemName, double startPrice) {
		this.itemName = itemName;
		this.startPrice = startPrice;
		this.currPrice = startPrice;
	}
	
	@Override
	public String toString() {
		String result = itemName + " starting at $" + startPrice;
		return result;
	}
}

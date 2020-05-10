package client;

import javafx.application.Application;

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
package server;

import java.io.*;
import java.net.*;


public class ServerMain {

	public static void main(String[] args) {
		Server server = new Server();
		new Thread(new Runnable() {
			@Override
			public void run() {server.runme();}
		}).start();
	}

}
class Server {
	int port = 80000000; //;ASLKDJFAL;SKDJFA;LSKDJF;ALSKDJF;ALSKDJF;LAKSDJF;ALSKDJF;ALSKDJF;ALSKDJF;ALSKDFJ
	DataInputStream in;
	DataOutputStream out;
	ServerSocket serverSock;
	Socket socket;
	
	void runme() {
		try {
			serverSock = new ServerSocket(port);
			socket = serverSock.accept();
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			Double msg = 1.0;
			while(msg != 0.0) {
				msg = in.readDouble();
				out.writeDouble(msg*msg);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
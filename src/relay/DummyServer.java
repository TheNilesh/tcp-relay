package relay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DummyServer implements Runnable{

	ServerSocket srv;
	
	DummyServer() throws IOException{
			srv=new ServerSocket(4065);	//TODO:loop to ensure port number is free
			new Thread(this).start();
	}
	
	String getSocketAddress(){
		return "" + srv.getLocalPort();
	}
	
	public void run(){
		Socket s1;
		Socket s2;
		try {
			s1 = srv.accept();
			s2 = srv.accept();
			new BridgedConnection(s1,s2).start();
			new BridgedConnection(s2,s1).start();
			srv.close();
			System.out.println("Bridge created.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package relay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Relay extends Thread{
	final static Logger LOG = Logger.getLogger(Relay.class.getName()); 
	
	public Hashtable<String,Socket> relayedServers;
	ServerSocket clnt;	//clients
	ServerSocket srv;	//server skeletons
	int cPort;
	int sPort;
	
	Relay(){
		LOG.setLevel(Level.INFO);
		cPort=2025;
		sPort=2026;
		relayedServers=new Hashtable<String,Socket>();
		new Thread(this).start();	//one thread for Servers
		LOG.info("Started listening to servers");
		
		LOG.info("Starting listening to clients");
		acceptClients();			//another for Clients
		
		
	}
	
	public void acceptClients(){
		try {
			clnt=new ServerSocket(cPort);
			while(true){
				Socket cl=clnt.accept();
				LOG.info("Got a client connection");
				ClientRelay cr=new ClientRelay(this,cl);	//client communicating relay
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registerServers(){
		try {
			srv=new ServerSocket(sPort);
			while(true){
				Socket skltn=srv.accept();
				LOG.info("Got a server connection");
				ServerRelay sr=new ServerRelay(this,skltn);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		registerServers();
	}
	
	public static void main(String args[]){
		new Relay();
	}

}

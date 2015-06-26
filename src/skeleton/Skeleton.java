package skeleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import relay.BridgedConnection;

public class Skeleton extends Thread{
	final String servIp="localhost";
	final int servPort=21;
	final String ID="FTP420";
	
	private Socket s;
	PrintWriter oStream;
	BufferedReader iStream;
	
	Skeleton() throws UnknownHostException, IOException{
		s=new Socket("127.0.0.1",2026);
		oStream= new PrintWriter(s.getOutputStream());
		iStream= new BufferedReader(new InputStreamReader(s.getInputStream()));
		new Thread(this).start();
	}
	
	public void run(){
		
		System.out.println("sending identity : " + ID);

		oStream.print(ID + "\n");
		oStream.flush();
		System.out.println("sent");
		

		String recvd=null;
		String[] cmd=null;
		do{
			try {
				recvd=iStream.readLine();
				System.out.println(recvd);
				cmd=recvd.split(" ");
				if(cmd[0].equals("CONNECT_TO")){
					Socket s1=new Socket(cmd[1],Integer.parseInt(cmd[2]));//connection to Dummy Server
					Socket s2=new Socket(servIp,servPort);					//Connection to Server
					//Join both
					new BridgedConnection(s1,s2).start();
					new BridgedConnection(s2,s1).start();
					//oStream.print("SUCCESS");
					break;
				}
			} catch (IOException e) {e.printStackTrace();}
			
		}while(recvd.equalsIgnoreCase("exit"));
		
		try {
			s.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		new Skeleton();
	}
}

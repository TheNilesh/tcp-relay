package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{

	private Socket s;
	final String ip="127.0.0.1";
	private PrintWriter oStream;
	private BufferedReader iStream;
	
	Client() throws UnknownHostException, IOException{
		s=new Socket(ip,2025);
		oStream= new PrintWriter(s.getOutputStream());
		iStream= new BufferedReader(new InputStreamReader(s.getInputStream()));
		new Thread(this).start();
	}
	
	public void run(){
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

		String str="nilesh";
		String recvd=null;
		String[] cmd=null;
		boolean notConnected=true;	//not connected to server, that means connected to relay.
		do{
			try {
				str=br.readLine();		//TODO: if connected to server, its reply might be multiline, this will read only first line
				
				//System.out.println("sending:" + str);
				oStream.print(str + "\n");
				oStream.flush();
				//System.out.println("sent");
				recvd=iStream.readLine();
				System.out.println(recvd);
				
				if(recvd.startsWith("CONNECT_TO ") && notConnected){
					cmd=recvd.split(" ");
					try{
						Socket s1=new Socket(ip, Integer.parseInt(cmd[1]));//connection to Dummy Server
						//modify stream so msg goes to server not to relay
						oStream= new PrintWriter(s1.getOutputStream());
						iStream= new BufferedReader(new InputStreamReader(s1.getInputStream()));
						System.out.println("Connected to Server via Relay:" + cmd[1]);
						s.close();
					}catch(IOException ie){
						System.out.println("Failed connecting Server");
					}
					//oStream.print("SUCCESS" + "\n");
				}
				
			} catch (IOException e) {e.printStackTrace();}
			
		}while(true);
		
	}
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		new Client();
	}
}

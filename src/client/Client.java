package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{

	private Socket s;
	private PrintWriter oStream;
	private BufferedReader iStream;
	
	Client() throws UnknownHostException, IOException{
		s=new Socket("127.0.0.1",2025);
		oStream= new PrintWriter(s.getOutputStream());
		iStream= new BufferedReader(new InputStreamReader(s.getInputStream()));
		new Thread(this).start();
	}
	
	public void run(){
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

		String str="nilesh";
		String recvd=null;
		//String[] cmd=null;
		do{
			try {
				str=br.readLine();
				
				System.out.println("sending:" + str);
				oStream.print(str + "\n");
				oStream.flush();
				System.out.println("sent");
				recvd=iStream.readLine();
				System.out.println(recvd);
				
				/*
				cmd=recvd.split(" ");
				if(cmd[0].equals("CONNECT_TO")){
					Socket s1=new Socket(cmd[1],Integer.parseInt(cmd[2]));//connection to Dummy Server

					oStream.write("SUCCESS");
				}*/
				
			} catch (IOException e) {e.printStackTrace();}
			
		}while(true);
		
	}
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		new Client();
	}
}

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
		
		do{
			try {
				str=br.readLine();
				
				oStream.print(str + "\n");
				oStream.flush();
				if(str.equalsIgnoreCase("exit")){
					break;
				}

				recvd=iStream.readLine();
				System.out.println(recvd);
				
				if(recvd.startsWith("CONNECT_TO ")){
					cmd=recvd.split(" ");
					try{
						System.out.println("Connected to Server via Relay.");
						new Stub(ip, Integer.parseInt(cmd[1]));//connection to Dummy Server
						System.out.println("Disconnected.");
					}catch(IOException ie){
						System.out.println("Failed connecting Server");
					}
				}
				
			} catch (IOException e) {e.printStackTrace();}
			
		}while(true);
		
		try {s.close();	} catch (IOException e) {e.printStackTrace();}
	}//run
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		new Client();
	}
}

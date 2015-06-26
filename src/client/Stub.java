package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

class Stub extends Thread{
Socket server;

	Stub(String ip,int port) throws UnknownHostException, IOException{
		server=new Socket(ip,port);
		
		new Thread(this).start();
		asyncWrite();
	}
	
	private void asyncRead(){
		try {
			BufferedReader iStream= new BufferedReader(new InputStreamReader(server.getInputStream()));
			for (String line = iStream.readLine(); line != null; line = iStream.readLine()) {
			       System.out.println(line);
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void asyncWrite(){
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String command;
		
		try {
			
			PrintWriter oStream= new PrintWriter(server.getOutputStream());
			do{	//loop to receive commands
				try {
					command=br.readLine();
					
					oStream.print(command + "\n");
					oStream.flush();
					if(command.equalsIgnoreCase("exit")){
						break;
					}	
					
				} catch (IOException e) {e.printStackTrace();}
				
			}while(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		asyncRead();
	}
}

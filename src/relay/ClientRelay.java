package relay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientRelay implements Runnable{

	BufferedReader iStream;
	PrintWriter oStream;
	Relay rs;
	
	ClientRelay(Relay rs, Socket cl) throws IOException{
		iStream= new BufferedReader(new InputStreamReader(cl.getInputStream()));
		oStream= new PrintWriter(cl.getOutputStream());
		this.rs=rs;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		try{
			while(true){
				String str;
				Relay.LOG.info("Waiting for client cmd");
				str = iStream.readLine();
				if(str==null){
					break;//while out
				}
				
				System.out.println(str);
				
				if(str.startsWith("LIST")){
					oStream.print("List " + rs.relayedServers.keySet().toString() + "\n");
					Relay.LOG.info("Sent List");
				}else if(str.startsWith("CONN")){
					String cmd[]=str.split(" ");
					System.out.println("Connecting to " + cmd[1]);
					Socket skltn=rs.relayedServers.get(cmd[1]);
					
					if(skltn==null){
						System.out.println(cmd[1] + " is not available.");
						oStream.print(cmd[1] + " not available " + "\n" );
					}else{
						PrintWriter bw=new PrintWriter(skltn.getOutputStream());
						//?
						
						// start a dummy server to map
						DummyServer ds=new DummyServer();
						String tmpAddress=ds.getSocketAddress();
						
						bw.print("CONNECT_TO " + tmpAddress + "\n");		//ask skeleton to connect dummy server
						oStream.print("CONNECT_TO " + tmpAddress  + "\n");		//ask client to connect dummy server
						bw.flush();
					}
				}else{
					oStream.print("Unknown Command" + "\n" );
				}
				
				oStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package relay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
		int i=1;
		try{
			while(i-->0){
				String str;
				Relay.LOG.info("Waiting for client cmd");
				str = "LIST";
				System.out.println(str);
				String cmd[]=str.split(" ");
				switch(cmd[0]){
				case "LIST":
					oStream.print("List " + rs.relayedServers.keySet().toString());
					Relay.LOG.info("Sent List");
					break;
				case "CONN":
					System.out.println("Connecting to " + cmd[1]);
					Socket skltn=rs.relayedServers.get(cmd[1]);
					if(skltn==null){
						System.out.println(cmd[1] + " is not available.");
						oStream.print(cmd[1] + " not available " );
						break;
					}
					
					
					BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(skltn.getOutputStream()));
					//?
					
					// start a dummy server to map
					DummyServer ds=new DummyServer();
					String tmpAddress=ds.getSocketAddress();
					
					
					bw.write("CONNECT_TO " + tmpAddress);		//ask skeleton to connect dummy server
					oStream.print("CONNECT_TO " + tmpAddress);		//ask client to connect dummy server
					break;
				default:
					oStream.print("Unknown Command" );
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

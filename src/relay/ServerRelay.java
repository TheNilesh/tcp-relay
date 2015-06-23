package relay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ServerRelay implements Runnable{
	BufferedReader iStream;
	PrintWriter oStream;
	Relay rs;
	Socket skltn;
	
	ServerRelay(Relay rs, Socket skltn) throws IOException {
		iStream= new BufferedReader(new InputStreamReader(skltn.getInputStream()));
		oStream= new PrintWriter(skltn.getOutputStream());
		this.rs=rs;
		this.skltn=skltn;
		new Thread(this).start();
		Relay.LOG.info("Created thread to handle server");
	}

	@Override
	public void run() {
		//listen to skltn
		String id = "hell";
		try {
			Relay.LOG.info("Waiting for Server identity");
			id = iStream.readLine();
			rs.relayedServers.put(id, skltn);
			Relay.LOG.info("New Server:" + id);
			/*
			while(true){
				Relay.LOG.info("Waiting for resp");
				String str=iStream.readLine();
				if(str.equals("BYE")){
					
				}
			}*/
		}catch(IOException ie){
			rs.relayedServers.remove(id);
			System.out.println("Server disconnected");
		}
	}

}

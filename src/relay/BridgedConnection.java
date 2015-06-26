package relay;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BridgedConnection extends Thread {
	Socket s1;
	Socket s2;
	public BridgedConnection(Socket s1,Socket s2){
		this.s1=s1;
		this.s2=s2;
	}

    public void run() {
        try {
            InputStream is = s1.getInputStream(); 
            OutputStream os = s2.getOutputStream();
            for (int i; (i = is.read()) != -1; i++) {
                os.write(i);
            }
        } catch (IOException e) {
           // e.printStackTrace();
        	System.out.println("Bridge disconnected!");
        	try {
				s1.close();s2.close();
			} catch (IOException e1) {}
        } 
    }

}

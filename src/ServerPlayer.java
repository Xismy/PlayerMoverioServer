import java.io.IOException;
import java.net.ServerSocket;

/**
 * Hebra que acepta solicitudes de conexión.
 * @author iye19
 *
 */
public class ServerPlayer implements Runnable{
	
	@Override
	public void run() {
		try{
			ServerSocket serverSocket = new ServerSocket(1234);
			while(true){
				new Thread(new Player(serverSocket.accept())).start();			
			}
		}
		catch(IOException ex){}
				
	}
}

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Clase principal.
 * @author iye19
 *
 */
public class ServerDiscover {
	static final int LDATOS = 64;
	
	/**
	 * Responde las peticiones de descubrimiento y crea una hebra que acepta clientes.
	 * @param args No se utiliza.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		DatagramSocket socket = new DatagramSocket(1235);//Socket para peticiones de descubrimiento.
		DatagramPacket p = new DatagramPacket(new byte[LDATOS], LDATOS);
		String nombre = "Servidor";		
		
		
		new Thread(new ServerPlayer()).start();
		
		while(true){
			socket.receive(p);
			nombre = "Servidor";
			p.setData(nombre.getBytes());
			p.setLength(nombre.length());
			socket.send(p);
			p.setData(new byte[LDATOS]);
			p.setLength(LDATOS);
		}
	}
}

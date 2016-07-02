
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.jcodec.containers.mp4.MP4Util;



import com.sun.media.jfxmedia.track.VideoTrack;
import com.sun.net.httpserver.*;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;



/**
 * Hebra que implementa un reproductor de m�sica. Recibe peticiones del cliente, ejecuta las acciones correspondientes y responde si es necesario.
 * @author iye19
 *
 */
public class Player implements Runnable{
	
	private Socket socket;
	private BufferedReader sock_in;
	private PrintWriter sock_out;
	private File[] list;
	
	/**
	 * Inicializa los buffers de entrada y salida.
	 * @param s Socket creado al aceptar la petici�n del cliente.
	 */
	Player(Socket s){
		try{
			socket = s;
			sock_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			sock_out = new PrintWriter(s.getOutputStream(), true);
			list = listAllVideos();
			new HTTP(list);
		}
		catch(IOException ex){}
	}
	
	File[] listAllVideos(){
		ArrayList<File> videos = new ArrayList<>();
		File[] roots = File.listRoots();
		for(File root:roots)
			videos.addAll(listVideos(root));
		File[] list = new File[videos.size()];
		videos.toArray(list);
		return list;
	}
	
	ArrayList<File> listVideos(File root){
		ArrayList<File> videos= new ArrayList<>();
		File[] files = root.listFiles();
		if(files == null)
			return videos;
		for(File file:files){
			if(file.getName().endsWith(".mp4")){
				try {
					MP4Util.parseMovie(file.getAbsoluteFile());
					videos.add(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				try {
					if(file.isDirectory() && file.getCanonicalFile().equals(file.getAbsoluteFile()))
						videos.addAll(listVideos(file));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return videos;
	}
	
	@Override
	public void run() {
		String cmd = "";
		boolean end = false;
	
		while(!end){
			try{
				cmd = sock_in.readLine();
				System.out.println(cmd);
				
				println(cmd);
				
				switch (cmd) {
					case"open":
						int index = Integer.parseInt(sock_in.readLine());
						println("8080");
						for(File file:list){
							println("videos/"+file.getName());
						}
						println("");
						break;
							
					case "ls":
						for(File file:list)
							println(file.getName());
						println("");
						break;
						
					case "disconnect":
						end = true;
						println("");
						Thread.sleep(1000);
						sock_out.close();
						try{
							sock_in.close();
						}
						catch(IOException ioex){}
						socket.close();
						break;
		
					default:
						break;
				}
			}
			catch(Exception ex){
				end = true;
				ex.printStackTrace(new PrintWriter(System.out, true));
			}
		}
	}
	
	
	
	/**
	 * Env�a un mensaje por el socket, impidiendo que otra hebra tome el control del buffer durante el proceso.
	 * @param str Mensaje a enviar.
	 */
	void println(String str){
		synchronized (sock_out) {
			sock_out.println(str);
		}
	}
}
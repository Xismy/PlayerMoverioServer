import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*
 * a simple static http server
*/
public class HTTP implements HttpHandler{
	HttpServer server;
	File[] list;
	
	HTTP(File[] list){
		try{
			server = HttpServer.create(new InetSocketAddress(8080), 0);
			this.list = list;
		
			server.createContext("/videos", this);
		    server.setExecutor(null);
		    server.start();
		}
		catch(IOException ioex){
			System.out.println(ioex);
		}
	}


 
	public void handle(HttpExchange t){
		String res = t.getRequestURI().getPath();
		res = res.substring(res.lastIndexOf('/')+1);

		File resFile = null;
		for(File file:list)
			if(file.getName().equals(res))
				resFile = file;
		if(resFile == null)
			return;
		
		try{
	    	InputStream is = new FileInputStream(resFile);
	    	t.sendResponseHeaders(200, resFile.length());
	    	OutputStream os = t.getResponseBody();
	    	byte[] buff =  new byte[4096];
	    	int read;
	    	do{
	    		read = is.read(buff);
	    		os.write(buff, 0, read);
	    	}while(read == buff.length);
	    	is.close();
	    	os.close();
		}
    	catch (IOException ioex) {
			System.out.println(ioex);
		}

	} 
}
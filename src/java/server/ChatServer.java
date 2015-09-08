package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

/**
 *
 * @author Pernille
 */
public class ChatServer
{
    ServerSocket serverSocket;
    private static final Properties properties = Utils.initProperties("server.properties"); 
    BufferedReader in;
    PrintWriter out;
    String input;
    String output;
    
    private void runServer()
  {
      
    int port = Integer.parseInt(properties.getProperty("port"));
    String ip = properties.getProperty("serverIp");
    
    
    Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Sever started. Listening on: "+port+", bound to: "+ip);
    try {
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(ip, port));
      do {
        Socket socket = serverSocket.accept(); //Important Blocking call
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Connected to a client");        
      } while (keepRunning);
    } 
    catch (IOException ex) {
      Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
    
}

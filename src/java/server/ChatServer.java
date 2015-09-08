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
    private ServerSocket serverSocket;
    private static final Properties properties = Utils.initProperties("server.properties"); 
    private BufferedReader in;
    private PrintWriter out;
    private static boolean keepRunning = true;
    private String input;
    private String output;
    private ClientHandler ch;
    
    private void runServer()
  {
      
    int port = Integer.parseInt(properties.getProperty("port"));
    String ip = properties.getProperty("serverIp");
//    int port = 9090;
//    String ip = "localhost";
    
    Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Sever started. Listening on: "+port+", bound to: "+ip);
    try {
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(ip, port));
      do {
        Socket socket = serverSocket.accept(); //Important Blocking call
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Connected to a client");        
        ch = new ClientHandler(socket);
        ch.start();
        
      } while (keepRunning);
    } 
    catch (IOException ex) {
      Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
    public static void main(String[] args)
    {
        String logFile = properties.getProperty("logFile");
        Utils.setLogFile(logFile, ChatServer.class.getName());
        try
        {
        new ChatServer().runServer();
        } finally
        {
            Utils.closeLogger(ChatServer.class.getName());
        }
        
    }
}

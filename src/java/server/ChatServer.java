package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
    private ClientHandler ch;
    private Socket socket;
    private ChatServer cs;
    private ConcurrentMap<String, ClientHandler> clients = new ConcurrentHashMap();
    String[] splitInput = new String[100];

    public void stopServer()
    {
        keepRunning = false;
    }
    
    private void runServer()
    {
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
//    int port = 9090;
//    String ip = "localhost";

        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Sever started. Listening on: " + port + ", bound to: " + ip);
        try
        {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));

            while (keepRunning)
            {
                socket = serverSocket.accept(); //Important Blocking call
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Connected to a client");
                out.println("Connected to a client, welcome");
                //System.out.println(in.readLine());
                String inputToSplit = in.readLine();
                splitInput = inputToSplit.split("#");
                String command = splitInput[0];
                System.out.println("Her er command: " + command);
                if (command.equals("USER"))
                {
                    String userName = splitInput[1];
                    clients.putIfAbsent(userName, ch = new ClientHandler(socket, userName, cs));
                    out.println("Welcome: " + userName);
                    ch.start();
                }

            }
        } catch (IOException ex)
        {
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

    public void sendToAll(String msg)
    {
        System.out.println("Hvad er det her: " + msg);
        for (Map.Entry<String, ClientHandler> entry : clients.entrySet())
        {
            ClientHandler receiver = entry.getValue();
            System.out.println(receiver.userName);
            receiver.sendMSG(msg);
        }
    }
}

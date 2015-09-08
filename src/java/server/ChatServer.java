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
    private ConcurrentMap<String, ClientHandler> clients = new ConcurrentHashMap();
    String[] splitInput = new String[100];

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

            do
            {
                socket = serverSocket.accept(); //Important Blocking call
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Connected to a client");
                out.println("Connected to a client, welcome");
                System.out.println(in.readLine());

                splitInput = in.readLine().split("#");
                String command = splitInput[0];
                if (command.equals("USER"))
                {
                    String userName = splitInput[1];
                    clients.put(userName, ch = new ClientHandler(socket, userName));
                    ch.start();
                } else if (command.equals("MSG"))
                {
                    String[] receivers = splitInput[1].split(",");
                    if (receivers.length == 1 && receivers[0].equals("*"))
                    {
                        //vi går i clients og henter alle de brugere der er logget på
                        for (Map.Entry<String, ClientHandler> entry : clients.entrySet())
                        {
                            ClientHandler receiver = entry.getValue();
                            receiver.sendMSG(splitInput[2]);
                        }
                    } else if (receivers.length == 1)
                    {
                        //VI vil gerne have fat i den clienthandler som navnet i receivers hør sammem
                        //med.
                        ClientHandler receiver = clients.get(receivers[0]);
                        receiver.sendMSG(splitInput[1]);
                    } else
                    {
                        for (String receiver1 : receivers)
                        {
                            ClientHandler receiver = clients.get(receiver1);
                            receiver.sendMSG(splitInput[2]);
                        }
                    }

                }

            } while (keepRunning);
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
}

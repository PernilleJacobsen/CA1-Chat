/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pernille
 */
public class ClientHandler extends Thread
{
    Scanner input;
    PrintWriter out;
    Socket socket;

    public ClientHandler(Socket socket) throws IOException
    {
        input = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    @Override
    public void run()
    {
        String message = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals("stop"))
        {
            out.println(message.toUpperCase());
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
            message = input.nextLine(); //IMPORTANT blocking call
        }
        out.println("stop");//Echo the stop message back to the client for a nice closedown
        try
        {
            socket.close();
        } catch (IOException ex)
        {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Closed a Connection");
    }
}

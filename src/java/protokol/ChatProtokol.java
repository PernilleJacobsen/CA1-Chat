/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protokol;

import java.util.ArrayList;

/**
 *
 * @author Pernille
 */
public class ChatProtokol
{
   public String processInput(String input)
   {
       String[] splitInput= new String [100]; 
       splitInput= input.split("#");
       String command= splitInput[0];
       String userName;
       String recivers;
       String msg;
       
       if(command.equals("USER"))
       {
//           return command, userName;
           return null;
       }
       else if(command.equals("MSG"))
       {
           return null;
       }
       else if(command.equals("STOP"))
       {
           return null;
       }
       else
       {
           return null;
       }
       
   }
}

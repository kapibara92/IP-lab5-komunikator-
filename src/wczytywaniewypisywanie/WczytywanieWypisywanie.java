package wczytywaniewypisywanie;

/**
 * @author Joanna Wójcik
 */

import java.io.*;
import java.net. *;

public class WczytywanieWypisywanie {

//metoda pobierajaca wiadomosc od uzytkwnika    
    public static String inputText() throws IOException
    {
       
        BufferedReader x = new BufferedReader(new InputStreamReader(System.in));
        return x.readLine();
    }
  
    public static void SendAll(Socket socket)throws IOException{
       BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
             System.out.println("podaj widomosc ktora chcesz wyslac");
         String message=inputText();
            toServer.writeBytes("SENDALL "+message+"\n");
    }
    public static void Send(Socket socket)throws IOException{
        String modifiedSentence;
         BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
                    toServer.writeBytes("LISTCLIENTS"+"\n");       
      modifiedSentence = fromServer.readLine();
      
   String[]   logins=modifiedSentence.split(";");
             String user;
        String message;
        Boolean ifUserExist=false;
        
        System.out.println("Podaj nazwe uzytkownika do ktorego chcesz wyslac wiadomosc" );
        user=inputText();
      for(int i=0; i<logins.length; i++)
      {
      if(logins[i].equals(user))
      {
          ifUserExist=true;
      }
        }
      if(ifUserExist==false)
      {
          System.out.println("Nie ma uzytkownika o podanym loginie");
      }
     else
      {
          System.out.println("podaj widomosc ktora chcesz wyslac");
          message=inputText();
          toServer.writeBytes("SEND "+user+" "+message+"\n");
      }
    }
   public static String newLogin(Socket socket, String[] logins) throws IOException{
       String login="";
        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
         Boolean newLogin=false;
      while(newLogin==false)
        {
             System.out.println("Zaloguj się do serwera" );
     
        login=inputText();
         
         toServer.writeBytes("LOGIN "+login+"\n");
      newLogin=true;
      for(int i=0; i<logins.length; i++)
      {
      if(logins[i].equals(login))
      {
          newLogin=false;
          System.out.println("Serwer juz zawiera ten login" );
      }
        }
        }
      return login;
        
   }
    public static void main(String[] args) throws Exception {
        String newText;
       String message="";
        Boolean ifEnd=false;
       
        String login;
          Socket socket = new Socket("localhost", 10105);
        //Wypisz("Połączenie z serwerem NAWIĄZANE... Wpisz login lub \"EXIT\" by zakończyć");
        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
        
         String [] logins;
         Boolean newLogin=false;
        
      
           toServer.writeBytes("LISTCLIENTS"+"\n");       
      String  modifiedSentence = fromServer.readLine();
      System.out.println("lista loginow: "+modifiedSentence );
      
      logins=modifiedSentence.split(";");
      login=newLogin(socket, logins);
      while(ifEnd==false)
      {
         
          
          toServer = new DataOutputStream(socket.getOutputStream());
       toServer.writeBytes("AUTORECEIVE TRUE"+"\n");  
toServer = new DataOutputStream(socket.getOutputStream());
       toServer.writeBytes("GETALL"+"\n"); 
       fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       if(fromServer.ready()==true)
       {
      System.out.println("login:odebrana wiadomosc\n"+ fromServer.readLine());
       }
       // message=getMessage(socket);
        
       System.out.println("Dostępne komendy: SEND, SENDALL, EXIT" );
       newText=inputText();
       if(newText.toUpperCase().equals("SEND")==true)
       {
           Send(socket);
       }
       else if(newText.toUpperCase().equals("EXIT")==true)
        {
            toServer.writeBytes("EXIT"+"\n");
            ifEnd=true;
        }
       else if(newText.toUpperCase().equals("SENDALL")==true)
        {
            SendAll(socket);
        }
         else
        {
        System.out.println("zle polecenia ");
        }
      }
      
        return;
        
    }
}

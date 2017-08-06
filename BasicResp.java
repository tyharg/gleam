package gleambot;

import java.io.*;
import java.net.*;
/**
 *
 * @author Ty
 */
public class BasicResp {
    String channel;
   
    BasicResp(String c){
	channel = c;
    }
    
//Display help message    
public void help( BufferedWriter writer, Socket sock, BufferedReader read, String line){
     try{
	 writer.write("PRIVMSG " + channel + " :Available commands" + "\r\n");
	 writer.write("PRIVMSG " + channel + " :~help - Show list of available commands" + "\r\n");
	 writer.write("PRIVMSG " + channel + " :~w __zip code__" + "\r\n");
	 writer.write("PRIVMSG " + channel + " :weather + zip code at the end of line  - Get weather info" + "\r\n");
	 writer.write("PRIVMSG " + channel + " :~last + _username_" + "\r\n");
	 writer.write("PRIVMSG " + channel + " :last fm with username at end of line - Show most recently played song of last.fm user" + "\r\n");
	 writer.flush();
     }
     catch(Exception E){
	 System.out.println("Error occured");
     }
}
    //
    
    
}



package gleambot;

import java.io.*;
import java.net.*;


/**
 *
 * @author Ty
 */
public class Gleambot {


    public static void main(String[] args) throws Exception {
	//Freenode login details
	String server = "irc.freenode.net";
	String nick = "gleam_bot_";
	String login = "gleam_bot_";
	String channel = "#gleambot";
	
	boolean reading;
	
	//Open socket and connect to server
	Socket socket = new Socket(server, 6667);
	BufferedWriter writer = new BufferedWriter(
		new OutputStreamWriter(socket.getOutputStream()));
	BufferedReader reader = new BufferedReader(
		new InputStreamReader(socket.getInputStream()));
       
        //Log on to server
	writer.write("NICK " + nick + "\r\n");
	writer.write("USER " + login + " 8 * : Java IRC Gleam Bot\r\n");
	writer.flush();

        String line = null;
	
	//Create objects for basic and API responses
	BasicResp bRes = new BasicResp(channel);
	ApiResp aRes = new ApiResp(channel);

	
        while ((line = reader.readLine( )) != null) {
	    if (line.indexOf("004") >= 0) {
	        //We are now logged in
	        System.out.println("Connection Established...");
	        break;
	    }
	    else if (line.indexOf("433") >= 0){
	       System.out.println("Nickname is already in use.");
	       return;
	   }
       }
       
       //join channel
        writer.write("JOIN " + channel + "\r\n");
        writer.flush( );
       
       //Keep reading lines from the server.
        while ((line = reader.readLine()) != null) {
	    if (line.toLowerCase().contains("ping") || line.toLowerCase().contains("PING")) {
		// We must respond to PINGs to avoid being disconnected.
		writer.write("PONG " + line.substring(5) + "\r\n");
		writer.flush();
		System.out.println("Ponged server");
	    } 
	    
	    //Call help function with keyword ~help
	    else if (line.toLowerCase().contains("~help") ) {
		bRes.help(writer, socket, reader, line);
	    } 
	    
	    //Call weather function with keywords ~w, weather
	    else if (line.toLowerCase().contains("~w")|| line.toLowerCase().contains("weather")) {
		aRes.zipWeather(writer, socket, reader, line);
	    }
	    
	    //Call last.fm function with keywords ~last, last fm
	    else if (line.toLowerCase().contains("~last")|| line.toLowerCase().contains("last fm")) {
		aRes.lastFM(writer, socket, reader, line);
	    }
	    
	    else {
		// Print the raw line received by the bot.
		System.out.println(line);
	    }
	  
	    
	    
	}
	
    
	
    }

    
}

/*

 */
package gleambot;

//GSON libraries
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//Networking libraries for IRC connection
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;


/**
 *
 * @author Ty
 */
public class ApiResp {
    private String apikey_OWM = "53bf8829017323f0b150b3d180b42f30"; //OpenWeatherMap API key
    private String apikey_LFM = "cd81d8c66419cbf2b503e1248ba96a22"; //Last.FM API Key
    private String channel;
    
    //Start GSON
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    
    ApiResp(String c) {
	channel = c;
    }

    //zipWeather() takes  BufferedWriter, BufferedReader, IRC socket, and IRC line.  
    //Outputs OpenWeatherMap to IRC channel and returns void.
    public void zipWeather ( BufferedWriter writer, Socket sock, BufferedReader read, String line) throws Exception{
	try{
	//Pieces together url using zip code via last 5 characters of input, url parts, and api key.
	String zip = line.substring(line.length() - 5);
	String urlPart1 = "http://api.openweathermap.org/data/2.5/weather?zip=";
	String urlPart2 = ",us&APPID=";
	String url = urlPart1 + zip + urlPart2 + apikey_OWM;
	
	//Hits the URL using the reader() method.  Prints out URL to console.  Stores the json in a String
	String rawJson = reader(url);
	System.out.println("User input hit url at: " + url);
	
	//Creates a weather object from the JSON
	//JSON is mapped according to the openweather API heirarchy
	gleambot.Weather weatherData = gson.fromJson(rawJson, gleambot.Weather.class);
	
	//Get location name
	String locQuote = gson.toJson(weatherData.getName());
	String loc = locQuote.replaceAll("\"", "");
	System.out.println(loc);
	
	//Get wind speed via Wind object
	String windString = gson.toJson(weatherData.getWind());
	gleambot.Wind windData = gson.fromJson(windString, gleambot.Wind.class);
	String wind = gson.toJson(windData.getSpeed());
	
	//get tempurature data via Main object
	String mainString = gson.toJson(weatherData.getMain());
	gleambot.Main tempData = gson.fromJson(mainString, gleambot.Main.class);
	
	//uses getTemp() method to get current temperature, converts from kelvin to F, stores as int
	String temp = gson.toJson(tempData.getTemp());
	double tempDub = ( 1.8 * (Double.parseDouble(temp) - 273) + 32);
	int tempInt = (int)tempDub;
	//uses getTempMax() method to get high temperature, converts from kelvin to F, stores as int
	String high = gson.toJson(tempData.getTempMax());
	double highDub = ( 1.8 * (Double.parseDouble(high) - 273) + 32);
	int highInt = (int)highDub;
	//uses getTempMin() method to get low temperature, converts from kelvin to F, stores as int
	String low = gson.toJson(tempData.getTempMin());
	double lowDub = ( 1.8 * (Double.parseDouble(low) - 273) + 32);
	int lowInt = (int)lowDub;
	
	//gets current conditions via a Weather_ object, which is placed into an array to match JSON formatting
	String descString = gson.toJson(weatherData.getWeather());
	Weather_[] wedder = gson.fromJson(descString, gleambot.Weather_[].class);
	String conditions = gson.toJson(wedder[0].getDescription());
	
	
	//prints data out to console
	System.out.println(temp + ", " + high + ", " + low + ", " + wind + ", " + conditions);
	
	//Sends data to writer for IRC output
	writer.write("PRIVMSG " + channel + " : In " + loc +", it is currently "+ conditions + " & " +  tempInt
	+  "F with maximum temperatures of " + highInt + "F , and minimum temperatures of " + lowInt +
	"F. " + wind + " MPH winds."  + "\r\n"); 
	writer.flush();
	}
	//If something is wrong, alert the user that there may be an error
	catch(Exception e){
	    writer.write("PRIVMSG " + channel + " : Error occured, invalied syntax?" + " \r\n"); 
	    writer.flush();
	}
	
    }
    //lastFM() takes  BufferedWriter, BufferedReader, IRC socket, and IRC line.  
    //Outputs Last.FM recently played data to IRC channel and returns void.
    public void lastFM ( BufferedWriter writer, Socket sock, BufferedReader read, String line) throws Exception{
	try{
	//Get username from IRC line
	String[] parts = line.split(" ");
	String user = parts[parts.length - 1];
	//Build URL
	String urlPart1 = "http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&user=";
	String urlPart2 = "&api_key=";
	String urlPart3 = "&format=json&limit=1";
	String url = urlPart1 + user + urlPart2 + apikey_LFM + urlPart3;
	//Read URL
	String rawJson = reader(url);
	System.out.println("User input hit url at: " + url);
	
	
	//Create objects from JSON and use methods to parse data
	gleambot.LastFM trackData = gson.fromJson(rawJson, gleambot.LastFM.class);
	//RecentTracks
	String recentTrackString = gson.toJson(trackData.getRecenttracks());
	gleambot.Recenttracks recenttracks = gson.fromJson(recentTrackString, gleambot.Recenttracks.class);
	//Tracks
	String trackString = gson.toJson(recenttracks.getTrack());
	Track[] track = gson.fromJson(trackString, gleambot.Track[].class);
	//Artist
	gleambot.Artist artist = gson.fromJson(gson.toJson(track[0].getArtist()), gleambot.Artist.class);
	
	//Store song and artist info in strings
	String artistNowPlaying = gson.toJson(artist.getText());
	String songNowPlaying = gson.toJson(track[0].getName());
	
	System.out.println(user + ": " + songNowPlaying + ", " + artistNowPlaying  );
	
	//Ourput data to IRC line
	writer.write("PRIVMSG " + channel + " : " + user +  " most recently played: " + songNowPlaying + " by " + artistNowPlaying + " on last.fm \r\n"); 
	writer.flush();
	}
	//If something is wrong, alert the user that there may be an error
	catch(Exception e){
	    writer.write("PRIVMSG " + channel + " : Error occured, invalied syntax?" + " \r\n"); 
	    writer.flush();
	}
    }
    

    //reader() takes url as a String and returns json data as a String
    public String reader(String urlString) throws Exception{
	String json;
	BufferedReader reader = null;
	try {
	    URL url = new URL(urlString);
	    //Create BufferedReader object which reads url
	    reader = new BufferedReader(new InputStreamReader(url.openStream()));
	    StringBuffer buffer = new StringBuffer();
	    int read;
	    char[] chars = new char[1024];
	    while ((read = reader.read(chars)) != -1) {
		//Append url data to buffer
		buffer.append(chars, 0, read);
	    }
	    //Place buffer into string
	    json = buffer.toString();
	} finally {
	    //Close reader
	    if (reader != null) {
		reader.close();
	    }
	}
	//Return json string
	return json;
    }

}
    
    



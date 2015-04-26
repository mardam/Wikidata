
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main{
	
	private final static String urlString = "http://www.wikidata.org/w/api.php?action=feedrecentchanges&format=json&feedformat=rss";
	
	public static void main(String[] args){
		String rssFeed = getRSSFeed();
		//System.out.println(string);
		parseString(rssFeed);
	}
	
	
	public static String getRSSFeed(){
		String ret = "";
		URL con;
		try {    
		    con = new URL(urlString);
		    URLConnection connection = con.openConnection();
		    InputStream is = connection.getInputStream();
		    Scanner scanner = new Scanner(is);
			ret = scanner.useDelimiter("\\Z").next();
		    scanner.close();
			is.close();

		} catch (MalformedURLException f) {
		    // TODO Auto-generated catch block
		    f.printStackTrace();
		} catch (IOException f) {
		    // TODO Auto-generated catch block
		    f.printStackTrace();
		}
		return ret;
	}
	
	
	public static void parseString(String string){
	    Map <String, String> updates = new HashMap<>();
	    String [] foo = string.split("<item>");
	    for (String s: foo){
	    	String title = parseTitle(s);
	    	if (!title.contains("Wikidata  - Recent changes")){
		    	String date = parseTime(s);
		    	updates.put(title, date);
		    	System.out.println(title + " | " + date);
	    	}
	    }

	    System.out.println("---------------------------------------------------------");
		
	}
	
	public static String parseTitle(String substring){
		String startString = "<title>";
		String endString = "</title>";
    	int start = substring.indexOf(startString) + new String(startString).length();
    	int end = substring.indexOf(endString);
    	String ret = substring.substring(start, end);
    	return ret;
	}
	
	
	public static String parseTime(String substring){
		String startString = "<pubDate>";
		String endString = "</pubDate>";
    	int start = substring.indexOf(startString) + new String(startString).length();
    	int end = substring.indexOf(endString);
    	String ret = substring.substring(start, end);
    	return ret;
	}
}





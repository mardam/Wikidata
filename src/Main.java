
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Main{
	
	private final static String urlString = "http://www.wikidata.org/w/api.php?action=feedrecentchanges&format=json&feedformat=rss";
	private static int changes;
    private static Map <String, Date> updates = new HashMap<>();
    private final static int sleepingTime = 15000;
	
	public static void main(String[] args){
		while(true){
			createNewReview();
			System.out.println("------------------------------");
			System.out.println(changes);
			try {
				Thread.sleep(sleepingTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return ret;
	}
	
	
	public static void parseString(String string){
	    String [] foo = string.split("<item>");
	    for (String s: foo){
	    	String title = parseTitle(s);
	    	if (!title.contains("Wikidata  - Recent changes")){
		    	Date date = parseTime(s);
		    	if (updates.containsKey(title)){
		    		if (!updates.get(title).equals(date)){
		    			addUpdate(title,date);
		    		}
		    	}
		    	else{
		    		addUpdate(title,date);
		    	}
	    	}
	    }

	    System.out.println("---------------------------------------------------------");
		
	}
	
	public static String parseTitle(String substring){
		String startString = "<title>";
		String endString = "</title>";
    	int start = substring.indexOf(startString) + startString.length();
    	int end = substring.indexOf(endString);
    	String ret = substring.substring(start, end);
    	return ret;
	}
	
	
	public static Date parseTime(String substring){
		String startString = "<pubDate>";
		String endString = "</pubDate>";
		int start = substring.indexOf(startString) + startString.length();
    	int end = substring.indexOf(endString);
    	String timeString = substring.substring(start, end);
    	Date date = null;
    	try{
    		date = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(timeString);
    	}
    	catch (ParseException e){
		    // TODO Auto-generated catch block
		    e.printStackTrace();
    	}
    	return date;
	}
	
	public static void createNewReview(){
		changes = 0;
		String rssFeed = getRSSFeed();
		//System.out.println(string);
		parseString(rssFeed);
	}
	
	public static void addUpdate(String title, Date date){
    	updates.put(title, date);
    	System.out.println(title + " | " + date);
    	changes++;
	}
}
/**
 * WEB services handler
 * 
 * Implements interfaces used to acquire data from various RSS feeds
 */
package nucs;

import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * @author fOcus
 *
 */
public class Main {

	static ParserRSS handler   = null;
	static OracleConnection	dbConnection	=	new OracleConnection();
	
	
	public static void retrieveRssData(String path)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
        	URL url = new URL(path);

        	URLConnection hc	=	url.openConnection();
        	
        	hc.setRequestProperty("User-Agent", "My UberCool App v.0.1");

        	DataInputStream data = new DataInputStream(hc.getInputStream());
			
            SAXParser      saxParser = factory.newSAXParser();
            handler   = new ParserRSS();
            saxParser.parse(data, handler);

        } catch (Throwable err) {
            err.printStackTrace ();
        }
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
    	retrieveRssData("http://feeds.feedburner.com/TheHackersNews?format=xml");
		retrieveRssData("http://www.infoq.com/feed");
		retrieveRssData("http://www.reddit.com/r/technology/.rss");
		
		for( int k=0; k< handler.items.size(); k++ )
		{
			System.out.println(handler.items.get(k).toString());
		}
		
	}

}

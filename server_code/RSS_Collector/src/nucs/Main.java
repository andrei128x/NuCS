/**
 * WEB services handler
 * 
 * Implements interfaces used to acquire data from various RSS feeds
 */
package nucs;


/**
 * @author fOcus
 *
 */
public class Main {

	static ParserRSS	rssDataCollector	=	new ParserRSS();
	
	//static ParserRSS handler   = null;
	static OracleConnection	dbConnection	=	new OracleConnection();
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		rssDataCollector.getRssData("http://www.reddit.com/r/technology/.rss");
		rssDataCollector.getRssData("http://www.infoq.com/feed");
		rssDataCollector.getRssData("http://feeds.feedburner.com/TheHackersNews?format=xml");
		
		for( int k=0; k< rssDataCollector.items.size(); k++ )
		{
			//System.out.println(rssDataCollector.items.get(k).toString());
			dbConnection.addRssArticle( rssDataCollector.items.get(k) );
		}
		
		dbConnection.closeConnection();
	}

}

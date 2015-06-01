package nucsApp;

public class ItemElement {

	public int cat_count	=	0;
	
	public String		title		=	"";
	public String		link		=	"";
	
	// array of string, otherwise the Oracle ARRAY class will NOT parse it ( with ArrayList, it does not work)
	public String[]		category = new String[128]; // dirty fix, see above
	public String		author		=	"";
	public String		pubDate		=	"";
	public String		articleText	=	"";

	public String toString()
	{
		return title + " | " + link + " | " + author + " | " + pubDate + " | " + articleText + " | " + " | ";
	}
	
	public String toSQL()
	{
		String ret = "begin \n add_rss_data('" + title + "', '" + link + "', " + "10" + ", '" + author + "', " + "sysdate" + ", '"  + "'); \n end;";
		System.out.println(ret);
		return ret;
	}

}

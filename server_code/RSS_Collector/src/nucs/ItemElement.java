package nucs;

import java.util.ArrayList;
import java.util.List;

public class ItemElement {

	public String		title		=	"";
	public String		link		=	"";
	public List<String>	category	=	new ArrayList<String>();
	public String		author		=	"";
	public String		pubDate		=	"";
	public String		description	=	"";

	public String toString()
	{
		return title + " | " + link + " | " + author + " | " + pubDate + " | " + description + " | " + " | ";
	}
	
	public String toSQL()
	{
		String ret = "begin \n add_rss_data('" + title + "', '" + link + "', " + "10" + ", '" + author + "', " + "sysdate" + ", '"  + "'); \n end;";
		System.out.println(ret);
		return ret;
	}

}

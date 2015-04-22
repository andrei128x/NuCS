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

}

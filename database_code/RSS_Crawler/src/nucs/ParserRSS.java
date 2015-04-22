package nucs;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParserRSS extends DefaultHandler {

	public List<ItemElement> items	=	new ArrayList<ItemElement>();
	
	private ItemElement currentItem	=	null;
	
	private boolean insideItem		=	false;	// retains whether we are inside an RSS item or not
	private String	currentProperty	=	null;	// retains the current field from the RSS item, used for parsing
	
	/**
	 * Append the current field from the RSS item to the ItemElement
	 * @param data	string data that is currently being read from the XML
	 */
	private void addData(String data)
	{
//		if("description".equals(currentProperty))
//		{
//			System.out.println(data);
//		}
		
		/* Populate the ItemElement object with the corresponding information */
		switch(currentProperty)
		{
		case("title"):			currentItem.title				+=	data;break;
		case("link"):			currentItem.link				+=	data;break;
		case("category"):		currentItem.category.add(data);break;
		
		case("author"):
		case("dc:creator"):		currentItem.author			+=	data;break;
		
		case("pubDate"):		currentItem.pubDate			+=	data;break;
		case("description"):	currentItem.description	+=	data;break;
		}
	}
	
	
	/**
	 * Function callback called when start of element is detected
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
		currentProperty	=	qName;

		if( "item".equals(qName) )
		{
			insideItem		=	true;
			currentItem	=	new ItemElement();
		}

		//System.out.println(" start : " + qName);
    }
	
	
	/**
	 * Function callback called when end of element is detected
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if( "item".equals(qName) )
		{
			insideItem	=	false;
			items.add(currentItem);
        }
	}
	
	
	/**
	 * Function callback called when element data is parsed
	 */
	public void characters(char ch[], int start, int length) throws SAXException
	{
		String data = new String(ch, start, length).trim().replace("\r", "").replace("\n", "");
		
		if(insideItem)
		{
			addData(data);
			//System.out.println("-- data : " + data);
		}
	}
	
}

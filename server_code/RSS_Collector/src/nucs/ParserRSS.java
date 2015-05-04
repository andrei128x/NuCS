package nucs;

import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParserRSS extends DefaultHandler {

	public List<ItemElement> items	=	new ArrayList<ItemElement>();
	
	private ItemElement currentItem	=	null;
	
	private boolean insideItem		=	false;	// retains whether we are inside an RSS item or not
	private String	currentProperty	=	null;	// retains the current field from the RSS item, used for parsing
	
	/**
	 * Retrieve the RSS content of a 
	 * @param path
	 */
	public void getRssData(String path)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
        	URL url = new URL(path);

        	URLConnection hc	=	url.openConnection();
        	
        	hc.setRequestProperty("User-Agent", "My UberCool App v.0.1");

        	DataInputStream data = new DataInputStream(hc.getInputStream());
			
            SAXParser      saxParser = factory.newSAXParser();
            //handler   = new ParserRSS();
            saxParser.parse(data, this);

        } catch (Throwable err) {
            err.printStackTrace ();
        }
	}
	
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
	 * Function callback called when start of XML element is detected
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
	 * Function callback called when end of XML element is detected
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
	 * Function callback called when XML element data is parsed
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

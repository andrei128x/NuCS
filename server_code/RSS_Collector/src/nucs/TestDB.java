package nucs;

import static org.junit.Assert.*;
import nucs.ItemElement;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestDB {

	static OracleConnection	testConnection	=	new OracleConnection();
	ItemElement	item	=	new ItemElement();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	
	@Test
	public void testItem() {
		for(int i=0; i<1000; i++ )
		{
			fillItem(0);
		}
	}
	
	
	@Test
	public void test() {
//		fail("Not yet implemented");
		
		
		System.out.println("Start adding dummy records to the database ...");
		for( int tmpIdx=0; tmpIdx<50000; tmpIdx++ )
		{
			fillItem(tmpIdx);
			testConnection.addRssArticle(item);
			
			if((tmpIdx+1)%100==0)
			{
				System.out.println("  " + (tmpIdx+1) + " records added ...");
			}
		}
		
	}


	// ----- 
	private void fillItem(int idx)
	{
		item.title	= "dummy item " + idx;
		item.link	= "http://localhost/?dummyid=" + idx;

		for(int localIdx=0; localIdx<20; localIdx++)
		{
			item.category[localIdx] = "dummy category " + localIdx;
		}
		
		item.author	=	"dummy dude " + idx;
		item.pubDate=	"1/1/2015";
		item.articleText	=	" some dummy text here : " + idx;
	}
		
}

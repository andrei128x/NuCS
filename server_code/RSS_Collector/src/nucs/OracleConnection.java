package nucs;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

public class OracleConnection {

	private Connection connection = null;

	public OracleConnection()
	{
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			 
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1522:orcl", "system",
					"test");
 
		} catch (SQLException e) {
 
			System.out.println("Connection Exception! Check output console");
			e.printStackTrace();
			return;
 
		}
 
		if (connection != null)
		{
			System.out.println("Connected");
		} else {
			System.out.println("Connection failed (w/o exception) !");
		}
		
		
	}
	
	public int addRssArticle(ItemElement article)
	{

		try {
			String sql = "{call add_rss_data(?, ?, ?, ?, sysdate, ? )}";
			CallableStatement stmt = connection.prepareCall(sql);
			stmt.setString(1, article.title);
			stmt.setString(2, article.link);
			
			String[] abc = {"123a","456b","789c"};
			
			/* TODO: refactor to remove the 'deprecated' warning */
			ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("NUCS_CATEG_LIST_TYPE",connection);
			ARRAY arr_param = new ARRAY(descriptor, connection, article.category);
			
			
			stmt.setArray(3,  arr_param);
			
			stmt.setString(4, article.author);
			stmt.setString(5, article.description);
			
			for(int k=0; k<article.category.length; k++)
			{
				System.out.print(article.category[k] + " | ");
			}
			//System.out.println(" *** ");
			
			stmt.executeUpdate();
//		    
//		    while(rs.next())
//		    {
//		    	int numColumns = rs.getMetaData().getColumnCount();
//	            for ( int i = 1 ; i <= numColumns ; i++ )
//	            {
//	            	System.out.println(rs.getString(i));
//	            }
//		    }
		    
		} catch (SQLException e) {
			System.out.println("SQL Exception raised !");
			e.printStackTrace();
		}
		
		return 0;
	}


	public void closeConnection()
	{
		try {
			connection.close();
			System.out.println("Connection closed correctly");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

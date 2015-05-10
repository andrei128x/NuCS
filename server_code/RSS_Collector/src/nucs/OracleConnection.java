package nucs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
 
		}
 
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
		
		
	}
	
	public void addRssDataIntoOracleDB(ItemElement article)
	{

		try {
			Statement stmt = connection.createStatement();
		    stmt.execute( article.toSQL() );
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
	}

}

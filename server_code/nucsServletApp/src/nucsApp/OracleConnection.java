package nucsApp;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nucsApp.ItemElement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;


@SuppressWarnings("deprecation")
public class OracleConnection {

	
	private	Connection connection = null;
	
	public	String	jdbcResponse	=	null;
	public	String	firstPageID		=	null;
	
	public OracleConnection()
	{
		
		Context initContext = null;
		try {
			initContext = new InitialContext();
		} catch (NamingException e1) {
			jdbcResponse	=	"error getting context";
			e1.printStackTrace();
		}
		
		
		Context envContext = null;
		try {
			envContext = (Context)initContext.lookup("java:/comp/env");
		} catch (NamingException e1) {
			jdbcResponse	=	"error getting environment";
			e1.printStackTrace();
		}
		
		
		DataSource ds = null;
		try {
			ds = (DataSource)envContext.lookup("jdbc/myoracle");
		} catch (NamingException e1) {
			jdbcResponse	=	"error getting data source";
			e1.printStackTrace();
		}
		
		try {
			 
			//connection = DriverManager.getConnection( "jdbc:oracle:thin:@192.168.1.13:1521:orcl", "system", "test");
			connection	=	ds.getConnection();
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			jdbcResponse	=	e.toString();
			e.printStackTrace();
			return;
 
		}
	}

	public void getContent(PrintWriter target)
	{
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
			jdbcResponse	=	"You made it, take control your database now!";
		} else {
			System.out.println("Failed to make connection!");
			jdbcResponse	=	"Failed to make connection!";
		}
		
		try {
			Statement stmt = connection.createStatement();
		    ResultSet rs = stmt.executeQuery( "select * from emp" );
		    
		    while(rs.next())
		    {
		    	target.print("\t<tr>");
		    	
		    	int numColumns = rs.getMetaData().getColumnCount();
	            for ( int i = 1 ; i <= numColumns ; i++ )
	            {
	            	System.out.print(rs.getString(i) + "\t");
	            	target.print("<td>" + rs.getString(i) + "</td>");
	            }
	            System.out.println();
	            target.println(" </tr>");
		    }
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void getArticles(PrintWriter target, int start, int page) throws SQLException
	{
		if (connection != null)
		{
			System.out.println("You made it, take control your database now!");
			jdbcResponse		=	"You made it, take control your database now!";
			
			String		tmpID		= null;
			boolean		firstRow	= true;
			Statement	stmt		= connection.createStatement();

			/* SQL query here */
			ResultSet rs = stmt.executeQuery(
					"select * from nucs_articles where"
					+ " rownum <= " + String.valueOf(page)
					+ " and art_id <= " + start
					+ " order by art_id desc"
				);
		    
		    while(rs.next())
		    {
		    	/* starting with SECOND RAW, start adding comma separator after previous raw */
		    	if(!firstRow)
		    	{
		    		target.println(",\n");
		    	}else{
		    		firstRow = false;
		    	}
		    	
		    	target.print("	{\n");
		    	
		    	tmpID	=	getColumnAsJSON(rs, "art_id");
		    	
		    	if(firstPageID == null){
		    		firstPageID	=	tmpID;
		    	}
		    	
		    	//int numColumns = rs.getMetaData().getColumnCount();
		    	target.println(	tmpID + ",");
		    	target.println(getColumnAsJSON(rs, "title")	+ ",");
		    	target.println(getColumnAsJSON(rs, "link")	+ ",");
		    	target.println(getColumnAsJSON(rs, "text") );
		    	
	            target.print("\t}");
		    };
			    
		    stmt.close();
		    rs.close();
			
		} else {
			System.out.println("Failed to make connection!");
			jdbcResponse	=	"Failed to make connection!";
			throw new SQLException("Some error occured during database connection !");
		}
	}
	
	
	/**
	 * Proceseaza toate categoriile disponibile, SAU toate categoriile atasate unui articol
	 * rezultatul functiei este scris in stream-ul PrintWriter, primit ca parametru
	 * @param target
	 * @param artID
	 * @throws SQLException
	 */
	public void getCategories(PrintWriter target, String artID) throws SQLException
	{
		if (connection != null)
		{
			System.out.println("You made it, take control your database now!");
			jdbcResponse		=	"You made it, take control your database now!";
			
			String		tmpID		= null;
			boolean		firstRow	= true;
			Statement	stmt		= connection.createStatement();

			String	query;
			
			if(artID==null){
				query	=	"select cat_name from nucs_view_categories";
			}else{
				query	=	"select cat_name from nucs_view_categories where art_id=" + artID;
			}
			
			/* SQL query here */
			ResultSet rs = stmt.executeQuery(query);
		    
		    while(rs.next())
		    {
		    	/* starting with SECOND RAW, start adding comma separator after previous raw */
		    	if(!firstRow)
		    	{
		    		target.println(",\n");
		    	}else{
		    		firstRow = false;
		    	}
		    	
		    	target.print("	{\n");
		    	
		    	tmpID	=	getColumnAsJSON(rs, "art_id");
		    	
		    	if(firstPageID == null){
		    		firstPageID	=	tmpID;
		    	}
		    	
		    	//int numColumns = rs.getMetaData().getColumnCount();
		    	target.println(	tmpID + ",");
		    	target.println(getColumnAsJSON(rs, "title")	+ ",");
		    	target.println(getColumnAsJSON(rs, "link")	+ ",");
		    	target.println(getColumnAsJSON(rs, "text") );
		    	
	            target.print("\t}");
		    };
			    
		    stmt.close();
		    rs.close();
			
		} else {
			System.out.println("Failed to make connection!");
			jdbcResponse	=	"Failed to make connection!";
			throw new SQLException("Some error occured during database connection !");
		}
	}
	
	/**
	 * Primeste un ResultSet si un nume de proprietate, si returneaza un rand intreg dintr-un JSON
	 * @param r
	 * @param colName
	 * @return Rand formatat "cheie": "valoare"
	 * @throws SQLException
	 */
	private String getColumnAsJSON(ResultSet r, String colName) throws SQLException
	{
		return( "\t \"" + colName + "\":\"" + r.getString(colName) + "\"");
	}
	
	public void closeConnection()
	{
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public int addRssArticle(ItemElement article)
	{

		try {
			String sql = "{call add_rss_data(?, ?, ?, ?, ?, ? )}";
			CallableStatement stmt = connection.prepareCall(sql);
			stmt.setString(1, article.title);
			stmt.setString(2, article.link);
			stmt.setString(3, article.author);
			stmt.setString(4, article.pubDate);
			stmt.setString(5, article.articleText);
			
			/* TODO: refactor to remove the 'deprecated' warning */
			ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("NUCS_CATEG_LIST_TYPE",connection);
			ARRAY arr_param = new ARRAY(descriptor, connection, article.category);
			
			stmt.setArray(6,  arr_param);

			stmt.executeUpdate();
			stmt.close();

		} catch (SQLException e) {
			System.out.println("SQL Exception raised !");
			e.printStackTrace();
		}
		
		return 0;
	}
	
}

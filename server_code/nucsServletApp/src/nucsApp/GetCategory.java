package nucsApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetCategory extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String	articleID;
		
		System.out.println(">>> : " + request.getQueryString());
//		System.out.println(">>> : " + request.getParameter("start"));
		
		articleID = request.getParameter("art_id");
		
		if(articleID!=null){
			try{
				Integer.valueOf( articleID );	// testeaza daca ID-ul reprezinta un numar
			}catch(Exception e){
				//e.printStackTrace();
				System.out.println("[hah, bad parameter ... SQL injection attempt, maybe !?]");
				articleID = null;	// reseteaza ID-ul, deoarece este invalid
			}finally{
				
			System.out.println("> + : " + articleID );
				
				OracleConnection conn = new OracleConnection();
				
				response.setContentType("application/json");
				response.setHeader("Access-Control-Allow-Origin", "null");
		        PrintWriter writer = response.getWriter();
		        
		        writer.println("{\n \"articles\": [");
		        
		        try {
					conn.getCategories(writer, articleID);	// proceseaza cererea din Oracle DB
				} catch (SQLException e) {
					e.printStackTrace();
					
					/* return empty JSON file */
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
		        
		        writer.println("\n ],\n");
		        
		        /* return start ID of current page */
		        writer.println(" \"current_page\": {" + conn.firstPageID + "}\n}");
		        
		        conn.closeConnection();
			}
		}
	}
}

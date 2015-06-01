package nucsApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetArticles extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int	startIndex	=	Integer.MAX_VALUE;
		int	pageSize	=	4;
		
		System.out.println(">>> : " + request.getQueryString());
//		System.out.println(">>> : " + request.getParameter("start"));
		
		if(request.getParameter("start")!=null){
			try{
				startIndex	=	Integer.valueOf( request.getParameter("start") );
				if(startIndex == 0){
					startIndex	=	Integer.MAX_VALUE;
				}
			}catch(Exception e){
				//e.printStackTrace();
				System.out.println("[hah, bad or missing parameter !]");
			}
		}
		
		System.out.println("> + : " + startIndex );
		
		OracleConnection conn = new OracleConnection();
		
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "null");
        PrintWriter writer = response.getWriter();
        
        writer.println("{\n \"articles\": [");
        
        try {
			conn.getArticles(writer, startIndex, pageSize);
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

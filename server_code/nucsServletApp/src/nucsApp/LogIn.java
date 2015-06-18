package nucsApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LogIn
 */

public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String	user		=	"";
		String	pass		=	"";
		String	logResult	=	"false";
		
		//System.out.println(">>> : " + request.getQueryString());
//		System.out.println(">>> : " + request.getParameter("start"));
		
        // get request parameters for userID and password
        user = request.getParameter("user");
        pass = request.getParameter("pass");
        
        OracleConnection conn = new OracleConnection();
        
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "null");
		PrintWriter writer = response.getWriter();
		
		System.out.println(request.getCookies());
		
        try {
        	// proceseaza cererea din Oracle DB
			if(conn.getLogin(writer, user, pass))
			{
				Cookie loginCookie = new Cookie("user",user);
	            //setting cookie to expiry in 30 mins
	            loginCookie.setMaxAge(30*60);
	            response.addCookie(loginCookie);
	            System.out.println("setting cookie...");
			};
		} catch (SQLException e) {
			e.printStackTrace();
			
			/* return empty JSON file */
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
        
        writer.println("\n}");
        
        conn.closeConnection();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response); // handle both GET and POST operations
	}

}

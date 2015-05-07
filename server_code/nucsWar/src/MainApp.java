

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainApp
 */
@WebServlet(description = "Some nice description here", urlPatterns = { "/MainApp" })

public class MainApp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		OracleConnection conn = new OracleConnection();
		
		response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>JDBC Connection from servlet</title>");
        writer.println("</head>");
        
        
        writer.println("<body bgcolor=white>");
        
        writer.println("<table>");
        
        conn.getContent(writer);
                
        writer.println("</table>");
        
        writer.println("</body>");
        writer.println("</html>");
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

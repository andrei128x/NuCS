package nucsApp;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainApp
 */

public class AddData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		OracleConnection conn = new OracleConnection();
		
		response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        Enumeration<String> e = request.getHeaderNames();
        
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>JDBC Connection from servlet</title>");
        writer.println("</head>");
        
        
        writer.println("<body bgcolor=white>");
        writer.println("<br>[response]<br>");
        
        writer.println("Method: " + request.getMethod() + "<br>");
        writer.println("Request URI: " + request.getRequestURI() + "<br>");
        writer.println("Protocol: " + request.getProtocol() + "<br>");
        writer.println("PathInfo: " + request.getPathInfo() + "<br>");
        writer.println("Remote Address: " + request.getRemoteAddr() + "<br>");
        
        writer.println("[new data]");
        
        writer.println("<table>");
        
        
        conn.getContent(writer);
                
        writer.println("</table>");
        
        writer.println("[end new data]");
        
        writer.println("</body>");
        writer.println("</html>");
        
        conn.closeConnection();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

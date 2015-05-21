package nucsApp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetData extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>GetData handler class</title>");
        writer.println("</head>");
        
        
        writer.println("<body bgcolor=white>Yehaaaaw ");
        writer.println("</body");
        writer.println("</html>");
	}
	
}

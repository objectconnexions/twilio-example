package uk.co.objectconnexions.twilio_spike;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/debug")
public class DebugServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                    
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<body><pre>");
            out.print(SpikeProperties.debug("<br />"));
            out.println("</pre></body>");
    }
}

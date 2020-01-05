package uk.co.objectconnexions.twilio_spike;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;

@WebServlet("/call")
public class CallServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(CallServlet.class);  

    @Override
    public void init(ServletConfig config) throws ServletException {
        String username = SpikeProperties.getProperty("account-sid");
        String password = SpikeProperties.getProperty("auth-token");
        Twilio.init(username, password);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String number = request.getParameter("number");
        String name = request.getParameter("name");
        String explain = request.getParameter("explain");
        String from = SpikeProperties.getProperty("number");

        LOG.info("CALL: " + request);
        
        try {
            Call call = Call.creator(new PhoneNumber(number), new PhoneNumber(from),
                    new URI(SpikeProperties.getProperty("server") + "/demo?name=" + name + "&section=" + explain
                    )).create();
            
            response.setContentType("text/plain");
            response.getWriter().println(call.getSid());
        } catch (URISyntaxException e) {
            throw new ServletException(e);
        }
    }
}

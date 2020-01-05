package uk.co.objectconnexions.twilio_spike;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Say;

@WebServlet("/demo")
public class ResponseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(ResponseServlet.class);  

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String section = request.getParameter("section");
        String name = request.getParameter("name");

        LOG.info("request for response to " + name);
        
        VoiceResponse.Builder voice = new VoiceResponse.Builder();
        
        Say say = new Say.Builder("Bonjour!").voice(Say.Voice.WOMAN)
                .language(Say.Language.FR_FR).build();
        voice.say(say);
        
        say = new Say.Builder("Hello there " + name  + ". " + section + " This is an example of automated message").build();
        voice.say(say);
        
        voice.hangup(new Hangup.Builder().build());

        response.setContentType("application/xml");
        String xml = voice.build().toXml();
        response.getWriter().print(xml);
        
        LOG.debug("sending " + xml);
    }
 
}

package uk.co.objectconnexions.twilio_spike;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.VoiceResponse.Builder;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Number;
import com.twilio.twiml.voice.Redirect;
import com.twilio.twiml.voice.Say;

@WebServlet("/voice")
public class IncomingCallServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(IncomingCallServlet.class);  

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String from = request.getParameter("FromNumber");
        
        LOG.info("incoming call from " + from);
        for (String name : request.getParameterMap().keySet()) {
            LOG.debug(" " + name + ": " + request.getParameter(name));
        }
        
        VoiceResponse.Builder builder = new VoiceResponse.Builder();
        String digits = request.getParameter("Digits");
        if ("1".equals(digits)) {
            call(SpikeProperties.getProperty("land-line-number"), builder);
                                
        } else if ("2".equals(digits)) {
            call(SpikeProperties.getProperty("mobile-number"), builder);
            
        } else {
            if (digits != null) {
                    builder.say(new Say.Builder("Sorry, I don\'t understand that choice.").build());
            }
                builder.gather(
                        new Gather.Builder()
                                .numDigits(1)
                                .say(new Say.Builder("To call the land line, press 1. For mobile, press 2.").build())
                                .build()
                )
                .redirect(new Redirect.Builder(request.getContextPath() + "/voice").build());
        }
        
        response.setContentType("application/xml");
        try {
            String xml = builder.build().toXml();
            response.getWriter().print(xml);
            LOG.debug("sending " + xml);
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void call(String to, Builder builder) {
        builder.say(new Say.Builder("Putting you through! Please note that this call is being recorded. One moment please.").build());
        
        Number dialNumber = new Number.Builder(to).build();
        Dial dial = new Dial.Builder()
                .number(dialNumber)
                .record(Dial.Record.RECORD_FROM_RINGING_DUAL)
                .recordingStatusCallback(SpikeProperties.getProperty("server") + "/recorded")
                .build();
        builder.dial(dial);
        builder.hangup(new Hangup.Builder().build());
    }
}

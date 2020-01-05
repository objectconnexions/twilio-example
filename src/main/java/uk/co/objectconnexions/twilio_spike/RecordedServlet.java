package uk.co.objectconnexions.twilio_spike;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/recorded")
public class RecordedServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(RecordedServlet.class);  
    private S3Upload s3Upload;

    @Override
    public void init() throws ServletException {
        s3Upload = new S3Upload();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOG.info("RECORDED: " + request);
        for (String name : request.getParameterMap().keySet()) {
            LOG.debug(" " +name + ": " + request.getParameter(name));
        }

        String recording = request.getParameter("RecordingUrl") + ".mp3";
        LOG.debug("copying to S3: " + recording);
        s3Upload.copyToS3(recording);
    }

}

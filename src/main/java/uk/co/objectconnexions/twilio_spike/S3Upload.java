package uk.co.objectconnexions.twilio_spike;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;

public class S3Upload {
    private static final Logger LOG = LoggerFactory.getLogger(S3Upload.class);  

    private AmazonS3 s3;
    private int number;

    public S3Upload() {
        AWSCredentials credentials = new BasicAWSCredentials(
          SpikeProperties.getProperty("aws-access-key"), 
          SpikeProperties.getProperty("aws-secret-key")
        );
            
        s3 = AmazonS3ClientBuilder
          .standard()
          .withCredentials(new AWSStaticCredentialsProvider(credentials))
          .withRegion(SpikeProperties.getProperty("aws-region"))
          .build();    
        
        String bucketName = SpikeProperties.getProperty("aws-bucket-name");
        if (s3.doesObjectExist(bucketName, "next_id")) {
            String number_id = s3.getObjectAsString(bucketName, "next_id");
            number = Integer.valueOf(number_id);
        } else {
            number = 1;
        }  
        
       LOG.info(" using " + s3 + " starting at " + number);
    }
    
    public void copyToS3(String url) {
        try {
            String name = "take_" + number++;
            copyToS3(url, name);
            copyToS3(url, "latest");
            
            s3.putObject(SpikeProperties.getProperty("aws-bucket-name"), "next_id",  Integer.toString(number));
        } catch (MalformedURLException e) {
            LOG.error("failed to copy recording to S3", e);

        } catch (IOException e) {
            LOG.error("failed to copy recording to S3", e);
        }
    }

    private void copyToS3(String url, String name) throws IOException, MalformedURLException {
        InputStream stream = new URL(url).openStream();
        String bucketName = SpikeProperties.getProperty("aws-bucket-name");
        PutObjectResult result = s3.putObject(bucketName, "recording/" + name + ".mp3", stream, null);
        LOG.info("stored recording with md5 " + result.getContentMd5());
    }
    
    public static void main(String[] args) {
        System.out.println("copying " + args[0]);
        new S3Upload().copyToS3("https://www.twilio.com/console/voice/logs/recordings/" + args[0]);
    }
}

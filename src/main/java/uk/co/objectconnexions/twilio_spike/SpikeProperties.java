package uk.co.objectconnexions.twilio_spike;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SpikeProperties {

    private static final Logger LOG = LoggerFactory.getLogger(SpikeProperties.class);  
    private static final String PROPERTY_NAME_PREFIX = "spike.twilio.";
    private static final String PROPERTIES_FILE = "spike.properties";
    private static SpikeProperties propertiesLoader;
    
    public static synchronized SpikeProperties getProperties() {
        if (propertiesLoader == null) {
            propertiesLoader = new SpikeProperties();
        } 
        return propertiesLoader;
    }
    
    public static String getProperty(String key) {
        return getProperties().properties.getProperty(PROPERTY_NAME_PREFIX + key);
    }
    
    public static String debug(String delimiter) {
        StringBuffer buffer = new StringBuffer(512);
        Properties properties = getProperties().properties;
        for (Object name : properties.keySet()) {
           buffer.append(name + ": ");
           String value = properties.getProperty(name.toString());
           int len = value.length();
           if (name.toString().endsWith("-key") || Character.isDigit(value.charAt(len - 1))) {
               for (int i = 0; i < len -5; i++) {
                   buffer.append('*');
               }
               value = value.substring(len - 5);
           }
           buffer.append(value);
           buffer.append(delimiter + "\n");
        } 
        return buffer.toString();
    }
    
    
    private Properties properties;

    private SpikeProperties() {
        try {
            properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException e) {
            LOG.warn("no properties loaded");
        }
    }
}

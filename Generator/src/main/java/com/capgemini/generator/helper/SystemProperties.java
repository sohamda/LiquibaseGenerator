package com.capgemini.generator.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;


public class SystemProperties {
    
    private static SystemProperties systemProperties = null;
    private Properties prop = new Properties();
    private String propertiesFilePath;
    
    
    protected SystemProperties() {
        super();
    }
    
    public static SystemProperties getInstance() {
        if(systemProperties == null) {
            systemProperties = new SystemProperties();
        }
        return systemProperties;
    }
    
    public String getProperty(String propName) {
        
        if(prop.isEmpty()) {
            try {
                InputStream input = new FileInputStream(getPropertiesFilePath());
                prop.load(input);
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        String propValue = prop.getProperty(propName);
        return propValue;
    }

    public void setPropertiesFilePath(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

    public String getPropertiesFilePath() {
        return propertiesFilePath;
    }
}

/*
 * ServerProperties.java
 *
 * Created on October 19, 2002, 9:26 AM
 */

package com.mettlesolutions.bankgame.util;

import com.mettlesolutions.bankgame.util.Constants;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
/**
 * This loads all the required server config/properties
 * @author  kmetla
 */
public class ServerProperties {

/***	moved all these values to a config file ***/

// Development settings
//  public final static String SERVER_ADDR = "http://192.168.1.102:81/";
//  public final static String DRIVE_USED = "C:" + File.separator;
//  public final static String INSTRCTNS_SERVER_ADDR = "http://141.153.195.118:81/";

//  public final static String INSTRCTNS_SERVER_ADDR = "http://128.122.99.115:88/";

// Professor's Server1 address
//  public final static String SERVER_ADDR = "http://128.122.99.115:88/";
//  public final static String DRIVE_USED = "C:" + File.separator;

// Professor's Server2 address (no space on C drive, so installed on D drive)
//  public final static String SERVER_ADDR = "http://128.122.99.22:88/";
//  public final static String DRIVE_USED = "D:" + File.separator;

// Professor's Server3 address (no space on C drive, so installed on D drive)
//  public final static String SERVER_ADDR = "http://128.122.99.30:88/";
//  public final static String DRIVE_USED = "D:" + File.separator;

    public final static String SERVER_ADDR = "http://localhost:8080/";
    public final static String DRIVE_USED = "/"; 
    public final static String INSTRCTNS_SERVER_ADDR = SERVER_ADDR; 
    public final static String CONFIG_LOCATION = "/usr/local/Cellar/tomcat6/6.0.33/libexec/conf/";
    public final static String REPORT_LOCATION = "/usr/local/Cellar/tomcat6/6.0.33/libexec/conf/";
    public final static String APP_HOME = "/usr/local/Cellar/tomcat6/6.0.33/libexec/webapps/bank-game/";

    public static Properties loadProperties(){
        Properties properties = new Properties();

        try {
            File serverDetailsFile = new File(Constants.SERVER_DETAILS_FILE);
            properties.load(new FileInputStream(serverDetailsFile));
        }
        catch(FileNotFoundException fnfe){
            System.out.println("properties file '" + Constants.SERVER_DETAILS_FILE + "' not found." );
            fnfe.printStackTrace();
            return null;
        }
        catch(IOException ioe){
            System.out.println("IO Exception while loading properties file '" + Constants.SERVER_DETAILS_FILE + "'." );
            ioe.printStackTrace();
            return null;
        }

        return properties;
    }

    public static String getServerAddress() {
        Properties properties = loadProperties();
        if(null == properties)
            return Constants.EMPTY_STRING;

        String SERVER_BEING_USED = properties.getProperty("SERVER_BEING_USED");
        if(SERVER_BEING_USED.equals("SERVER_ADDR_1"))
            return properties.getProperty("SERVER_ADDR_1");
	    else if(SERVER_BEING_USED.equals("SERVER_ADDR_2"))
            return properties.getProperty("SERVER_ADDR_2");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_3"))
            return properties.getProperty("SERVER_ADDR_3");
        else // if(SERVER_BEING_USED.equals("DVLPMNT_SERVER_ADDR"))
            return properties.getProperty("DVLPMNT_SERVER_ADDR");
    }

    public static String getDriveUsed() {
        Properties properties = loadProperties();
        if(null == properties)
            return Constants.EMPTY_STRING;

        String SERVER_BEING_USED = properties.getProperty("SERVER_BEING_USED");
        if(SERVER_BEING_USED.equals("SERVER_ADDR_1"))
            return properties.getProperty("DRIVE_USED_1");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_2"))
            return properties.getProperty("DRIVE_USED_2");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_3"))
            return properties.getProperty("DRIVE_USED_3");
        else // if(SERVER_BEING_USED.equals("DVLPMNT_SERVER_ADDR"))
            return properties.getProperty("DVLPMNT_DRIVE_USED");
    }

    public static String getInstrctnsServerAddr() {
        Properties properties = loadProperties();
        if(null == properties)
            return Constants.EMPTY_STRING;

        String SERVER_BEING_USED = properties.getProperty("SERVER_BEING_USED");
        if(SERVER_BEING_USED.equals("SERVER_ADDR_1"))
            return properties.getProperty("INSTRCTNS_SERVER_ADDR_1");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_2"))
            return properties.getProperty("INSTRCTNS_SERVER_ADDR_2");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_3"))
            return properties.getProperty("INSTRCTNS_SERVER_ADDR_3");
        else // if(SERVER_BEING_USED.equals("DVLPMNT_SERVER_ADDR"))
            return properties.getProperty("DVLPMNT_INSTRCTNS_SERVER_ADDR");
    }

    public static String getServerNum() {
        Properties properties = loadProperties();
        if(null == properties)
            return Constants.EMPTY_STRING;

        String SERVER_BEING_USED = properties.getProperty("SERVER_BEING_USED");
        if(SERVER_BEING_USED.equals("SERVER_ADDR_1"))
            return "Server1";
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_2"))
            return "Server2";
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_3"))
            return "Server3";
        else
            return "DevelopmentServer";
    }

    public static String getConfigLocation() {
        Properties properties = loadProperties();
        if(null == properties)
            return Constants.EMPTY_STRING;

        String SERVER_BEING_USED = properties.getProperty("SERVER_BEING_USED");
        if(SERVER_BEING_USED.equals("SERVER_ADDR_1"))
            return properties.getProperty("CONFIG_LOCATION_1");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_2"))
            return properties.getProperty("CONFIG_LOCATION_2");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_3"))
            return properties.getProperty("CONFIG_LOCATION_3");
        else // if(SERVER_BEING_USED.equals("DVLPMNT_SERVER_ADDR"))
            return properties.getProperty("DVLPMNT_CONFIG_LOCATION");
    }

    public static String getReportLocation() {
        Properties properties = loadProperties();
        if(null == properties)
            return Constants.EMPTY_STRING;

        String SERVER_BEING_USED = properties.getProperty("SERVER_BEING_USED");
        if(SERVER_BEING_USED.equals("SERVER_ADDR_1"))
            return properties.getProperty("REPORT_LOCATION_1");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_2"))
            return properties.getProperty("REPORT_LOCATION_2");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_3"))
            return properties.getProperty("REPORT_LOCATION_3");
        else // if(SERVER_BEING_USED.equals("DVLPMNT_SERVER_ADDR"))
            return properties.getProperty("DVLPMNT_REPORT_LOCATION");
    }

    public static String getAppHome() {
        Properties properties = loadProperties();
        if(null == properties)
            return Constants.EMPTY_STRING;

        String SERVER_BEING_USED = properties.getProperty("SERVER_BEING_USED");
        if(SERVER_BEING_USED.equals("SERVER_ADDR_1"))
            return properties.getProperty("APP_HOME_SERVER_1");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_2"))
            return properties.getProperty("APP_HOME_SERVER_2");
        else if(SERVER_BEING_USED.equals("SERVER_ADDR_3"))
            return properties.getProperty("APP_HOME_SERVER_3");
        else // if(SERVER_BEING_USED.equals("DVLPMNT_SERVER_ADDR"))
            return properties.getProperty("DVLPMNT_APP_HOME");
    }

}

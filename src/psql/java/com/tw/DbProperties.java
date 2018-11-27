package com.tw;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbProperties {

    private final static DbProperties webDbProps = new DbProperties();

    private Properties props;

    private DbProperties(){
        props = new Properties();
        try (InputStream is = new FileInputStream(System.getProperty("webAppDB"))) {
            props.load(is);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    public static String connectionUrl(){
        return webDbProps.props.getProperty("WEBAPP_DB_CONNECTION_STRING");
    }

    public static String user(){
        return webDbProps.props.getProperty("WEBAPP_DB_USER");
    }

    public static String password(){
        return webDbProps.props.getProperty("WEBAPP_DB_PASSWORD");
    }
}



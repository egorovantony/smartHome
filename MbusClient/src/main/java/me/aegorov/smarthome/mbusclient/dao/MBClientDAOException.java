package me.aegorov.smarthome.mbusclient.dao;

import me.aegorov.smarthome.mbusclient.app.MBClientException;

/**
 * Created by anton on 2/5/17.
 */
public class MBClientDAOException extends MBClientException {
    public MBClientDAOException(){}
    public MBClientDAOException(Throwable cause) {
        super(cause);
    }
    public MBClientDAOException(String message){
        super(message);
    }
    public MBClientDAOException(String message, Throwable cause){
        super(message, cause);
    }
}

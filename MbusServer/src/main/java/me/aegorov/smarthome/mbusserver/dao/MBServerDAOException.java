package me.aegorov.smarthome.mbusserver.dao;

import me.aegorov.smarthome.mbusserver.app.MBServerException;

/**
 * Created by anton on 2/14/17.
 */
public class MBServerDAOException extends MBServerException {
    public MBServerDAOException(){}
    public MBServerDAOException(Throwable cause) {
        super(cause);
    }
    public MBServerDAOException(String message){
        super(message);
    }
    public MBServerDAOException(String message, Throwable cause){
        super(message, cause);
    }
}

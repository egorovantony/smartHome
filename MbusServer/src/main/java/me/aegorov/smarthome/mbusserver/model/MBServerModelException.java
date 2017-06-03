package me.aegorov.smarthome.mbusserver.model;

import me.aegorov.smarthome.mbusserver.app.MBServerException;

/**
 * Created by anton on 2/14/17.
 */
public class MBServerModelException extends MBServerException {
    public MBServerModelException(){}
    public MBServerModelException(Throwable cause) {
        super(cause);
    }
    public MBServerModelException(String message){
        super(message);
    }
    public MBServerModelException(String message, Throwable cause){
        super(message, cause);
    }
}

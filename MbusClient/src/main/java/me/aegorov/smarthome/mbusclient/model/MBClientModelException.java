package me.aegorov.smarthome.mbusclient.model;

import me.aegorov.smarthome.mbusclient.app.MBClientException;

/**
 * Created by anton on 2/5/17.
 */
public class MBClientModelException extends MBClientException{
    public MBClientModelException(){}
    public MBClientModelException(Throwable cause) {
        super(cause);
    }
    public MBClientModelException(String message){
        super(message);
    }
    public MBClientModelException(String message, Throwable cause){
        super(message, cause);
    }
}

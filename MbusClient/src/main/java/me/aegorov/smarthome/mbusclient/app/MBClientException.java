package me.aegorov.smarthome.mbusclient.app;

/**
 * Created by anton on 2/5/17.
 */
public class MBClientException extends Exception {
    public MBClientException(){}
    public MBClientException(Throwable cause) {
        super(cause);
    }
    public MBClientException(String message){
        super(message);
    }
    public MBClientException(String message, Throwable cause){
        super(message, cause);
    }
}

package me.aegorov.smarthome.mbusserver.app;

/**
 * Created by anton on 1/19/17.
 */
public class MBServerException extends Exception{
    public MBServerException(){}
    public MBServerException(Throwable cause) {
        super(cause);
    }
    public MBServerException(String message){
        super(message);
    }
    public MBServerException(String message, Throwable cause){
        super(message, cause);
    }
}

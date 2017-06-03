package me.aegorov.smarthome.mbusclient.util;

/**
 * Created by anton on 1/23/17.
 */
public class Path {
    public static String getUserDir(){
        return System.getProperties().getProperty("user.dir");
    }
}

package me.aegorov.smarthome.mbusserver.model;

/**
 * Created by anton on 1/20/17.
 */
public enum TypeWriteSens {
    INPUT("IN"),
    OUT("OUT");

    private String shortName;
    public String getShortName(){
        return shortName;
    }
    private TypeWriteSens(String shortName){
        this.shortName = shortName;
    }
}

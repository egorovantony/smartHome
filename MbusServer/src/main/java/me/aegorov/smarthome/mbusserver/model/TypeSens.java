package me.aegorov.smarthome.mbusserver.model;

/**
 * Created by anton on 1/19/17.
 */
public enum TypeSens {
    DISCRETE("DI"),
    REGISTER("RE");
    private String shortName;
    private TypeSens(String shortName){
        this.shortName = shortName;
    }
    public String getShortName(){
        return this.shortName;
    }
}

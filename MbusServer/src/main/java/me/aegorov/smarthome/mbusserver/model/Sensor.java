package me.aegorov.smarthome.mbusserver.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by anton on 1/19/17.
 */
public class Sensor {

    private int ID;
    private int IDContr;
    private TypeSens type;
    private TypeWriteSens typeWrite;
    private int minValue;
    private int maxValue;
    private boolean working;
    private int ContrOrder;

    public Sensor(ResultSet rs) throws MBServerModelException{
        String strType;
        String strTypeWrite;

        try {
            this.ID         = rs.getInt("ID");
            this.IDContr    = rs.getInt("IDContr");
            this.minValue   = rs.getInt("MinValue");
            this.maxValue   = rs.getInt("MaxValue");
            this.working    = rs.getBoolean("Working");
            this.ContrOrder = rs.getInt("ContrOrder");
            strType         = rs.getString("Type");
            strTypeWrite    = rs.getString("TypeWrite");
        }catch (SQLException ex){
            throw new MBServerModelException("Ошибка при чтении данных датчика с ID=" + this.ID);
        }

        if (    this.ID == 0 ||
                this.IDContr == 0 ||
                this.ContrOrder == 0){
            throw new MBServerModelException("Не все поля заполнены корректно для датчика с ID=" + this.ID);
        }

        try{
            this.type = TypeSens.valueOf(strType);
        }catch(IllegalArgumentException ex){
            throw new MBServerModelException("Некорректное значение поля Type для датчика с ID=" + this.ID);
        }

        try{
            this.typeWrite = TypeWriteSens.valueOf(strTypeWrite);
        }catch(IllegalArgumentException ex){
            throw new MBServerModelException("Некорректное значение поля TypeWrite для датчика с ID=" + this.ID);
        }

    }

    public int getID() {
        return ID;
    }

    public int getIDContr() {
        return IDContr;
    }

    public TypeSens getType() {
        return type;
    }

    public TypeWriteSens getTypeWrite() {
        return typeWrite;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public boolean isWorking() {
        return working;
    }

    public int getContrOrder() {
        return ContrOrder;
    }

}


package me.aegorov.smarthome.mbusclient.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by anton on 1/31/17.
 */
public class SensorHistory {
    private int ID;
    private int IDSensor;
    private Date date;
    private boolean diValue;
    private int regValue;
    private boolean error;
    private String txtError = "";

    public SensorHistory(SensorState ss, Date date){
        this.IDSensor = ss.getIdSensor();
        this.date     = date;
        if (!ss.isErrRead()) {
            switch (ss.getTypeSens()) {
                case DISCRETE: {
                    this.diValue = ss.isDiscreteValue();
                    break;
                }
                case REGISTER: {
                    this.regValue = ss.getRegisterValue();
                    break;
                }
                default: {
                    break;
                }
            }
        }else{
            this.error = true;
            this.txtError = ss.getTxtError();
        }

    }
    public SensorHistory(ResultSet rs) throws MBClientModelException{
        try {
            this.ID       = rs.getInt("ID");
            this.IDSensor = rs.getInt("IDSensor");
            this.date     = rs.getTimestamp("dateTime");
            this.diValue  = rs.getBoolean("diValue");
            this.regValue = rs.getInt("regValue");
            this.error    = rs.getBoolean("error");
            this.txtError = rs.getString("txtError");
        }catch (SQLException ex){
            throw new MBClientModelException(ex);
        }

        if (this.ID == 0 || this.IDSensor == 0 || this.date == null){
            throw new MBClientModelException("Не все поля заполнены корректно для данных истории с ID=" + this.ID);
        }
    }
    public int getID() {
        return ID;
    }
    public int getIDSensor() {
        return IDSensor;
    }
    public Date getDate() {
        return date;
    }
    public boolean isDiValue() {
        return diValue;
    }
    public int getRegValue() {
        return regValue;
    }
    public boolean isError() {
        return error;
    }
    public String getTxtError() {
        return txtError;
    }
}

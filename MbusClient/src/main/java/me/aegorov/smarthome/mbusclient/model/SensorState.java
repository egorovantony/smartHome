package me.aegorov.smarthome.mbusclient.model;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.io.ModbusTransaction;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.text.SimpleDateFormat;

/**
 * Created by anton on 1/20/17.
 */
public class SensorState {
    private int IDSensor;
    private int IDContr;
    private int ContrOrder;
    private TypeSens type;
    private TypeWriteSens typeWrite;
    private boolean discreteValue;
    private int registerValue;
    private boolean read;
    private boolean errRead;
    private String txtError;

    private TCPMasterConnection connection;

    public SensorState(Sensor sensor, TCPMasterConnection connection){
        this.IDSensor = sensor.getID();
        this.IDContr = sensor.getIDContr();
        this.ContrOrder = sensor.getContrOrder();
        this.type = sensor.getType();
        this.typeWrite = sensor.getTypeWrite();

        this.connection = connection;
    }
    public void readValue(){
        ModbusTransaction transaction = new ModbusTCPTransaction(this.connection);
        this.read = true;
        try {
            switch (this.type) {
                case DISCRETE: {
                    switch (this.typeWrite) {
                        case INPUT: {
                            ReadInputDiscretesRequest reqID = new ReadInputDiscretesRequest(this.ContrOrder, 1);
                            transaction.setRequest(reqID);
                            transaction.execute();
                            ReadInputDiscretesResponse respID = (ReadInputDiscretesResponse) transaction.getResponse();
                            this.discreteValue = respID.getDiscreteStatus(0);
                            break;
                        }
                        case OUTPUT: {
                            ReadCoilsRequest reqOD = new ReadCoilsRequest(this.ContrOrder, 1);
                            transaction.setRequest(reqOD);
                            transaction.execute();
                            ReadCoilsResponse respOD = (ReadCoilsResponse) transaction.getResponse();
                            this.discreteValue = respOD.getCoilStatus(0);
                            break;
                        }
                    }
                    break;
                }
                case REGISTER: {
                    switch (this.typeWrite) {
                        case INPUT: {
                            ReadInputRegistersRequest reqIR = new ReadInputRegistersRequest(this.ContrOrder, 1);
                            transaction.setRequest(reqIR);
                            transaction.execute();
                            ReadInputRegistersResponse respIR = (ReadInputRegistersResponse) transaction.getResponse();
                            this.registerValue = respIR.getRegisterValue(0);
                            break;
                        }
                        case OUTPUT: {
                            ReadMultipleRegistersRequest reqOR = new ReadMultipleRegistersRequest(this.ContrOrder, 1);
                            transaction.setRequest(reqOR);
                            transaction.execute();
                            ReadMultipleRegistersResponse respOR = (ReadMultipleRegistersResponse) transaction.getResponse();
                            this.registerValue = respOR.getRegisterValue(0);
                            break;
                        }
                    }
                    break;
                }
            }
        }catch (Exception ex){
            this.errRead = true;
            this.txtError = ex.getMessage();
            ex.printStackTrace();
        }
    }
    public int getIdSensor(){
        return this.IDSensor;
    }
    public boolean isDiscreteValue(){
        return this.discreteValue;
    }
    public int getRegisterValue(){
        return this.registerValue;
    }
    public TypeSens getTypeSens(){
        return this.type;
    }
    public boolean isErrRead(){
        return this.errRead;
    }
    public String getTxtError(){
        return this.txtError;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        /*if (this.read){
            return dateFormat.format(this.date) + "; ID:" + this.ID + "; value:" +
                    (this.type == TypeSens.DISCRETE ? this.discreteValue:this.registerValue);
        }else if (errRead){
            return dateFormat.format(this.date) + "; ID:" + this.ID + "; error read";
        }*/
        return "";
    }
}

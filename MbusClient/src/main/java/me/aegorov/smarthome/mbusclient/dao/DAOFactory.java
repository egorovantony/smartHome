package me.aegorov.smarthome.mbusclient.dao;

import javax.sql.DataSource;

/**
 * Created by anton on 1/30/17.
 */
public class DAOFactory {
    private static DAOFactory instance;

    private TypeStorage typeStorage;
    private DataSource dataSource;

    public static DAOFactory getInstance(){
        return instance;
    }
    public DAOFactory(TypeStorage ts) throws MBClientDAOException{
        this.typeStorage = ts;
        if (this.typeStorage == TypeStorage.DB){
            throw new MBClientDAOException("Для типа хранилища DB необходимо указывать DataSource.");
        }
        instance = this;
    }
    public DAOFactory(TypeStorage ts, DataSource dataSource){
        this.typeStorage = ts;
        this.dataSource  = dataSource;
        instance = this;
    }
    public SensorDao getSensorDao() throws MBClientDAOException{
        switch (this.typeStorage){
            case FILE:{
                return new SensorDaoFile();
            }
            case DB:{
                return new SensorDaoDB(dataSource);
            }
            default:{
                throw new MBClientDAOException("Введенный тип хранилища не поддерживается.");
            }
        }
    }
    public SensorHistoryDao getSensorHistoryDao() throws MBClientDAOException{
        switch (this.typeStorage){
            case FILE:{
                return new SensorHistoryDaoFile();
            }
            case DB:{
                return new SensorHistoryDaoDB(dataSource);
            }
            default:{
                throw new MBClientDAOException("Введенный тип хранилища не поддерживается.");
            }
        }
    }
}

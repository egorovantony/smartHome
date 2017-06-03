package me.aegorov.smarthome.mbusserver.dao;

import javax.sql.DataSource;

/**
 * Created by anton on 2/14/17.
 */
public class DAOFactory {
    private static DAOFactory instance;

    private TypeStorage typeStorage;
    private DataSource dataSource;

    public static DAOFactory getInstance(){
        return instance;
    }
    public DAOFactory(TypeStorage ts) throws MBServerDAOException{
        this.typeStorage = ts;
        if (this.typeStorage == TypeStorage.DB){
            throw new MBServerDAOException("Для типа хранилища DB необходимо указывать DataSource.");
        }
        instance = this;
    }
    public DAOFactory(TypeStorage ts, DataSource dataSource){
        this.typeStorage = ts;
        this.dataSource  = dataSource;
        instance = this;
    }
    public SensorDao getSensorDao() throws MBServerDAOException{
        switch (this.typeStorage){
            case FILE:{
                return new SensorDaoFile();
            }
            case DB:{
                return new SensorDaoDB(dataSource);
            }
            default:{
                throw new MBServerDAOException("Введенный тип хранилища не поддерживается.");
            }
        }
    }
}
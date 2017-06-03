package me.aegorov.smarthome.mbusclient.dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;

/**
 * Created by anton on 1/30/17.
 */
public class DataSourceFactory {
    public static DataSource getDataSource(
            String strTypeDS,
            String URL,
            String user,
            String password) throws MBClientDAOException{

        TypeDS typeDS;

        try {
            typeDS = TypeDS.valueOf(strTypeDS);
        }catch (EnumConstantNotPresentException ex){
            throw new MBClientDAOException("Тип базы данных '" + strTypeDS + "' не поддерживается.");
        }

        switch (typeDS){
            case MYSQL:{
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                }catch (ClassNotFoundException ex){
                    throw new MBClientDAOException(ex);
                }
                MysqlDataSource mySqlDataSource = new MysqlDataSource();
                mySqlDataSource.setURL(URL);
                mySqlDataSource.setUser(user);
                mySqlDataSource.setPassword(password);
                return mySqlDataSource;
            }
            default:{
                throw new MBClientDAOException("Тип базы данных '" + typeDS + "' не поддерживается.");
            }
        }
    }
    public enum TypeDS{
        MYSQL
    }
}

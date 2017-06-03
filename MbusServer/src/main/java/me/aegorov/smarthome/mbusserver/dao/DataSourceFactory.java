package me.aegorov.smarthome.mbusserver.dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;

/**
 * Created by anton on 2/14/17.
 */
public class DataSourceFactory {
    public static DataSource getDataSource(
            String strTypeDS,
            String URL,
            String user,
            String password) throws MBServerDAOException{

        TypeDS typeDS;

        try {
            typeDS = TypeDS.valueOf(strTypeDS);
        }catch (EnumConstantNotPresentException ex){
            throw new MBServerDAOException("Тип базы данных '" + strTypeDS + "' не поддерживается.");
        }

        switch (typeDS){
            case MYSQL:{
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                }catch (ClassNotFoundException ex){
                    throw new MBServerDAOException(ex);
                }
                MysqlDataSource mySqlDataSource = new MysqlDataSource();
                mySqlDataSource.setURL(URL);
                mySqlDataSource.setUser(user);
                mySqlDataSource.setPassword(password);
                return mySqlDataSource;
            }
            default:{
                throw new MBServerDAOException("Тип базы данных '" + typeDS + "' не поддерживается.");
            }
        }
    }
    public enum TypeDS{
        MYSQL
    }
}

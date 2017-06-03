package me.aegorov.smarthome.mbusserver.dao;

import me.aegorov.smarthome.mbusserver.model.MBServerModelException;
import me.aegorov.smarthome.mbusserver.model.Sensor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 2/14/17.
 */
public class SensorDaoDB implements SensorDao {

    private DataSource dataSource;

    public SensorDaoDB(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public List<Sensor> findAll() throws MBServerDAOException{
        List<Sensor> result = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SqlConst.SQL_FIND_ALL);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Sensor sensor = null;

                try {
                    sensor = new Sensor(rs);
                }catch (MBServerModelException ex){
                    ex.printStackTrace();
                }

                if (sensor != null){
                    result.add(sensor);
                }
            }
        }catch (SQLException ex){
            throw new MBServerDAOException(ex);
        }

        return result;
    }
    @Override
    public List<Sensor> findByIDContr(int idContr) throws MBServerDAOException {
        List<Sensor> result = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SqlConst.SQL_FIND_BY_IDCONTR);
            statement.setInt(1,idContr);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Sensor sensor = null;

                try {
                    sensor = new Sensor(rs);
                }catch (MBServerModelException ex){
                    ex.printStackTrace();
                }

                if (sensor != null){
                    result.add(sensor);
                }
            }
        }catch (SQLException ex){
            throw new MBServerDAOException(ex);
        }

        return result;
    }

    @Override
    public void insert(Sensor sensor) {

    }

    @Override
    public void update(Sensor sensor) {

    }

    @Override
    public void delete(Sensor sensor) {

    }

    private class SqlConst{
        public static final String SQL_FIND_ALL = "select * from sensor";
        public static final String SQL_FIND_BY_IDCONTR = "select * from sensor where IDContr = ?";
        public static final String SQL_INSERT =
                "insert into sensor (IDContr, Type, TypeWrite, MinValue, MaxValue, Working, ContrOrder) " +
                        "values (?,?,?,?,?,?,?)";
        public static final String SQL_UPDATE =
                "update sensor set " +
                        "IDContr = ?, " +
                        "Type = ?, " +
                        "TypeWrite = ?, " +
                        "MinValue = ?, " +
                        "MaxValue = ?, " +
                        "Working = ?, " +
                        "ContrOrder = ?)" +
                        "where ID = ?";
        public static final String SQL_DELETE = "delete from sensor where ID = ?";
    }


}

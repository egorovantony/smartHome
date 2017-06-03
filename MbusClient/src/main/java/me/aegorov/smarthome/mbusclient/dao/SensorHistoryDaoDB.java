package me.aegorov.smarthome.mbusclient.dao;

import me.aegorov.smarthome.mbusclient.model.MBClientModelException;
import me.aegorov.smarthome.mbusclient.model.SensorHistory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by anton on 1/30/17.
 */
public class SensorHistoryDaoDB implements SensorHistoryDao{
    private DataSource dataSource;

    public SensorHistoryDaoDB(DataSource ds){
        this.dataSource = ds;
    }

    @Override
    public List<SensorHistory> findByRangeDate(Date dateFr, Date dateTo) throws MBClientDAOException {
        List<SensorHistory> result = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SqlTmp.FIND_BY_RANGE_DATE);
            //String dateTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
            Timestamp tstFr = new Timestamp(dateFr.getTime());
            Timestamp tstTo = new Timestamp(dateTo.getTime());
            statement.setTimestamp(1,tstFr);
            statement.setTimestamp(2,tstTo);

            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                SensorHistory sensorHistory = null;

                try {
                    sensorHistory = new SensorHistory(rs);
                }catch (MBClientModelException ex){
                    ex.printStackTrace();
                }

                if (sensorHistory != null){
                    result.add(sensorHistory);
                }
            }
        }catch (SQLException ex){
            throw new MBClientDAOException(ex);
        }

        return result;
    }

    @Override
    public void insertSingle(SensorHistory sensorHistory) {

    }

    @Override
    public void insertMultiply(List<SensorHistory> sensorsHistory) throws MBClientDAOException {
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SqlTmp.INSERT);

            for (SensorHistory sh:sensorsHistory) {
                statement.setInt(1, sh.getIDSensor());
                Timestamp tst = new Timestamp(sh.getDate().getTime());
                statement.setTimestamp(2, tst);
                statement.setBoolean(3,sh.isDiValue());
                statement.setInt(4, sh.getRegValue());
                statement.setBoolean(5, sh.isError());
                statement.setString(6, sh.getTxtError());

                statement.addBatch();
            }
            statement.executeBatch();
        }catch (SQLException ex){
            throw new MBClientDAOException(ex);
        }
    }

    private class SqlTmp{
        public static final String FIND_BY_RANGE_DATE = "select * from sensorHistory where dateTime between ? and ?";
        public static final String INSERT =
                "insert into sensorHistory (IDSensor, dateTime, diValue, regValue, error, txtError) " +
                        "values (?,?,?,?,?,?)";
    }
}

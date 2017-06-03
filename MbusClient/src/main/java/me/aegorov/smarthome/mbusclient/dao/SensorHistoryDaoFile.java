package me.aegorov.smarthome.mbusclient.dao;

import me.aegorov.smarthome.mbusclient.app.Settings;
import me.aegorov.smarthome.mbusclient.model.SensorHistory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by anton on 1/30/17.
 */
public class SensorHistoryDaoFile implements SensorHistoryDao {
    public SensorHistoryDaoFile() {
        super();
    }

    @Override
    public List<SensorHistory> findByRangeDate(Date dateFr, Date dateTo) throws MBClientDAOException {
        return null;
    }

    @Override
    public void insertSingle(SensorHistory sensorHistory) {
        String fileName = Settings.getInstance().getSensHistFilePath(new Date());
        try(FileWriter fw = new FileWriter(fileName, true)){
            fw.write(sensorHistory.toString() + "\n");
        }catch (IOException IOex){
            //Nothing
        }
    }
    @Override
    public void insertMultiply(List<SensorHistory> sensorHistories) {
        String fileName = Settings.getInstance().getSensHistFilePath(new Date());
        try(FileWriter fw = new FileWriter(fileName, true)){
            for (SensorHistory sensor : sensorHistories) {
                fw.write(sensorHistories.toString() + "\n");
            }
        }catch (IOException IOex){
            //Nothing
        }
    }

}
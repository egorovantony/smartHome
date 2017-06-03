package me.aegorov.smarthome.mbusclient.dao;

import me.aegorov.smarthome.mbusclient.model.SensorHistory;

import java.util.Date;
import java.util.List;

/**
 * Created by anton on 1/30/17.
 */
public interface SensorHistoryDao {
    public List<SensorHistory> findByRangeDate(Date dateFr, Date dateTo) throws MBClientDAOException;
    public void insertSingle(SensorHistory sensorHistory) throws MBClientDAOException;
    public void insertMultiply(List<SensorHistory> sensorHistory) throws MBClientDAOException;
}
package me.aegorov.smarthome.mbusserver.dao;

import me.aegorov.smarthome.mbusserver.model.Sensor;

import java.util.List;

/**
 * Created by anton on 2/14/17.
 */
public interface SensorDao {
    public List<Sensor> findAll() throws MBServerDAOException;
    public List<Sensor> findByIDContr(int idContr) throws MBServerDAOException;
    public void insert(Sensor sensor);
    public void update(Sensor sensor);
    public void delete(Sensor sensor);
}

package me.aegorov.smarthome.mbusclient.dao;

import me.aegorov.smarthome.mbusclient.model.Sensor;

import java.util.List;

/**
 * Created by anton on 1/30/17.
 */
public interface SensorDao {
    public List<Sensor> findAll() throws MBClientDAOException;
    public List<Sensor> findByIDContr(int idContr) throws MBClientDAOException;
    public void insert(Sensor sensor);
    public void update(Sensor sensor);
    public void delete(Sensor sensor);
}

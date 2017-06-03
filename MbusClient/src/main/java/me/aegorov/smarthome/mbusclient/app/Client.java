package me.aegorov.smarthome.mbusclient.app;

import me.aegorov.smarthome.mbusclient.dao.DAOFactory;
import me.aegorov.smarthome.mbusclient.dao.SensorDao;
import me.aegorov.smarthome.mbusclient.dao.SensorHistoryDao;
import me.aegorov.smarthome.mbusclient.model.Sensor;
import me.aegorov.smarthome.mbusclient.model.SensorHistory;
import me.aegorov.smarthome.mbusclient.model.SensorState;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by anton on 1/18/17.
 */
public class Client {
    public void execute() throws MBClientException{
        Settings settings = Settings.getInstance();
        DAOFactory daoFactory = DAOFactory.getInstance();
        try {
            InetAddress slaveAddr = InetAddress.getByName(settings.getAddressServer());
            TCPMasterConnection con = new TCPMasterConnection(slaveAddr);
            con.setPort(settings.getPortServer());

            SensorDao sensorDao = daoFactory.getSensorDao();
            List<Sensor> sensors = sensorDao.findAll();

            SensorHistoryDao sensHDao = daoFactory.getSensorHistoryDao();

            while(true){
                List<SensorHistory> arrSensorHistory = new ArrayList<>();

                Date currDate = new Date();
                for (Sensor sensor : sensors) {
                    SensorState sensorState = new SensorState(sensor, con);
                    sensorState.readValue();

                    arrSensorHistory.add(new SensorHistory(sensorState, currDate));
                }

                sensHDao.insertMultiply(arrSensorHistory);

                Thread.sleep(settings.getRetryTime());
            }

        }catch (Exception ex){
            throw new MBClientException(ex);
        }
    }
}

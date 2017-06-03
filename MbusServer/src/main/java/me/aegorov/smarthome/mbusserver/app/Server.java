package me.aegorov.smarthome.mbusserver.app;

import me.aegorov.smarthome.mbusserver.dao.DAOFactory;
import me.aegorov.smarthome.mbusserver.model.Sensor;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.*;

import java.net.InetAddress;
import java.util.List;
import java.util.Random;

/**
 * Created by anton on 1/18/17.
 */
public class Server {
    public void execute(){
        Settings settings = Settings.getInstance();
        DAOFactory daoFactory = DAOFactory.getInstance();

        ModbusTCPListener listener = null;
        SimpleProcessImage spi = null;

        try {
            //1. prepare a process image
            spi = new SimpleProcessImage();

            Random randBoolean  = new Random();
            Random randInt      = new Random();

            List<Sensor> sensors = daoFactory.getSensorDao().findAll();
            for (Sensor sensor : sensors) {
                switch (sensor.getType()){
                    case DISCRETE:{
                        switch (sensor.getTypeWrite()){
                            case INPUT:{
                                spi.addDigitalIn(new SimpleDigitalIn(randBoolean.nextBoolean()));
                                break;
                            }
                            case OUT:{
                                spi.addDigitalOut(new SimpleDigitalOut(randBoolean.nextBoolean()));
                                break;
                            }
                        }
                        break;
                    }
                    case REGISTER:{
                        switch (sensor.getTypeWrite()){
                            case INPUT:{
                                spi.addInputRegister(new SimpleInputRegister(randInt.nextInt(250)));
                                break;
                            }
                            case OUT:{
                                spi.addRegister(new SimpleRegister(randInt.nextInt(250)));
                                break;
                            }
                        }
                        break;
                    }
                }
            }

            //2. create the coupler holding the image
            ModbusCoupler.getReference().setProcessImage(spi);
            ModbusCoupler.getReference().setMaster(false);
            ModbusCoupler.getReference().setUnitID(15);

            //3. create a listener with 3 threads in pool
            InetAddress address = InetAddress.getByName(settings.getAddressServer());
            listener = new ModbusTCPListener(1);
            listener.setAddress(address);
            listener.setPort(settings.getPortServer());
            listener.start();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}

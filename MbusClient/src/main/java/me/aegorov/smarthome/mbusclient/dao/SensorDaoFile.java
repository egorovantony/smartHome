package me.aegorov.smarthome.mbusclient.dao;

import me.aegorov.smarthome.mbusclient.app.Settings;
import me.aegorov.smarthome.mbusclient.model.Sensor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 1/20/17.
 */
public class SensorDaoFile implements SensorDao {
    public SensorDaoFile() {
        super();
    }

    @Override
    public List<Sensor> findAll(){
        ArrayList<Sensor> arrSensors = new ArrayList<>();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(Settings.getInstance().getSensFilePath());

            Node rootNode = document.getDocumentElement();

            NodeList listNodes = rootNode.getChildNodes();
            for (int i = 0; i < listNodes.getLength(); i++) {
                Node sensorNode = listNodes.item(i);
                if (sensorNode.getNodeType() == rootNode.ELEMENT_NODE){
                    NodeList sensAttrs = sensorNode.getChildNodes();
                    /*Sensor sensor = new Sensor();*/
                    for (int j = 0; j < sensAttrs.getLength(); j++) {
                        Node sensAttr = sensAttrs.item(j);
                        if (sensAttr.getNodeType() == rootNode.ELEMENT_NODE){

                            /*switch (sensAttr.getNodeName()){
                                case "ID":{
                                    sensor.ID = sensAttr.getTextContent();
                                    break;
                                }
                                case "IDContr":{
                                    sensor.IDContr = Integer.parseInt(sensAttr.getTextContent());
                                    break;
                                }
                                case "type":{
                                    sensor.type = sensAttr.getTextContent().equals(TypeSens.DISCRETE.getShortName()) ? TypeSens.DISCRETE : TypeSens.REGISTER;
                                    break;
                                }
                                case "typeWrite":{
                                    sensor.typeWrite = sensAttr.getTextContent().equals(TypeWriteSens.INPUT.getShortName()) ? TypeWriteSens.INPUT : TypeWriteSens.OUT;
                                    break;
                                }
                                case "minValue":{
                                    sensor.minValue = Integer.parseInt(sensAttr.getTextContent());
                                    break;
                                }
                                case "maxValue":{
                                    sensor.maxValue = Integer.parseInt(sensAttr.getTextContent());
                                    break;
                                }
                            }*/
                        }

                    }
                    /*arrSensors.add(sensor);*/
                }
            }

        }catch (Exception ex){
            System.out.println();
        }

        if (arrSensors.isEmpty()){

        }

        return arrSensors;
    }

    @Override
    public List<Sensor> findByIDContr(int idContr) throws MBClientDAOException {
        return null;
    }

    @Override
    public void insert(Sensor sensor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Sensor sensor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Sensor sensor) {
        throw new UnsupportedOperationException();
    }
}

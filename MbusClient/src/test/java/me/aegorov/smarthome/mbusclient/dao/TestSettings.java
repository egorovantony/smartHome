package me.aegorov.smarthome.mbusclient.dao;

import me.aegorov.smarthome.mbusclient.app.MBClientException;
import me.aegorov.smarthome.mbusclient.app.Settings;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by anton on 2/10/17.
 */
public class TestSettings {
    private static final String workDir = "src/test/resources/Settings";
    @Test
    public void testCorrectSettingValuesFromFile(){
        try {
            Settings set = new Settings(workDir);
            set.read();

            //Check Address Server
            assertEquals(set.getAddressServer(), "192.168.1.35");
            assertEquals(set.getPortServer(), 7777);
            assertEquals(set.getRetryTime(), 105);
            assertEquals(set.getTypeStorage(), TypeStorage.FILE);
            assertEquals(set.getDbType(), "PostgreSQL");
            assertEquals(set.getDbAddress(), "192.168.1.105");
            assertEquals(set.getDbUser(), "ivan");
            assertEquals(set.getDbPassword(), "25");
        }catch (MBClientException ex){
            fail(ex.getMessage());
        }
    }

    @Test
    public void testThrowMBClientException(){
        for (int i = 1; i < 9; i++) {
            try {
                changeFileErrorSettings(workDir, i);
                Settings set = new Settings(workDir + "/err");
                set.read();
                fail("MBClientException did'n throw");
            } catch (MBClientException ex) {}
        }
    }

    private void changeFileErrorSettings(String workDir, int changeCode){
        try{
            String filePathSource =  workDir + "/conf/settings.xml";
            String filePathTarget =  workDir + "/err/conf/settings.xml";
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(filePathSource);

            Node rootNode = document.getDocumentElement();

            NodeList listNodes = rootNode.getChildNodes();
            for (int i = 0; i < listNodes.getLength(); i++) {
                Node setting = listNodes.item(i);
                if (setting.getNodeType() == rootNode.ELEMENT_NODE && setting.getNodeName().equals("setting")) {
                    NodeList settingChilds = setting.getChildNodes();
                    String name = null;
                    String value = null;
                    for (int j = 0; j < settingChilds.getLength(); j++) {
                        Node settingChild = settingChilds.item(j);
                        switch (settingChild.getNodeName()) {
                            case "name": {
                                name = settingChild.getTextContent();
                                break;
                            }
                            case "value": {
                                switch (changeCode){
                                    case 1:{
                                        if (name.equals("addressServer")) {
                                            settingChild.setTextContent("");
                                        }
                                    }
                                    case 2:{
                                        if (name.equals("retryTime")) {
                                            settingChild.setTextContent("");
                                        }
                                    }
                                    case 3:{
                                        if (name.equals("typeStorage")) {
                                            settingChild.setTextContent("");
                                        }
                                    }
                                    case 4:{
                                        if (name.equals("portServer")) {
                                            settingChild.setTextContent("");
                                        }
                                    }
                                    case 5:{
                                        if (name.equals("typeStorage")) {
                                            settingChild.setTextContent("DB");
                                        }
                                        if (name.equals("dbType")) {
                                            settingChild.setTextContent("");
                                        }
                                    }
                                    case 6:{
                                        if (name.equals("typeStorage")) {
                                            settingChild.setTextContent("DB");
                                        }
                                        if (name.equals("dbAddress")) {
                                            settingChild.setTextContent("");
                                        }
                                    }
                                    case 7:{
                                        if (name.equals("typeStorage")) {
                                            settingChild.setTextContent("DB");
                                        }
                                        if (name.equals("dbUser")) {
                                            settingChild.setTextContent("");
                                        }
                                    }
                                    case 8:{
                                        if (name.equals("typeStorage")) {
                                            settingChild.setTextContent("DB");
                                        }
                                        if (name.equals("dbPassword")) {
                                            settingChild.setTextContent("");
                                        }
                                    }
                                }
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePathTarget));
            transformer.transform(source, result);
        }catch (Exception ex){
            fail(ex.getMessage());
        }
    }


}

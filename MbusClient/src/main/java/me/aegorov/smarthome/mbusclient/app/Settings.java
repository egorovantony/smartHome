package me.aegorov.smarthome.mbusclient.app;

/**
 * Created by anton on 1/18/17.
 */

import me.aegorov.smarthome.mbusclient.dao.TypeStorage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by anton on 1/18/17.
 */
public class Settings {
    private static Settings instance = null;

    private String addressServer;
    private int portServer;
    private int retryTime;
    private TypeStorage typeStorage;
    private String dbType;
    private String dbAddress;
    private String dbUser;
    private String dbPassword;

    private String workDir;

    public static Settings getInstance(){
        return instance;
    }

    public Settings(String workDir){
        this.workDir = workDir;
        instance = this;
    }

    public String getAddressServer() {
        return addressServer;
    }

    public int getPortServer() {
        return portServer;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public TypeStorage getTypeStorage() {
        return typeStorage;
    }

    public String getDbType() {
        return dbType;
    }

    public String getDbAddress() {
        return dbAddress;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getWorkDir(){
        return workDir;
    }

    public void read() throws MBClientException {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(ConfigureEnvironment.getSetFilePath(workDir));

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
                                value = settingChild.getTextContent();
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }

                    if (name != null && value != null) {
                        fillParam(name, value);
                    }
                }
            }
        }catch (MBClientException ex) {
            throw new MBClientException(ex);
        }catch (Exception ex){
            throw new MBClientException("Ошибка чтения файла настроек.", ex);
        }

        //Проверка значений
        checkParam();

    }

    private void fillParam(String name, String value) throws MBClientException{
        switch (name){
            case "addressServer":{
                this.addressServer = value;
                break;
            }
            case "portServer":{
                try {
                    this.portServer = Integer.parseInt(value);
                }catch (NumberFormatException ex){
                    throw new MBClientException("Поле portServer в настроечном файле должно быть числовым.");
                }
                break;
            }
            case "retryTime":{
                try {
                    this.retryTime = Integer.parseInt(value);
                }catch (NumberFormatException ex){
                    throw new MBClientException("Поле retryTime в настроечном файле должно быть числовым.");
                }
                break;
            }
            case "typeStorage":{
                try {
                    this.typeStorage = TypeStorage.valueOf(value);
                }catch(IllegalArgumentException ex){
                    throw new MBClientException("Параметр typeStorage в настроечном файле имеет некорректный формат.");
                }
                break;
            }
            case "dbType":{
                this.dbType = value;
                break;
            }
            case "dbAddress":{
                this.dbAddress = value;
                break;
            }
            case "dbUser":{
                this.dbUser = value;
                break;
            }
            case "dbPassword":{
                this.dbPassword = value;
                break;
            }
            default:{
                throw new MBClientException("Параметр " + name + " не должен присутствовать в настроечном файле.");
            }
        }
    }

    private void checkParam() throws MBClientException{
        if (this.addressServer.isEmpty() || this.retryTime == 0 || this.typeStorage == null || this.portServer == 0 ||
                (this.typeStorage == TypeStorage.DB &&
                        (this.dbType.isEmpty() ||
                                this.dbAddress.isEmpty() ||
                                this.dbUser.isEmpty() ||
                                this.dbPassword.isEmpty()
                        )
                )
                ){
            throw new MBClientException("Файл настроек имеет некорректную структуру или не все поля заполнены   ");
        }
    }

}


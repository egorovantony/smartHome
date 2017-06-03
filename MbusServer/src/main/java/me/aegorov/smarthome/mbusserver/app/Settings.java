package me.aegorov.smarthome.mbusserver.app;

/**
 * Created by anton on 1/18/17.
 */

import me.aegorov.smarthome.mbusserver.dao.TypeStorage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by anton on 1/18/17.
 */
public class Settings {
    public static final String DIR_LOG = "log";
    public static final String DIR_DATA = "data";
    public static final String DIR_SET = "conf";

    public static final String FILE_SETTINGS = "settings.xml";
    public static final String FILE_SENSORS = "sensors.xml";
    public static final String FILE_SENS_HISTORY_PREF = "sensor_history.txt";
    public static final String FILE_ERROR = "error.txt";

    private static Settings instance = null;

    private String addressServer;
    private int portServer;
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

    public String getSetFilePath(){
        return this.workDir + "/" + DIR_SET + "/" + FILE_SETTINGS;
    }

    public String getSensFilePath(){
        return this.workDir + "/" + DIR_SET +  "/" + FILE_SENSORS;
    }

    public String getFileError(){
        return this.workDir + "/" + DIR_LOG + "/" + FILE_ERROR;
    }

    public void read() throws MBServerException {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(this.getSetFilePath());

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
        }catch (MBServerException ex) {
            throw new MBServerException(ex);
        }catch (Exception ex){
            throw new MBServerException("Ошибка чтения файла настроек.", ex);
        }

        //Проверка значений
        checkParam();

    }

    private void fillParam(String name, String value) throws MBServerException{
        switch (name){
            case "addressServer":{
                this.addressServer = value;
                break;
            }
            case "portServer":{
                try {
                    this.portServer = Integer.parseInt(value);
                }catch (NumberFormatException ex){
                    throw new MBServerException("Поле portServer в настроечном файле должно быть числовым.");
                }
                break;
            }
            case "typeStorage":{
                try {
                    this.typeStorage = TypeStorage.valueOf(value);
                }catch(IllegalArgumentException ex){
                    throw new MBServerException("Параметр typeStorage в настроечном файле имеет некорректный формат.");
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
                throw new MBServerException("Параметр " + name + " не должен присутствовать в настроечном файле.");
            }
        }
    }

    private void checkParam() throws MBServerException{
        if (this.addressServer.isEmpty() || this.typeStorage == null || this.portServer == 0 ||
                (this.typeStorage == TypeStorage.DB &&
                        (this.dbType.isEmpty() ||
                                this.dbAddress.isEmpty() ||
                                this.dbUser.isEmpty() ||
                                this.dbPassword.isEmpty()
                        )
                )
                ){
            throw new MBServerException("Файл настроек имеет некорректную структуру или не все поля заполнены   ");
        }
    }

}


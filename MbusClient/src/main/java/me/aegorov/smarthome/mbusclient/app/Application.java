package me.aegorov.smarthome.mbusclient.app;

import me.aegorov.smarthome.mbusclient.dao.DAOFactory;
import me.aegorov.smarthome.mbusclient.dao.DataSourceFactory;
import me.aegorov.smarthome.mbusclient.dao.TypeStorage;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by anton on 2/4/17.
 */
public class Application {
    private static String typeRun = "";
    private static String workDir = "";

    public static void main(String[] args) {
        parseInputParams(args);

        switch (typeRun) {
            case "confInit": {
                confInit();
                break;
            }
            case "confDB": {
                confDB();
                break;
            }
            case "run": {
                run();
            }
            default: {
                System.err.printf("Параметры для запуска приложения введены некорректно");
                System.exit(1);
            }
        }
    }

    private static void parseInputParams(String[] args){
        if (args.length < 2){
            System.err.println("Параметры для запуска приложения не введены");
            System.exit(1);
        }
        typeRun = args[0];
        workDir = args[1];
    }
    private static void confInit(){
        try {
            ConfigureEnvironment confEnv = new ConfigureEnvironment(workDir);
            confEnv.exec();
        }catch (MBClientException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
    private static void confDB(){
        readSet();
        Settings settings = Settings.getInstance();
        try {
            ConfigureDB confDB = new ConfigureDB(
                    workDir,
                    settings.getDbAddress(),
                    settings.getDbUser(),
                    settings.getDbPassword()
            );
            confDB.exec();
        }catch (MBClientException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
    private static void run(){
        try {
            readSet();
            initApp();
            Client client = new Client();
            client.execute();
        }catch (MBClientException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
    private static void readSet(){
        try {
            Settings settings = new Settings(workDir);
            settings.read();
        }catch (MBClientException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
    private static void initApp(){
        Settings settings = Settings.getInstance();

        //Turn off standard stream
        try {
            System.in.close();
        }catch (Exception ex){

        }
        System.out.close();

        //Redirect error stream
        try{
            PrintStream printStream = new PrintStream(new FileOutputStream(ConfigureEnvironment.getFileError(workDir)));
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
            System.exit(1);
        }

        // Init DAOFactory
        try {
            if (settings.getTypeStorage() == TypeStorage.DB) {
                //Настройка доступа к БД
                DataSource dataSource = null;
                dataSource = DataSourceFactory.getDataSource(
                        settings.getDbType(),
                        settings.getDbAddress(),
                        settings.getDbUser(),
                        settings.getDbPassword());
                new DAOFactory(TypeStorage.DB, dataSource);
            } else {
                new DAOFactory(TypeStorage.DB);
            }
        }catch (MBClientException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
}

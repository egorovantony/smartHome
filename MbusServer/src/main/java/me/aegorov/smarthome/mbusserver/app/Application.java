package me.aegorov.smarthome.mbusserver.app;

import me.aegorov.smarthome.mbusserver.dao.DAOFactory;
import me.aegorov.smarthome.mbusserver.dao.DataSourceFactory;
import me.aegorov.smarthome.mbusserver.dao.TypeStorage;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by anton on 2/14/17.
 */
public class Application {
    private static String workDir = "";

    public static void main(String[] args) {
        if (args.length < 1){
            System.err.println("Параметры для запуска приложения не введены");
            System.exit(1);
        }
        workDir = args[0];
        initApp();
        Server server = new Server();
        server.execute();
    }

    private static void initApp(){
        //Turn off standard stream
        try {
            System.in.close();
        }catch (Exception ex){

        }
        System.out.close();

        //Getting settings
        Settings settings = null;
        try {
            settings = new Settings(workDir);
            settings.read();
        }catch (MBServerException ex){
            ex.printStackTrace();
            System.exit(1);
        }

        //Redirect error stream
        try{
            PrintStream printStream = new PrintStream(new FileOutputStream(settings.getFileError()));
            System.setErr(printStream);
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
                new DAOFactory(TypeStorage.FILE);
            }
        }catch (MBServerException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
}

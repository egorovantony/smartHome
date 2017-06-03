package me.aegorov.smarthome.mbusclient.app;

import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * Created by anton on 2/14/17.
 */
public class ConfigureEnvironment {
    public static final String DIR_LOG = "log";
    public static final String DIR_DATA = "data";
    public static final String DIR_SET = "conf";
    public static final String DIR_SQL = "sql";

    public static final String FILE_SETTINGS = "xml";
    public static final String FILE_SENSORS = "sensors.xml";
    public static final String FILE_SENS_HISTORY_PREF = "sensor_history.txt";
    public static final String FILE_ERROR = "error.txt";
    public static final String FILE_DDL_CREATE = "ddl_create.txt";
    
    private String workDir = "";
    
    public ConfigureEnvironment(String workDir){
        this.workDir = workDir;
    }
    public void exec() throws MBClientException{
        String sFile, tFile, dir;
        try {
            dir = this.workDir + "/" + DIR_LOG;
            System.out.println("Create: " + dir);
            Files.createDirectory(Paths.get(dir));

            dir = this.workDir + "/" + DIR_SET;
            System.out.println("Create: " + dir);
            Files.createDirectory(Paths.get(dir));

            dir = this.workDir + "/" + DIR_DATA;
            Files.createDirectory(Paths.get(dir));

            dir = this.workDir + "/" + DIR_SQL;
            Files.createDirectory(Paths.get(dir));

            sFile = "/" + DIR_SET + "/" + FILE_SETTINGS;
            tFile = this.workDir + "/" + DIR_SET + "/" + FILE_SETTINGS;
            System.out.println("Create: " + tFile);
            copyFile(sFile, tFile);

            sFile = "/" + DIR_SET + "/" + FILE_SENSORS;
            tFile = this.workDir + "/" + DIR_SET + "/" + FILE_SENSORS;
            System.out.println("Create: " + tFile);
            copyFile(sFile, tFile);

            sFile = "/" + DIR_SQL + "/" + FILE_DDL_CREATE;
            tFile = this.workDir + "/" + DIR_SQL + "/" + FILE_DDL_CREATE;
            System.out.println("Create: " + tFile);
            copyFile(sFile, tFile);

        }catch (IOException ex){
            throw new MBClientException(ex);
        }
    }
    public static String getSetFilePath(String workDir){
        return workDir + "/" + DIR_SET + "/" + FILE_SETTINGS;
    }
    public static String getSensFilePath(String workDir){
        return workDir + "/" + DIR_SET +  "/" + FILE_SENSORS;
    }
    public static String getFileError(String workDir){
        return workDir + "/" + DIR_LOG + "/" + FILE_ERROR;
    }
    public static String getSensHistFilePath(String workDir, Date date){
        String fileName = workDir + "/" + DIR_DATA;
        String pathSensHistory[] = FILE_SENS_HISTORY_PREF.split(Pattern.quote("."));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        fileName += "/";

        if (pathSensHistory.length == 2) {
            fileName += pathSensHistory[0] + "_" + dateFormat.format(date) + "." + pathSensHistory[1];
        }else {
            fileName += FILE_SENS_HISTORY_PREF;
        }
        return fileName;
    }
    private void copyFile(String source, String target) throws MBClientException{
        InputStream in = getClass().getResourceAsStream(source);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        try(FileOutputStream fos = new FileOutputStream(target))
        {
            String line = br.readLine();
            while(line != null){
                line += "\n";
                fos.write(line.getBytes());
                fos.flush();
                line = br.readLine();
            }
        }catch (IOException ex){
            throw new MBClientException(ex);
        }
    }
}

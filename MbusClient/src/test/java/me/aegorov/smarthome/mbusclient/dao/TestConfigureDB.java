package me.aegorov.smarthome.mbusclient.dao;

import me.aegorov.smarthome.mbusclient.app.MBClientException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.fail;

/**
 * Created by anton on 2/8/17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConfigureDB {
    private static final String DB_DRIVER= "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://192.168.1.31:3306";
    private static final String DB_USER = "anton";
    private static final String DB_PASS = "10";
    private static final String TABLE_SCHEMA = "modbus_test";
    private static final String WORK_DIR = "src/test/resources/DB";

    private static Connection conn;
    private static Statement stmt;

    private static ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
    private static ByteArrayOutputStream bErrorStream = new ByteArrayOutputStream();

    @BeforeClass
    public static void redirectSystemStreams(){
        System.setOut(new PrintStream(bOutStream));
        System.setErr(new PrintStream(bErrorStream));
    }

    @BeforeClass
    public static void setConnection(){
        try {
            conn = getConnection();
            stmt = conn.createStatement();
        }catch (SQLException ex){
            fail(ex.getMessage());
        }
    }

    @AfterClass
    public static void closeConnection(){
        try{
            stmt.close();
        }catch (Exception ex){
            fail(ex.getMessage());
        }
        try{
            conn.close();
        }catch (Exception ex){
            fail(ex.getMessage());
        }
    }

    @AfterClass
    public static void cleanDBAfterTests(){
        try {
            setSQLFileForInputStream("ddl_clean");
            Configure conf = new Configure(WORK_DIR);
            conf.confDB(DB_URL, DB_USER, DB_PASS);
        }catch (MBClientException ex){
            fail(ex.getMessage());
        }
    }

    @Test
    public void test1_CleanDB(){
        try {
            //check for missing tables
            String query = "select * from information_schema.tables\n" +
                    "where `table_schema` = '" + TABLE_SCHEMA + "'";

            int size = 0;
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                size++;
            }
            if (size > 0){
                //call clean database
                setSQLFileForInputStream("ddl_clean");
                Configure conf = new Configure(WORK_DIR);
                conf.confDB(DB_URL, DB_USER, DB_PASS);
            }
        }catch (SQLException ex){
            fail(ex.getMessage());
        }catch (MBClientException ex){
            fail(ex.getMessage());
        }
    }

    @Test
    public void test2_CreateDB(){
        try {
            //call configure database
            setSQLFileForInputStream("ddl_create");
            Configure conf = new Configure(WORK_DIR);
            conf.confDB(DB_URL, DB_USER, DB_PASS);

            //check for all tables created
            String query = "select `TABLE_NAME` from information_schema.tables\n" +
                    "where `table_schema` = '" + TABLE_SCHEMA + "'";

            ResultSet rs = stmt.executeQuery(query);
            Set<String> tables = new TreeSet<>();
            while(rs.next()){
                tables.add(rs.getString("TABLE_NAME"));
            }

            Set<String> tablesReq = getAllTables();
            for (String tableReq : tablesReq) {
                if (!tables.contains(tableReq)){
                    fail("Table " + tableReq + " absent in database");
                }
            }
            for (String table : tables) {
                if (!tablesReq.contains(table)){
                    fail("Table " + table + " absent among the required");
                }
            }
        }catch (SQLException ex){
            fail(ex.getMessage());
        }catch (MBClientException ex){
            fail(ex.getMessage());
        }
    }

    @Test
    public void test3_CreateRecords(){
        try {

            //create records in tables
            setSQLFileForInputStream("dml_init");
            Configure conf = new Configure(WORK_DIR);
            conf.confDB(DB_URL, DB_USER, DB_PASS);

            //check records
            for (String table : getAllTables()) {
                String query = "select count(*) as rowcount from `" + TABLE_SCHEMA + "`.`" + table + "`";
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                if (rs.getInt("rowcount") == 0){
                    fail("The data in the table " + table + " was not found.");
                }
            }
        }catch (SQLException ex){
            fail(ex.getMessage());
        }catch (MBClientException ex){
            fail(ex.getMessage());
        }
    }

    @Test
    public void test4_ThrowMBClientException(){
        try {
            //inject non-existent file
            setSQLFileForInputStream("non_exist_file");
            Configure conf = new Configure(WORK_DIR);
            conf.confDB(DB_URL, DB_USER, DB_PASS);
            fail("MBClienException did't throw");
        }catch (MBClientException ex){
            if (!ex.getMessage().contains("java.io.FileNotFoundException")){
                fail("Was thrown another error");
            }
        }

        try {
            //using file with error sql-statements
            setSQLFileForInputStream("dml_error");
            Configure conf = new Configure(WORK_DIR);
            conf.confDB(DB_URL, DB_USER, DB_PASS);
            fail("MBClienException did't throw");
        }catch (MBClientException ex){
            if (!ex.getMessage().contains("com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException")){
                fail("Was thrown another error");
            }
        }

        try {
            //using incorrect URL for connection to DB
            setSQLFileForInputStream("ddl_clean");
            Configure conf = new Configure(WORK_DIR);
            conf.confDB("jdbc:mysql://192.168.1.125:3306", DB_USER, DB_PASS);
            fail("MBClienException did't throw");
        }catch (MBClientException ex){
            if (!ex.getMessage().contains("com.mysql.jdbc.exceptions.jdbc4.CommunicationsException")){
                fail("Was thrown another error");
            }
        }

        try {
            //using incorrect user data for connection to DB
            setSQLFileForInputStream("ddl_clean");
            Configure conf = new Configure(WORK_DIR);
            conf.confDB(DB_URL, DB_USER, "11");
            fail("MBClienException did't throw");
        }catch (MBClientException ex){
            if (!ex.getMessage().contains("java.sql.SQLException: Access denied")){
                fail("Was thrown another error");
            }
        }
    }

    private static void setSQLFileForInputStream(String fileName){
        // Injection user input data
        String fullFilePath = WORK_DIR + "/sql/" + fileName;
        ByteArrayInputStream bais = new ByteArrayInputStream(fullFilePath.getBytes());
        System.setIn(bais);
    }

    private static Connection getConnection(){
        try {
            Class.forName(DB_DRIVER);
            return  DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        }catch (Exception ex){
            fail(ex.getMessage());
            return null;
        }
    }

    private static Set<String> getAllTables(){
        Set<String> result = new TreeSet<>();
        result.add("tySensor");
        result.add("tyWriteSensor");
        result.add("sensor");
        result.add("controller");
        result.add("sensorHistory");
        return result;
    }
}

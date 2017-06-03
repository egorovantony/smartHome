package me.aegorov.smarthome.mbusclient.app;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by anton on 2/14/17.
 */
public class ConfigureDB {
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";

    private String workDir = "";
    private String dbUrl = "";
    private String dbUser = "";
    private String dbPass = "";
    private String ddlCreate = "";
    private List<String> sqlFiles = new ArrayList<>();

    public ConfigureDB(String workDir, String Url, String User, String Pass){
        this.dbUrl = Url;
        this.dbUser = User;
        this.dbPass = Pass;
        this.workDir = workDir;
        this.ddlCreate =
                this.workDir + "/" +
                ConfigureEnvironment.DIR_SQL + "/" +
                ConfigureEnvironment.FILE_DDL_CREATE;
    }
    public void exec() throws MBClientException{
        typeConnParams();

        List<String> stmts = new ArrayList<>();
        List<String> stmtsTmp = new ArrayList<>();

        for (String sqlFile : sqlFiles) {
            stmtsTmp.clear();
            stmtsTmp = parseToStatements(readFile(sqlFile));
            if (!stmtsTmp.isEmpty()){
                stmts.addAll(stmtsTmp);
            }
        }

        execQueries(stmts);
    }
    private void typeConnParams() throws MBClientException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(System.in);

        String pathDdlFile = "";
        System.out.print("SQL instruction files (or type Enter for default setting):");
        try{
            pathDdlFile = br.readLine();
        }catch (IOException ex){
            throw new MBClientException(ex);
        }

        if (pathDdlFile.equals("")){
            this.sqlFiles.add(this.ddlCreate);
        }else{
            String[] split = pathDdlFile.split(",");
            this.sqlFiles = Arrays.asList(split);
        }
    }
    private void execQueries(List<String> strStmts) throws MBClientException{
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(DB_DRIVER);

            System.out.println("Connection to database...");
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            System.out.println("OK");

            stmt = conn.createStatement();
            for (String strStmt : strStmts) {
                System.out.println("Call: " + strStmt);
                stmt.execute(strStmt);
                System.out.println("OK");
            }
            System.out.println("All queries was execute OK");
        }catch (Exception ex){
            throw new MBClientException(ex);
        }
        finally {
            try{
                if (stmt != null) {
                    stmt.close();
                }
            }catch(SQLException ex){
                // nothing
            }
            try{
                if (conn != null){
                    conn.close();
                }
            }catch (SQLException ex){
                // nothing
            }
        }

    }
    private String readFile(String fileName) throws MBClientException{
        File file = new File(fileName);

        StringBuilder sb = new StringBuilder();
        try(FileInputStream fInput = new FileInputStream(file)){
            int i = 0;
            do{
                i = fInput.read();
                if (i != -1) {
                    sb.append((char) i);
                }
            }while(i != -1);

        }catch (IOException ex){
            throw new MBClientException(ex);
        }

        return sb.toString();
    }
    private List<String> parseToStatements(String resource){
        List<String> sts = new ArrayList<>();
        List<String> rows = Arrays.asList(resource.split("\\n"));
        String st = "";
        Iterator<String> resIter = rows.iterator();

        while (resIter.hasNext()) {
            String currRow = resIter.next();

            if (currRow.length() < 3){
                continue;
            }

            if (currRow.substring(0,2).equals("<<")){
                if (st != null){
                    sts.addAll(Arrays.asList(st.split(";")));
                }
                st = "";
            }else if (currRow.substring(0,2).equals(">>")){

            }else{
                st += currRow;
            }

        }
        return sts;
    }
}

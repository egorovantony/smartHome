package me.aegorov.smarthome.mbusclient.dao;

import me.aegorov.smarthome.mbusclient.app.MBClientException;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by anton on 2/6/17.
 */
public class TestConfigureInit {
    private static final String workDir = "src/test/resources/Init";

    @BeforeClass
    public static void redirectSystemStreams(){
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bStream));
    }

    @AfterClass
    public static void setAfterClass(){}

    @Before
    public void setBefore(){
        deleteAllFilesAndDirectories();
    }

    @Test
    public void testCreatingFilesAndDirectories() {
        // Call configure
        Configure conf = new Configure(workDir);
        try {
            conf.confInit();
        } catch (MBClientException ex) {
            fail(ex.getMessage());
        }

        //Checks
        recursiveBypassDir(workDir, getAllPaths());
    }

    @Test
    public void testCreateExcessiveDirectory(){
        // Call configure
        Configure conf = new Configure(workDir);
        try {
            conf.confInit();
        } catch (MBClientException ex) {
            fail(ex.getMessage());
        }

        // Create excessive directory
        try {
            Files.createDirectory(Paths.get(workDir + "/excessive"));
        }catch (IOException ex){
            fail(ex.getMessage());
        }

        //Checks
        boolean assertionErrorCatch = false;
        try {
            recursiveBypassDir(workDir, getAllPaths());
        }catch (AssertionError ex){
            assertionErrorCatch = true;
        }

        if (!assertionErrorCatch){
            fail("There was no error with excessive directory");
        }
    }

    @Test
    public void testDeleteOneOfExistFileOrDirecotryFromList(){
        // Call configure
        Configure conf = new Configure(workDir);
        try {
            conf.confInit();
        } catch (MBClientException ex) {
            fail(ex.getMessage());
        }

        // Delete first exist file or directory
        Set<String> paths = getAllPaths();
        Iterator<String> iterPaths = paths.iterator();
        while(iterPaths.hasNext()){
            iterPaths.next();
            iterPaths.remove();
            break;
        }

        //Checks
        boolean assertionErrorCatch = false;
        try {
            recursiveBypassDir(workDir, paths);
        }catch (AssertionError ex){
            assertionErrorCatch = true;
        }

        if (!assertionErrorCatch){
            fail("There was no error after delete exist file or directory from list");
        }
    }

    @Test
    public void testThrowMBClientException(){
        // Call configure
        Configure conf = new Configure(workDir + "/NonExistDir");
        try {
            conf.confInit();
            fail("MBClientException did't throw.");
        }catch (MBClientException ex){

        }
    }

    private static void deleteAllFilesAndDirectories () {
        File file = new File(workDir);

        for (File currFile : file.listFiles()) {
            if (currFile.isFile()) {
                currFile.delete();
            } else {
                try {
                    FileUtils.deleteDirectory(currFile);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        if (file.listFiles().length > 0) {
            fail("Not all files and directories was deleted");
        }
    }

    private Set<String> getAllPaths () {
        String filePath;
        Set<String> paths = new TreeSet<>();

        filePath = workDir + "/log";
        paths.add(filePath);
        filePath = workDir + "/conf";
        paths.add(filePath);
        filePath = workDir + "/data";
        paths.add(filePath);
        filePath = workDir + "/sql";
        paths.add(filePath);
        filePath = workDir + "/conf/sensors.xml";
        paths.add(filePath);
        filePath = workDir + "/conf/settings.xml";
        paths.add(filePath);
        filePath = workDir + "/sql/ddl_create";
        paths.add(filePath);
        return paths;
    }

    private void recursiveBypassDir(String dir, Set paths) {
        File file = new File(dir);
        for (File currFile : file.listFiles()) {
            assertTrue(paths.contains(currFile.getPath()));
            if (currFile.isDirectory()) {
                recursiveBypassDir(currFile.getPath(), paths);
            }
        }
    }
}

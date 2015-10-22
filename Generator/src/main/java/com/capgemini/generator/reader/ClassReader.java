package com.capgemini.generator.reader;


import com.capgemini.generator.helper.ClassNamesHolder;
import com.capgemini.generator.helper.SystemProperties;
import com.capgemini.generator.liquibase.AddDatabaseChangeLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.regex.Pattern;

import liquibase.changelog.DatabaseChangeLog;

import liquibase.serializer.core.xml.XMLChangeLogSerializer;


public class ClassReader {
    
    public void readClasses(String rootPackagePath, String changeLogDirPath) throws MalformedURLException,
                                                           ClassNotFoundException, IOException {        
        File classesDir = new File(rootPackagePath);        
        getAllFiles(classesDir, rootPackagePath);
        System.out.println(ClassNamesHolder.getInstance().getClassNames());
        URL[] urls = {classesDir.toURI().toURL()};
        URLClassLoader urlClassLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        for(String className : ClassNamesHolder.getInstance().getClassNames()) {            
            Class c = urlClassLoader.loadClass(className);
            
            System.out.println(c.getName());
            if(!c.isEnum()) {
                // create changelog
                AddDatabaseChangeLog databaseChangeLog = new AddDatabaseChangeLog();
                DatabaseChangeLog changeLog = databaseChangeLog.addDatabaseChangeLog(c);
                
                // create file
                File changeLogFile = new File(changeLogDirPath + c.getSimpleName() + ".xml");
                System.out.println(changeLogFile.getCanonicalPath());
                FileOutputStream oFile = new FileOutputStream(changeLogFile, false); 
                XMLChangeLogSerializer xmlChangeLogSerializer = new XMLChangeLogSerializer();
                
                xmlChangeLogSerializer.write(changeLog.getChangeSets(), oFile);
                oFile.flush();
                oFile.close();
            }
        }        
    }
    
    private String convertFilePathToPackagePath(String filePath, String rootPackagePath) {
        
        String pathWithoutRootPackagePath = filePath.substring(filePath.indexOf(rootPackagePath) + rootPackagePath.length() + 1, filePath.length());
        return pathWithoutRootPackagePath.replaceAll(Pattern.quote("\\"), ".");
    }
            
    private void getAllFiles(File file, String rootPackagePath) throws IOException {
        
        File[] listFiles = file.listFiles();
        if(listFiles != null) {
            for(File filesInside : listFiles) {
                if(filesInside.isDirectory()) {
                    getAllFiles(filesInside, rootPackagePath);
                } else {
                    if(filesInside.getName().endsWith(".class") && !filesInside.getName().equals("package-info.class") 
                        && !filesInside.getName().equals("ObjectFactory.class") && !filesInside.getName().equals("JAXBDebug.class")) {
                        ClassNamesHolder.getInstance().addClassName(convertFilePathToPackagePath(filesInside.
                                                                                                 getAbsolutePath().
                                                                                                 substring(0, filesInside.getAbsolutePath().
                                                                                                           indexOf(".class")), rootPackagePath));
                    }
                }
            }
        }        
    }

    public static void main(String[] args) {
        ClassReader classReader = new ClassReader();
        if(args.length != 3) {
            throw new RuntimeException("Missing command line params");
        }
        try {
            SystemProperties.getInstance().setPropertiesFilePath(args[2]);
            classReader.readClasses(args[0], args[1]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

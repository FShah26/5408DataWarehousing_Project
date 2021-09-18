package parsing;

import constants.FileConstants;
import ds.Table;
import metadata_ds.Metadata;
import metadata_ds.TableProperties;
import queryExecution.Controller;
import queryExecution.ExecutionEngine;
import remotestorage.RemoteServerConnection;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.regex.*;

public class Parsing {

    String query;
    String deleteRegex = "^delete from (?!_)\\w*( (where)[a-z0-9]*=[a-z0-9]*)?;$";
    String updateRegex = "^update (?!_)\\w* set (((([a-z0-9]+)=([a-z0-9]+))([,]*))+) ((where) [a-z0-9]*=[a-z0-9]*)?;$";
   // String updateRegex = "^update (?!_)\\w* set (((([a-z0-9]+)=([a-z0-9]+))([,]*))+)[^,];$";
    //String updateRegex = "^update (?!_)\\w* set (((([a-z0-9]+)=([a-z0-9]+))([,]*))+)(;*| ((where) [a-z0-9]*=[a-z0-9]*)?;)$";
    String insertRegex = "^insert into (?!_)\\w* VALUES ?\\(((([a-z0-9\\$]+)([,]*))+)[^,]\\);$";
    String selectRegex = "^select (\\*|((([a-z]+)([,]*))+)) from (?!_)\\w*( (where) [a-z0-9]*=[a-z0-9]*)?;$";
    String createDBRegex = "^create database [^;]*;$";
    String createTableRegex = "^create table (?!_)\\w*\\(((([a-z0-9]+) (int|varchar\\(\\d+\\))([,]*))+)(?<!,)\\);$";
    String useDbRegex = "^use +[^;]*;$";
    String commitRegex ="^commit;$";
    Metadata metadata;
    String dbName = "testDb";
    ExecutionEngine executionEngine;
    Controller controller;
    Map<String,Table> data = new HashMap<>();
    Map<String,Map> userData = new HashMap<>();
    RemoteServerConnection remoteServerConnection;
    Thread controllerThread;
    Thread main;


    public Parsing(ExecutionEngine executionEngine)
    {
        metadata = new Metadata();
        metadata.updateMetadataProps(FileConstants.LOCAL_DB_LOCATION);
        metadata.updateMetadata();
        this.executionEngine = executionEngine;
        controller = new Controller(executionEngine);
        remoteServerConnection = new RemoteServerConnection();
        controllerThread = new Thread(controller);
        controllerThread.start();
        main = Thread.currentThread();
    }

    public void parse() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Query for Processing");
        query = input.nextLine();
        String[] queryToken = query.split(" ");
        TableProperties prop = new TableProperties();
        executionEngine.query = query;

        try {
            switch (queryToken[0].toUpperCase()) {
                case "COMMIT;":
                    if (syntaxValidation(query, commitRegex)) {
                        Logs logs = new Logs();
                        /*try {
                            logs.generalLogs();
                            logs.eventLogs(query);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        executionEngine.dataCommitToFile(data);

                    }
                    else {
                        System.out.println("Syntax Error");
                        System.out.println("The expected format is... COMMIT;");
                    }
                    break;
                case "USE":
                    if (syntaxValidation(query, useDbRegex)) {
                        String dbName = queryToken[1].substring(0,queryToken[1].length() - 1);
                        if(metadata.checkDBExist(dbName))
                        {
                        metadata.updateMetadataProps(FileConstants.LOCAL_DB_LOCATION);
                        metadata.loadMetaData();
                        }
                    } else {
                        System.out.println("Syntax Error");
                        System.out.println("The expected format is... USE <DATABASENAME>;");
                    }
                case "SELECT":
                    if (syntaxValidation(query, selectRegex)) {
                        Logs logs = new Logs();
                        /*try {
                            logs.generalLogs();
                            logs.eventLogs(query);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        if(metadata.checkWithMetaData(query))
                        {
                            // write into the files
                            //execute Selection logic
//                         executionEngine.Select(query);
                            if(executionEngine.getTableLocFromQuery(query).equals("local"))
                            {
                                controller.setQuery(query);
                                main.sleep(15000);
                            }
                            else
                            {
                                remoteServerConnection.pushFileToRemote(query);
                                main.sleep(15000);
                                remoteServerConnection.pullFileFromRemote();

                            }

                        }
                    } else {
                        System.out.println("Syntax Error");
                        System.out.println("The expected format is... SELECT * FROM <TABLENAME> or SELECT <COL1,COL2..COLN> FROM TABLENAME;");
                    }
                    break;
                case "INSERT":
                    if (syntaxValidation(query, insertRegex)) {
                        Logs logs = new Logs();
                        /*try {
                            logs.generalLogs();
                            logs.eventLogs(query);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        if(executionEngine.getTableLocFromQuery(query).equals("local"))
                        {
                            controller.setQuery(query);
                            main.sleep(15000);
                            this.data = executionEngine.fileTables;
                        }
                        else
                        {
                            remoteServerConnection.pushFileToRemote(query);
                            main.sleep(15000);
                            remoteServerConnection.pullFileFromRemote();

                        }

//                    if(executionEngine.Insert(query)){
//                        System.out.println("Insertion Successful");

//                    }
                    } else {
                        System.out.println("Syntax Error");
                        System.out.println("The expected format is... INSERT INTO <TABLENAME> VALUES(VALUE1,VALUE2,...VALUEN);");
                    }
                    break;

                case "UPDATE":
                    if (syntaxValidation(query, updateRegex)) {
                        Logs logs = new Logs();
                        /*try {
                            logs.generalLogs();
                            logs.eventLogs(query);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        if(metadata.checkWithMetaData(query))
                        {
                            logs = new Logs();
                            /*try {
                                logs.generalLogs();
                                logs.eventLogs(query);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                            if(executionEngine.getTableLocFromQuery(query).equals("local"))
                            {
                                controller.setQuery(query);
                                this.data = executionEngine.fileTables;
                                main.sleep(15000);
                            }else
                            {
                                remoteServerConnection.pushFileToRemote(query);
                                main.sleep(15000);
                                remoteServerConnection.pullFileFromRemote();

                            }
//                        if(executionEngine.update(query))
//                        {
//                            this.data = executionEngine.fileTables;
//                            System.out.println("update successfull");
//                        }
//                            controller.setQuery(query);
                        }
                    } else {
                        System.out.println("Syntax Error");
                        System.out.println("The expected format is... UPDATE <TABLENAME> SET <COL1=VAL1,COL2=VAL2...COLN=VALN>WHERE <CONDITION>;");
                    }
                    break;
                case "DELETE":
                    if (syntaxValidation(query, deleteRegex)) {
                        if(metadata.checkWithMetaData(query))
                        {
                            /*Logs logs = new Logs();
                            try {
                                logs.generalLogs();
                                logs.eventLogs(query);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                            if(executionEngine.getTableLocFromQuery(query).equals("local"))
                            {
                                controller.setQuery(query);
                                this.data = executionEngine.fileTables;
                                main.sleep(10000);
                            }else
                            {
                                remoteServerConnection.pushFileToRemote(query);
                                main.sleep(15000);
                                remoteServerConnection.pullFileFromRemote();

                            }
//                        if(executionEngine.delete(query))
//                        {
//                            this.data = executionEngine.fileTables;
//                            executionEngine.fileTables = this.data;
//                        }
//                            controller.setQuery(query);
                        }
                    } else {
                        System.out.println("Syntax Error");
                        System.out.println("The expected format is... DELETE FROM <TABLENAME> WHERE <CONDITION>;");
                    }
                    break;

                case "CREATE":
                    if (queryToken[1].equalsIgnoreCase("table")) {
                        if (syntaxValidation(query, createTableRegex)) {
                            if(metadata.checkWithMetaData(query))
                            {
                                Logs logs = new Logs();
                                /*try {
                                    logs.generalLogs();
                                    logs.eventLogs(query);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/
                                TableCreation tableCreation = new TableCreation();
                                tableCreation.createTable(query);
                                executionEngine.UpdateMetadata();
                                this.metadata = executionEngine.md;
                            }
                        } else {
                            System.out.println("Syntax Error");
                            System.out.println("The expected format is... CREATE TABLE <TABLENAME>(COLUMN1 <INT|VARCHAR(digit)>, COLUMN2 <INT|VARCHAR(digit)>);");
                        }
                    } else {
                        if (syntaxValidation(query, createDBRegex)) {
                            /*Logs logs = new Logs();
                            try {
                                logs.generalLogs();
                                logs.eventLogs(query);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                            DatabaseCreation databaseCreation= new DatabaseCreation(query);
                            //databaseCreation.createDatabase(query);

                        } else {
                            System.out.println("Syntax Error");
                            System.out.println("The expected format is... CREATE DATABASE <DATABASENAME>;");
                        }
                    }
                    break;

                default: System.out.println("Not a valid query");
                    break;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
   }

    public boolean syntaxValidation (String query, String regex)
    {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(query);
        Boolean validity = match.find();
        return validity;
    }
    public void stop()
    {
//        controller.stop();
    }
}


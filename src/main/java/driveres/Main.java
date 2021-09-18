package driveres;

import constants.FileConstants;
import ds.Table;
import metadata_ds.Metadata;
import parsing.ERD;
import parsing.UserAuthentication;
import parsing.Parsing;
import parsing.SQLDump;
import queryExecution.Controller;
import queryExecution.ExecutionEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static String userName, password;
    public static Metadata metadata;
    public static ExecutionEngine executionEngine;
    public static Controller controller;
    public static void main(String[] args) {


        String dbName = "testDb";
        metadata = new Metadata();
        FileConstants.settingLocations();
        metadata.updateMetadataProps(FileConstants.LOCAL_DB_LOCATION);
        metadata.updateMetadata();
        executionEngine = new ExecutionEngine(metadata);

//        controller = new Controller(executionEngine);
//        Thread controllerThread = new Thread(controller);
//        controllerThread.start();


        boolean flag = true, validation = false;
        String typeOfOperation,userinput;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Username");
        userName = input.nextLine();
        System.out.println("Enter Password");
        password = input.nextLine();
        UserAuthentication login = new UserAuthentication();
        validation = login.validation(userName,password);

        if(validation){
            System.out.println("User Authenticated");
            Parsing parsing = new Parsing(executionEngine);
            ERD erd = new ERD();
            while (flag) {
                System.out.println("Enter 1 for SqlDump, 2 for CRUD operations and 3 for ERD");
                typeOfOperation = input.nextLine();
                if (typeOfOperation.equalsIgnoreCase("1")) {
                    SQLDump sqlDump = new SQLDump();
                } else if (typeOfOperation.equalsIgnoreCase("2")){
                    parsing.parse();
//                    ExecutorService threadPool = Executors.newCachedThreadPool();
//                    Future<Boolean> futureTask = threadPool.submit(() -> controller.execute());
//                    while (!futureTask.isDone())
//                    {
//
//                    }

                }
                else if (typeOfOperation.equalsIgnoreCase("3")){
                    try {
                        erd.getERD();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    System.out.println("Invalid input");
                    continue;
                }
                System.out.println("Do you want to execute another query? (yes / no)");
                userinput = input.nextLine();
                if (userinput.equalsIgnoreCase("YES")) {
                    flag = true;
                } else {
                    parsing.stop();
                    flag = false;
                }
            }
        }
        else{
            System.out.println("INVALID USER");
        }


    }
}

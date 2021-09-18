package parsing;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import constants.FileConstants;
import metadata_ds.TableColDS;
import metadata_ds.TableColumn;
import metadata_ds.TableProperties;
import remotestorage.GCPStorage;
import remotestorage.RemoteServerConnection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SQLDump {

    String tableName, databaseName;
    String metadataPath = FileConstants.LOCAL_DB_LOCATION + "/meta-" + tableName + ".txt";
    RemoteServerConnection remoteServerConnection;

    public SQLDump() {
        try {
            remoteServerConnection = new RemoteServerConnection();
            getMetadata();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getMetadata() throws IOException {

        FileReader fileReader = null;

        String[] tablenames;
        File directory = new File(FileConstants.LOCAL_DB_LOCATION);
        File[] files = directory.listFiles((d, name) -> name.startsWith("meta"));
        for (File file : files) {
            String[] fileName = file.getName().split("\\.");
            fileReader = new FileReader(FileConstants.LOCAL_DB_LOCATION + "/" + file.getName());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = "", mergedLine = "";
            while ((line = bufferedReader.readLine()) != null) {
                mergedLine += (line + ",");
            }
            tablenames=fileName[0].split("-");
            System.out.println("create table "+tablenames[1]+"("+mergedLine.replaceAll("~", " ").replaceAll(",$","")+");");
        }


        System.out.println("****************SQLDUMP FOR REMOTE TABLES***********************");
        String remoteFiles = "";
        remoteFiles = remoteServerConnection.readDirectory();
        Scanner sc = new Scanner(remoteFiles);
        while (sc.hasNextLine()) {
            String file = sc.nextLine();
            String[] filename = file.split("\\-");
            if (filename[0].equals("meta")) {
                String fileContent = new String(remoteServerConnection.readFile(file));
                Scanner scContents = new Scanner(fileContent);
                String[] fileName = file.split("\\.");
                int index = 0;
                String remoteMerge="";
                while (scContents.hasNextLine()) {
                    remoteMerge += (scContents.nextLine()+",");
                }
                tablenames = fileName[0].split("-");
                System.out.println("create table " + tablenames[1] + "(" + remoteMerge.replaceAll("~", " ").replaceAll(",$", "") + ");");

            }
        }




        /*Bucket gcp = GCPStorage.getStorage();
        Page<Blob> blobs = gcp.list();

        for (Blob blob : blobs.iterateAll()) {
            String[] filename = blob.getName().split("\\-");
            if (filename[0].equals("meta")) {
                String[] fileName = blob.getName().split("\\.");
                String fileContent = new String(blob.getContent());
                Scanner sc = new Scanner(fileContent);
                sc.nextLine();
                int index = 0;
                String remoteMerge="";
                while (sc.hasNextLine()) {
                    remoteMerge += (sc.nextLine()+",");
                }
                tablenames = fileName[0].split("-");
                System.out.println("create table " + tablenames[1] + "(" + remoteMerge.replaceAll("~", " ").replaceAll(",$", "") + ");");

            }
        }*/

    }
}

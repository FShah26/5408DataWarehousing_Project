package parsing;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import constants.FileConstants;
import remotestorage.GCPStorage;
import remotestorage.RemoteServerConnection;

import java.io.*;
import java.util.Scanner;

public class ERD {

    public void getERD() throws IOException {

        //public static void main(String[] args) throws IOException {
        FileReader fileReader = null;
        RemoteServerConnection remoteServerConnection;
        remoteServerConnection = new RemoteServerConnection();
        String[] tablenames;
        File directory = new File(FileConstants.LOCAL_DB_LOCATION);
        File[] files = directory.listFiles((d, name) -> name.startsWith("meta"));
        for (File file : files) {
            String[] fileName = file.getName().split("\\.");
            fileReader = new FileReader(FileConstants.LOCAL_DB_LOCATION + "/" + file.getName());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = "", mergedLine = "";
            tablenames = fileName[0].split("-");
            //System.out.println("The metadata for " + tablenames[1] + " is:");
            System.out.println("-----------------  " + tablenames[1].toUpperCase() + "  -----------------");
            while ((line = bufferedReader.readLine()) != null) {

                System.out.println(line);
            }
        }

        System.out.println("****************SQLDUMP FORE REMOTE TABLES***********************");

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
                tablenames = fileName[0].split("-");
                System.out.println("-----------------  " + tablenames[1].toUpperCase() + "  -----------------");
                while (scContents.hasNextLine()) {
                    System.out.println(scContents.nextLine());
                }
            }
        }
    }
}

            /* Bucket gcp = GCPStorage.getStorage();
            Page<Blob> blobs = gcp.list();

            for (Blob blob : blobs.iterateAll()) {
                String[] filename = blob.getName().split("\\-");
                if (filename[0].equals("meta")) {
                    String[] fileNames = blob.getName().split("\\.");
                    String fileContent = new String(blob.getContent());
                    Scanner sc = new Scanner(fileContent);
                    sc.nextLine();
                    tablenames = fileNames[0].split("-");
                    System.out.println("-----------------  " + tablenames[1].toUpperCase() + "  -----------------");
                    while (sc.hasNextLine()) {
                        System.out.println((sc.nextLine()));
                    }
                }
            }
        }
    }

*/

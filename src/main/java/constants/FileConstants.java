package constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileConstants {
    public static final String INTEGER_ATTRIBUTE = "Int";
    public static final String STRING_ATTRIBUTE = "String";

    /*public static final String LOCAL_DB_LOCATION = "C:\\Users\\HOME\\Sravani\\5408\\csci-5408-w2021-b00884260-sravani_pinninti\\GroupProject_Latest\\5408-project\\createDB\\";
    public static  Path GDD_PATH = Paths.get(".\\assignment-3-307509-b015ddfee461.json");
    public static final String GDD_FILE = GDD_PATH.toAbsolutePath().normalize().toString();
    public static Path localRealtivePath= Paths.get("/home/sravanireddy30/DBMS/createDB");
    public static final String GCP_CONFIG_FILE="/home/sravanireddy30/DBMS/QueryRequestResponse/";
    public static final String GCP_DB_Directory = "/home/sravanireddy30/DBMS/createDB/";
    //public static final String LOCAL_DB_LOCATION = "/home/sravanireddy30/DBMS/createDB/";*/
    // public static final String LOCAL_DB_LOCATION = "C:\\Users\\hashi\\Desktop\\Uni\\5408\\Project\\5408-project\\databases";
    //    public static final String LOCAL_DB_LOCATION = "E:\\MACS\\5408_git\\Project\\testDb\\";
    //public static final String GCP_CONFIG_FILE = "E:\\MACS\\5408_git\\Project\\5408-project\\assignment-3-307509-b015ddfee461.json";
    //public static final String GCP_CONFIG_FILE = "C:\\Users\\HOME\\Desktop\\5408_Project\\createDB\\assignment-3-307509-b015ddfee461.json";
    //public static final String GCP_CONFIG_FILE = "C:\\Users\\hashi\\Desktop\\Uni\\5408\\Project\\5408-project\\assignment-3-307509-b015ddfee461.json";
    //public static final String GDD_FILE = "/home/sravanireddy30/DBMS/assignment-3-307509-b015ddfee461.json";
    //public static Path localRealtivePath= Paths.get(".\\createDB");
    //public static final String LOCAL_DB_LOCATION=localRealtivePath.toAbsolutePath().normalize().toString() + "\\";
    public static Path localRealtivePath;
    public static String LOCAL_DB_LOCATION;
    public Path GDD_PATH;
    public static String GDD_FILE;
    public static String GCP_CONFIG_FILE;
    public static String GCP_DB_Directory;
    public static  int flag;
    public static String GCP_REQUEST;
    public static String GCP_RESPONSE;

    public static void settingLocations() {

        System.out.println("In methos");
       final int flag = 0;
        if (flag ==0) {
            localRealtivePath = Paths.get(".//createDB");
            System.out.println("The local path is:" +localRealtivePath);
           LOCAL_DB_LOCATION = localRealtivePath.toAbsolutePath().normalize().toString() + "\\";
            Path GDD_PATH = Paths.get(".//assignment-3-307509-b015ddfee461.json");
            GDD_FILE = GDD_PATH.toAbsolutePath().normalize().toString();
            GCP_CONFIG_FILE = "/home/sravanireddy30/DBMS/QueryRequestResponse/";
            GCP_DB_Directory = "/home/sravanireddy30/DBMS/createDB/";

        }
        else
        {
            Path localRealtivePath = Paths.get(".//createDB");
            LOCAL_DB_LOCATION = localRealtivePath.toAbsolutePath().normalize().toString() +"/";
            Path GDD_PATH = Paths.get(".//assignment-3-307509-b015ddfee461.json");
            GDD_FILE = GDD_PATH.toAbsolutePath().normalize().toString();
            GCP_CONFIG_FILE = "/home/sravanireddy30/DBMS/QueryRequestResponse/";
            GCP_DB_Directory = "/home/sravanireddy30/DBMS/createDB/";
            GCP_REQUEST ="/home/sravanireddy30/DBMS/createDB/QueryRequestResponse/requestFile.txt";
            GCP_RESPONSE ="/home/sravanireddy30/DBMS/createDB/QueryRequestResponse/responseFile.txt";
        }
    }
   //0 for local and 1 for remote

    //public static Path GlobalRealtivePath= Paths.get(".\\createDB");
    //public static final String GCP_CONFIG_FILE=GlobalRealtivePath.toAbsolutePath().normalize().toString();

    public void display()
    {
        System.out.println("The local full path is:" +LOCAL_DB_LOCATION);
        System.out.println("The GDD Path full path is:" +GDD_PATH);
        System.out.println("The GDD File full path is:" +GDD_FILE);
        System.out.println("The Global full path is:" +GCP_CONFIG_FILE);
    }
}

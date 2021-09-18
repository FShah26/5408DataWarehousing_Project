package driveres;

import ds.Table;
import fileloding.LoadFromFile;
import filewriting.WriteToFile;

import java.io.IOException;

public class FileDriver {
    public static void main(String[] args) throws IOException {
//        // Remote File
//        LoadRemoteFile loadRemoteFile = new LoadRemoteFile("test1.txt", "test");
//        System.out.println(loadRemoteFile.read());

        // Local File
        LoadFromFile loadFromFile = new LoadFromFile("test.txt", "test");
        Table table = loadFromFile.read();
        System.out.println(table);

        // Write Local File
        WriteToFile writeLocalFile = new WriteToFile("test.txt");
        writeLocalFile.write(table);
//
//        // Write Remote File
//        WriteRemoteFile writeRemoteFile = new WriteRemoteFile("test2.txt");
//        writeRemoteFile.write(table);
    }
}

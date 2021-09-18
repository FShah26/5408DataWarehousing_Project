package locking;

import constants.FileConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class TableLocker {
    static String LOCK_FILE_NAME = "LockerFile.txt";

    public static void lockTable(String username, String tableName) throws IOException {
        ArrayList<String> fileData = readFile();
        fileData.add(tableName + "@" + username);
        writeToLockFile(fileData);
    }

    public static void unlockTable(String username, String tableName) throws IOException {
        ArrayList<String> fileData = readFile();
        String lockValue = tableName + "@" + username;
        Iterator line = fileData.iterator();
        while (line.hasNext()) {
            String curLine = (String) line.next();
            curLine.equals(lockValue);
            line.remove();
        }
        writeToLockFile(fileData);
    }

    private static void writeToLockFile(ArrayList<String> fileData) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (String fileDatum : fileData) {
            builder.append(fileDatum + "\n");
        }
        String toWrite = builder.toString();
        Files.writeString(Paths.get(FileConstants.LOCAL_DB_LOCATION + LOCK_FILE_NAME), toWrite);
    }

    // In real scenario rather than isRemote we'll use an URL
    public static boolean checkIfLocked(String username, String tableName) throws IOException {
        ArrayList<String> fileData = readFile();
        return lockCheck(fileData, username, tableName);
    }

    private static ArrayList<String> readFile() throws IOException {
        ArrayList<String> fileData = new ArrayList<>();

        File file = new File(FileConstants.LOCAL_DB_LOCATION + LOCK_FILE_NAME);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        if (file.length() != 0) {
            while ((line = reader.readLine()) != null) {
                fileData.add(line);
            }
        }
        return fileData;
    }

    private static boolean lockCheck(ArrayList<String> fileData, String userName, String tableName) {
        for (String line : fileData) {
            String[] split = line.split("@");
            if (split[0].equals(tableName) && !split[1].equals(userName)) {
                return true;
            }
        }
        return false;
    }
}

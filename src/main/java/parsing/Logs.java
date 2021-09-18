package parsing;

import constants.FileConstants;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class Logs {
    void generalLogs()  throws IOException {
        String[] listOfFiles;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        File directory = new File(FileConstants.LOCAL_DB_LOCATION);
        File[] files = directory.listFiles((d, name) -> !name.startsWith("meta") && !name.startsWith("General") && !name.startsWith("Event") && !name.startsWith("credentials"));
        File generalLogFile = new File(FileConstants.LOCAL_DB_LOCATION + "/GeneralLogs.txt");
        generalLogFile.createNewFile();
        FileWriter fileWriter = new FileWriter(FileConstants.LOCAL_DB_LOCATION + "/GeneralLogs.txt", true);
        fileWriter.write( "***************** "+ dateFormat.format(timestamp)+" *****************" + "\n");
        long count = 0;
        if (files.length > 0) {
        for (File file : files) {
            String[] fileName = file.getName().split("\\.");
            Path path = Paths.get(directory + "/" + file.getName());
            count = Files.lines(path).count();
            fileWriter.write(fileName[0] + " : has " + count + " records." + "\n");
        }
    }
        fileWriter.close();
    }

    void eventLogs(String query) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        File eventLogFile = new File(FileConstants.LOCAL_DB_LOCATION + "/EventLogs.txt");
        eventLogFile.createNewFile();
        FileWriter fileEventWriter = new FileWriter(FileConstants.LOCAL_DB_LOCATION + "/EventLogs.txt", true);
        fileEventWriter.write("***************** "+ dateFormat.format(timestamp)+" *****************"+ "\n");
        fileEventWriter.write(query + "\n" );
        fileEventWriter.close();
    }
}

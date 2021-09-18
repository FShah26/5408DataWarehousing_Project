package fileloding;

import constants.FileConstants;
import ds.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadFromFile extends FileReadingBase {
    public LoadFromFile(String pathToFile, String tableName) {
        this.tableName = tableName;
        this.pathToFile = FileConstants.LOCAL_DB_LOCATION + pathToFile;
    }

    public Table read() throws IOException {
        File file = new File(pathToFile);
        ArrayList<String> fileLines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String eachLine;

        if(file.length() != 0) {
            while ((eachLine = reader.readLine()) != null) {
                fileLines.add(eachLine);
            }
        }
        return parseFileText(fileLines);
    }
}

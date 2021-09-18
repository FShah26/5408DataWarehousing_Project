package filewriting;

import constants.FileConstants;
import ds.Table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WriteToFile extends FileWritingBase {

    public WriteToFile(String fileName) {
        this.filePath = FileConstants.LOCAL_DB_LOCATION + fileName;
    }

    @Override
    public void write(Table table) throws IOException {
        Files.writeString(Paths.get(filePath), formatTableToWrite(table));
    }
}

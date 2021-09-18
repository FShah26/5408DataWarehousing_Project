package remotestorage;

import com.google.cloud.storage.Bucket;
import constants.FileConstants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GDDWriter {
    public static void replaceGDDWithString(String stringToWrite) throws IOException {
        Bucket database = GCPStorage.getStorage();
        database.create(FileConstants.GDD_FILE, stringToWrite.getBytes(StandardCharsets.UTF_8));
    }

    public static void appendGDD(String stringToAppend) throws IOException {
        replaceGDDWithString(GDDReader.readGDD() + stringToAppend + "\n");
    }

    public static void createNewGDD() throws IOException {
        Bucket database = GCPStorage.getStorage();
        database.create(FileConstants.GDD_FILE,"".getBytes(StandardCharsets.UTF_8));

    }
}

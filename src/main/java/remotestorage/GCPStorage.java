package remotestorage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import constants.FileConstants;

import java.io.FileInputStream;
import java.io.IOException;

public class GCPStorage {
    public static Bucket getStorage() throws IOException {
        StorageOptions storageOpts = StorageOptions.newBuilder().setProjectId("assignment-3-307509")
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(FileConstants.GDD_FILE))).build();
        Storage storageService = storageOpts.getService();
        Bucket database = storageService.get("5408-databases");
        return database;
    }
}

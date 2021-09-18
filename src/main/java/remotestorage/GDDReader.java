package remotestorage;

import constants.FileConstants;

import java.io.IOException;

public class GDDReader {
    public static String readGDD() throws IOException {
        return new String(GCPStorage.getStorage().get(FileConstants.GDD_FILE).getContent());
    }
}

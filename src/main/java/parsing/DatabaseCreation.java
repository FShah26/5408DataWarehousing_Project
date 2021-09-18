package parsing;

import constants.FileConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class DatabaseCreation {


    String databaseName;

    DatabaseCreation(String query){
        databaseName = getDatabaseName(query);
        createDatabase(databaseName);
    }
    public String getDatabaseName(String query)
    {
        String[] queryToken = query.split(" ");
        String[] dbName = queryToken[2].split(";");
        return dbName[0];
    }
    public void createDatabase (String databaseName){

        String fullDatabasePath = FileConstants.LOCAL_DB_LOCATION + databaseName;
        boolean flag = false;
        try {
            File file = new File(fullDatabasePath);
            if (file.exists())
                throw new FileAlreadyExistsException(fullDatabasePath);
            flag = file.mkdir();
        } catch (FileAlreadyExistsException e) {
            System.out.println("A database with name " + databaseName + " already exists");
        }
        if (flag)
        {
            System.out.println("Database created succesfully");
        }
    }

}

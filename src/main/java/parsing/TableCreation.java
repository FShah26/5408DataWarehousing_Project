package parsing;

import constants.FileConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;

public class TableCreation {

    public boolean createTable (String query){
        String[] queryToken = query.split(" ");
        String[] dbtableName = queryToken[2].split("\\(");
        String[] metaToken = query.split("\\((?!\\d)");
        String[] colschema = metaToken[1].split("\\);");
        String[] splitColSchema = colschema[0].split(",");
        String[] splitDataType= new String[splitColSchema.length];

        String filefullpath = FileConstants.LOCAL_DB_LOCATION  + dbtableName[0] + ".txt";
        String metafullpath = FileConstants.LOCAL_DB_LOCATION + "meta-" + dbtableName[0] + ".txt";

        boolean flag = false;
        try {
            File file = new File(filefullpath);
            if (file.exists())
                throw new FileAlreadyExistsException(filefullpath);
            flag = file.createNewFile();
            file = new File(metafullpath);
            file.createNewFile();
        } catch (FileAlreadyExistsException e) {
            System.out.println("A table with name " + dbtableName[0] + " already exists");
        } catch (IOException e) {
            System.out.println("There is no database with name " + dbtableName[0]);
        }
        if (flag) {
            try {
                System.out.println("Table created sucessfully");
                FileWriter fileWriter = new FileWriter(metafullpath);
                for (int i = 0; i < splitColSchema.length; i++) {
                    fileWriter.write(splitColSchema[i].replaceAll(" ", "~") + "\n");
                }
                fileWriter.close();
                FileWriter fileMetaWriter = new FileWriter(filefullpath);
                String datatype;
                for(int i=0;i<splitColSchema.length;i++) {
                    splitDataType[i] = splitColSchema[i].split(" ")[1];
                    datatype = splitDataType[i];
                    //System.out.println("Th");
                    if (i == (splitColSchema.length - 1)) {
                        if (datatype.equalsIgnoreCase("int"))
                            fileMetaWriter.write("Int");
                        else
                            fileMetaWriter.write("String");
                    } else {
                            if (datatype.equalsIgnoreCase("int"))
                                fileMetaWriter.write("Int,");
                            else
                                fileMetaWriter.write("String,");
                        }
                    }

                fileMetaWriter.close();

            }catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            System.out.println("Table not created");
            return false;
        }
    }
}

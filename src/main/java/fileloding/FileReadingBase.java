package fileloding;

import constants.FileConstants;
import ds.Row;
import ds.RowImpl;
import ds.Table;
import ds.TableImpl;

import java.io.IOException;
import java.util.ArrayList;

public abstract class FileReadingBase {
    String pathToFile;
    String tableName;

    protected Table parseFileText(ArrayList<String> tableLines) {
        Table table = new TableImpl(tableName);
        // First line contains attributes types
        String attributeTypesString = tableLines.get(0);
        table.setAttributeTypes(attributeTypesString);

        String[] attributeTypesArray = attributeTypesString.split(",");

        for (int j = 1; j < tableLines.size(); j++) {
            String tableLine = tableLines.get(j);
            Row row = new RowImpl();
            String[] eachAttributeValue = tableLine.split(",");
            for (int i = 0; i < eachAttributeValue.length; i++) {
                if (attributeTypesArray[i].equals(FileConstants.INTEGER_ATTRIBUTE)) {
                    row.insertAttribute(Integer.parseInt(eachAttributeValue[i].strip()));
                } else {
                    row.insertAttribute(eachAttributeValue[i].strip());
                }
            }
            table.insert(row);
        }
        return table;
    }

    abstract Table read() throws IOException;
}

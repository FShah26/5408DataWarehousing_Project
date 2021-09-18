package filewriting;

import ds.Table;

import java.io.IOException;

public abstract class FileWritingBase {
    String filePath;

    public String formatTableToWrite(Table table) {
        StringBuilder builder = new StringBuilder();
        builder.append(table.getAttributeTypes());
        builder.append(table.toString());
        return builder.toString();
    }

    public abstract void write(Table table) throws IOException;
}
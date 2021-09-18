package driveres;

import ds.*;

import java.util.*;

public class DSDriver {
    public static void main(String[] args) {
        // Example Usage of Table
        Table table = new TableImpl("Test_Table");

        System.out.println("======= INSERTION ========");
        // Inserting
        for (int i = 0; i < 5; i++) {
            Row row = new RowImpl();
            row.insertAttribute(i);
            row.insertAttribute("Attribute1-" + i);
            row.insertAttribute("Attribute2-" + i);
            table.insert(row);
        }
        // Printing a table
        System.out.println(table);

        // Get
        System.out.println("======= GET ========");

        Value valueToSearchFor = new ValueImpl("Attribute1-1", false);
        int attributeLocation = 1;
        List<Row> rowList = table.get(valueToSearchFor, attributeLocation);
        System.out.println(rowList);

        // Print only specific attributes
        System.out.println("======= PRINT ONLY SPECIFIC ========");

        List<Integer> onlyPrint = new ArrayList<>(Arrays.asList(0, 1));
        System.out.println(table.toString(onlyPrint));

        // Delete
        System.out.println("======= Delete =======");
        Value valueToDelete = new ValueImpl("Attribute1-2", false);
        attributeLocation = 1;
        if (table.delete(valueToDelete, attributeLocation)) {
            System.out.println("Deleted Row");
            System.out.println(table);
        } else {
            System.out.println("Key not found to delete");
        }

        // Update
        System.out.println("======= UPDATE ========");
        Value keyToReplaceRow = new ValueImpl("Attribute1-1", false);
        attributeLocation = 1;

        Map<Integer, Value> valuesToUpdateInTheRow = new HashMap<>();
        valuesToUpdateInTheRow.put(0, new ValueImpl("5000", true)); // Change attribute at 0 location with 5000
        valuesToUpdateInTheRow.put(2, new ValueImpl("TESTING", false)); // Change attribute at 0 location with TESTING

        // Inserting new Row on the attribute having the value "keyToReplaceRow" at 1 attribute location.
        table.update(keyToReplaceRow, attributeLocation, valuesToUpdateInTheRow);

        System.out.println(table);
    }
}
